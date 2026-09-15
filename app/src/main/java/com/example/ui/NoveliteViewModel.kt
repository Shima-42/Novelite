package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.NoveliteLogger
import com.example.data.Achievement
import com.example.data.AuthorStatus
import com.example.data.Chapter
import com.example.data.Comment
import com.example.data.CustomCollection
import com.example.data.DayStatus
import com.example.data.LibraryTab
import com.example.data.MilestoneEvent
import com.example.data.NoveliteNotification
import com.example.data.NoveliteRepository
import com.example.data.ReaderConfig
import com.example.data.ReportItem
import com.example.data.Story
import com.example.data.StoryStatus
import com.example.data.StreakHistoryDay
import com.example.data.UserProfile
import com.example.data.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
  AUTH,
  ONBOARDING,
  HOME,
  EXPLORE,
  LIBRARY,
  WRITE,
  PROFILE,
  STORY_DETAIL,
  READER,
  WRITER_DRAFT,
  STREAK_DASHBOARD,
  NOTIFICATIONS,
  ADMIN,
  DIAGNOSTIC
}

data class CelebrationState(
  val isVisible: Boolean = false,
  val streakDays: Int = 12,
  val goalMinutes: Int = 10,
  val badgeName: String? = null
)

class NoveliteViewModel(
  val repository: NoveliteRepository = NoveliteRepository()
) : ViewModel() {

  // Current Navigation Screen
  private val _currentScreen = MutableStateFlow(
    if (repository.isLoggedIn.value) Screen.HOME else Screen.AUTH
  )
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // Selected Story & Chapter for Detail & Reader
  private val _selectedStoryId = MutableStateFlow<String?>(null)
  val selectedStoryId: StateFlow<String?> = _selectedStoryId.asStateFlow()

  private val _selectedChapterId = MutableStateFlow<String?>(null)
  val selectedChapterId: StateFlow<String?> = _selectedChapterId.asStateFlow()

  // Selected author profile (for public profile view)
  private val _selectedAuthorId = MutableStateFlow<String?>(null)
  val selectedAuthorId: StateFlow<String?> = _selectedAuthorId.asStateFlow()

  // Celebration state
  private val _celebration = MutableStateFlow(CelebrationState())
  val celebration: StateFlow<CelebrationState> = _celebration.asStateFlow()

  // Explore search & filters
  val searchQuery = MutableStateFlow("")
  val selectedGenreFilter = MutableStateFlow("All")
  val isCompletedOnlyFilter = MutableStateFlow(false)
  val sortByFilter = MutableStateFlow("Trending") // "Trending", "Most Popular", "Recently Updated", "Most Liked"

  // Repository flows
  val currentUser: StateFlow<UserProfile> = repository.currentUser
  val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
  val isOnboarded: StateFlow<Boolean> = repository.isOnboarded
  val stories: StateFlow<List<Story>> = repository.stories
  val comments: StateFlow<List<Comment>> = repository.comments
  val notifications: StateFlow<List<NoveliteNotification>> = repository.notifications
  val authorStatuses: StateFlow<List<AuthorStatus>> = repository.authorStatuses
  val achievements: StateFlow<List<Achievement>> = repository.achievements
  val challenges = repository.challenges
  val customCollections: StateFlow<List<CustomCollection>> = repository.customCollections
  val todayMinutesRead: StateFlow<Int> = repository.todayMinutesRead
  val todayWordsWritten: StateFlow<Int> = repository.todayWordsWritten
  val readerConfig: StateFlow<ReaderConfig> = repository.readerConfig
  val reports: StateFlow<List<ReportItem>> = repository.reports

  // Library Search Query & Flow
  private val _librarySearchQuery = MutableStateFlow("")
  val librarySearchQuery: StateFlow<String> = _librarySearchQuery.asStateFlow()

  fun setLibrarySearchQuery(query: String) {
    _librarySearchQuery.value = query
  }

  // Recently Read stories (stories with progress > 0 ordered by last update)
  val recentlyReadStories: StateFlow<List<Story>> = stories
    .map { list ->
      list.filter { it.readingProgressPercent > 0f }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // Filtered stories in library by title
  val filteredLibraryStories: StateFlow<List<Story>> = combine(
    stories,
    _librarySearchQuery
  ) { storyList, query ->
    val inLibrary = storyList.filter { it.isInLibrary }
    if (query.isBlank()) {
      inLibrary
    } else {
      inLibrary.filter {
        it.title.contains(query, ignoreCase = true) ||
        it.authorName.contains(query, ignoreCase = true) ||
        it.genre.contains(query, ignoreCase = true)
      }
    }
  }
  .flowOn(Dispatchers.Default)
  .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Writing Studio active editing story
  private val _editingStoryId = MutableStateFlow<String?>(null)
  val editingStoryId: StateFlow<String?> = _editingStoryId.asStateFlow()

  private val _editingChapterId = MutableStateFlow<String?>(null)
  val editingChapterId: StateFlow<String?> = _editingChapterId.asStateFlow()

  init {
    viewModelScope.launch {
      repository.milestoneEvents.collect { event ->
        when (event) {
          is MilestoneEvent.GoalCompleted -> {
            _celebration.value = CelebrationState(
              isVisible = true,
              streakDays = event.streakDays,
              goalMinutes = event.minutes
            )
          }
          is MilestoneEvent.StreakMilestone -> {
            _celebration.value = CelebrationState(
              isVisible = true,
              streakDays = event.milestoneDays,
              goalMinutes = currentUser.value.readingGoalMinutes,
              badgeName = event.badgeName
            )
          }
          is MilestoneEvent.ShieldUsed -> {
            // Can show notification
          }
        }
      }
    }
  }

  fun navigateTo(screen: Screen) {
    NoveliteLogger.trackNavigationTiming("NavigateTo_$screen") {
      viewModelScope.launch(Dispatchers.IO) {
        val result = kotlinx.coroutines.withTimeoutOrNull(2000L) {
          try {
            true
          } catch (e: Exception) {
            android.util.Log.e("NoveliteNavVM", "Navigation error for $screen", e)
            false
          }
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
          if (result == true) {
            _currentScreen.value = screen
          } else {
            android.util.Log.w("NoveliteNavVM", "Navigation timeout or failure for $screen, defaulting to HOME")
            _currentScreen.value = Screen.HOME
          }
        }
      }
    }
  }

  fun openStory(storyId: String) {
    NoveliteLogger.trackNavigationTiming("OpenStory_$storyId") {
      viewModelScope.launch(Dispatchers.IO) {
        val result = kotlinx.coroutines.withTimeoutOrNull(2000L) {
          try {
            _selectedStoryId.value = storyId
            true
          } catch (e: Exception) {
            false
          }
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
          if (result == true) {
            _currentScreen.value = Screen.STORY_DETAIL
          } else {
            _currentScreen.value = Screen.HOME
          }
        }
      }
    }
  }

  fun openReader(storyId: String, chapterId: String? = null) {
    NoveliteLogger.trackNavigationTiming("OpenReader_$storyId") {
      viewModelScope.launch(Dispatchers.IO) {
        val result = kotlinx.coroutines.withTimeoutOrNull(2000L) {
          try {
            _selectedStoryId.value = storyId
            val story = stories.value.find { it.id == storyId }
            _selectedChapterId.value = chapterId ?: story?.chapters?.firstOrNull()?.id
            true
          } catch (e: Exception) {
            false
          }
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
          if (result == true) {
            _currentScreen.value = Screen.READER
          } else {
            _currentScreen.value = Screen.HOME
          }
        }
      }
    }
  }

  fun openWriterDraft(storyId: String? = null, chapterId: String? = null) {
    NoveliteLogger.trackNavigationTiming("OpenWriterDraft") {
      viewModelScope.launch(Dispatchers.IO) {
        val result = kotlinx.coroutines.withTimeoutOrNull(2000L) {
          try {
            _editingStoryId.value = storyId
            _editingChapterId.value = chapterId
            true
          } catch (e: Exception) {
            false
          }
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
          if (result == true) {
            _currentScreen.value = Screen.WRITER_DRAFT
          } else {
            _currentScreen.value = Screen.HOME
          }
        }
      }
    }
  }

  fun openStreakDashboard() {
    navigateTo(Screen.STREAK_DASHBOARD)
  }

  fun openAuthorProfile(authorId: String) {
    NoveliteLogger.trackNavigationTiming("OpenAuthorProfile_$authorId") {
      viewModelScope.launch(Dispatchers.IO) {
        val result = kotlinx.coroutines.withTimeoutOrNull(2000L) {
          try {
            _selectedAuthorId.value = authorId
            true
          } catch (e: Exception) {
            false
          }
        }
        kotlinx.coroutines.withContext(Dispatchers.Main) {
          if (result == true) {
            _currentScreen.value = Screen.PROFILE
          } else {
            _currentScreen.value = Screen.HOME
          }
        }
      }
    }
  }

  fun dismissCelebration() {
    _celebration.value = _celebration.value.copy(isVisible = false)
  }

  // Reading Timer Sim
  fun addReadingMinute(mins: Int = 1) {
    repository.addReadingTime(mins)
  }

  fun useShield(): Boolean {
    return repository.useStreakShield()
  }

  fun toggleLike(storyId: String) {
    repository.toggleLikeStory(storyId)
  }

  fun toggleLikeChapter(storyId: String, chapterId: String) {
    repository.toggleLikeChapter(storyId, chapterId)
  }

  fun toggleLibrary(storyId: String, tab: LibraryTab = LibraryTab.CURRENTLY_READING) {
    repository.toggleLibraryStory(storyId, tab)
  }

  fun followAuthor(authorId: String) {
    repository.toggleFollowAuthor(authorId)
  }

  fun postComment(storyId: String, chapterId: String, text: String, quote: String? = null) {
    repository.addComment(storyId, chapterId, text, quote)
  }

  fun replyComment(commentId: String, text: String) {
    repository.replyToComment(commentId, text)
  }

  fun likeComment(commentId: String) {
    repository.toggleLikeComment(commentId)
  }

  fun deleteComment(commentId: String) {
    repository.deleteComment(commentId)
  }

  // Writing Studio
  fun startCreatingStory() {
    _editingStoryId.value = null
    _editingChapterId.value = null
  }

  fun selectStoryForEditing(storyId: String) {
    _editingStoryId.value = storyId
  }

  fun selectChapterForEditing(chapterId: String) {
    _editingChapterId.value = chapterId
  }

  fun createStory(
    title: String,
    desc: String,
    genre: String,
    tags: List<String>,
    status: StoryStatus,
    coverImageUrl: String? = null,
    teaserVideoUrl: String? = null,
    teaserVideoDurationSec: Int? = null
  ): Story {
    val story = repository.createStory(
      title = title,
      description = desc,
      genre = genre,
      tags = tags,
      status = status,
      coverImageUrl = coverImageUrl,
      teaserVideoUrl = teaserVideoUrl,
      teaserVideoDurationSec = teaserVideoDurationSec
    )
    _editingStoryId.value = story.id
    return story
  }

  fun updateStoryMedia(
    storyId: String,
    coverImageUrl: String?,
    teaserVideoUrl: String?,
    teaserVideoDurationSec: Int? = null
  ) {
    repository.updateStoryMedia(storyId, coverImageUrl, teaserVideoUrl, teaserVideoDurationSec)
  }

  fun updateStoryDetails(
    storyId: String,
    title: String,
    description: String,
    genre: String,
    tags: List<String>,
    status: StoryStatus,
    coverImageUrl: String?,
    teaserVideoUrl: String?,
    teaserVideoDurationSec: Int? = null
  ) {
    repository.updateStoryDetails(
      storyId = storyId,
      title = title,
      description = description,
      genre = genre,
      tags = tags,
      status = status,
      coverImageUrl = coverImageUrl,
      teaserVideoUrl = teaserVideoUrl,
      teaserVideoDurationSec = teaserVideoDurationSec
    )
  }

  fun addChapter(storyId: String, title: String, content: String, isDraft: Boolean): Chapter {
    val ch = repository.addChapterToStory(storyId, title, content, isDraft)
    _editingChapterId.value = ch.id
    return ch
  }

  fun updateChapter(storyId: String, chapterId: String, title: String, content: String, isDraft: Boolean) {
    repository.updateChapter(storyId, chapterId, title, content, isDraft)
  }

  fun updateStoryStatus(storyId: String, status: StoryStatus) {
    repository.updateStoryStatus(storyId, status)
  }

  // Custom Collections
  fun createCustomCollection(name: String, icon: String) {
    repository.createCollection(name, icon)
  }

  fun addStoryToCustomCollection(colId: String, storyId: String) {
    repository.addStoryToCollection(colId, storyId)
  }

  fun removeStoryFromCustomCollection(colId: String, storyId: String) {
    repository.removeStoryFromCollection(colId, storyId)
  }

  // Reader Settings
  fun updateReaderConfig(fontSize: Int? = null, fontFamily: String? = null, lineSpacing: Float? = null, themeMode: String? = null) {
    repository.updateReaderConfig(fontSize, fontFamily, lineSpacing, themeMode)
  }

  // Auth & Onboarding
  private val reservedUsernames = setOf("aurora_reads", "storyteller_alex", "novel_wizard", "admin")

  fun validatePasswordStrength(password: String): String? {
    if (password.length < 6) {
      return "Password must be at least 6 characters long."
    }
    if (!password.any { it.isDigit() } && !password.any { !it.isLetterOrDigit() }) {
      return "Password must contain at least one number or special character."
    }
    return null
  }

  fun validateUsernameUnique(username: String): Boolean {
    return !reservedUsernames.contains(username.trim().lowercase())
  }

  fun login(user: String, pass: String) {
    repository.login(user, pass)
    _currentScreen.value = Screen.HOME
    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("NoveliteViewModel: Logging in user $user")
      try {
        com.google.firebase.auth.FirebaseAuth.getInstance().signInWithEmailAndPassword(
          if (user.contains("@")) user else "$user@novelite.app",
          pass
        )
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth signIn notice: ${e.message}")
      }
    }
  }

  fun signup(user: String, email: String, pass: String) {
    repository.signup(user, email, pass)
    _currentScreen.value = Screen.ONBOARDING
    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("NoveliteViewModel: Registering user $user")
      try {
        com.google.firebase.auth.FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth createUser notice: ${e.message}")
      }
    }
  }

  fun completeOnboarding(role: UserRole, genres: List<String>, goalMinutes: Int) {
    repository.completeOnboarding(role, genres, goalMinutes)
    _currentScreen.value = Screen.HOME
  }

  fun logout() {
    repository.logout()
    _currentScreen.value = Screen.AUTH
    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("Signing out user from NoveliteViewModel...")
      try {
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
        NoveliteLogger.logRecomposition("FirebaseAuth signOut complete")
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth signOut notice: ${e.message}")
      }
    }
  }

  fun signOut() {
    logout()
  }

  fun updateProfile(displayName: String, bio: String, isPublic: Boolean, goalMins: Int) {
    repository.updateProfile(displayName, bio, isPublic, goalMins)
  }

  fun submitReport(targetType: String, title: String, reason: String, details: String) {
    repository.submitReport(targetType, title, reason, details)
  }

  fun resolveReport(id: String, action: String) {
    repository.resolveReport(id, action)
  }

  fun postAuthorStatus(
    statusText: String,
    storyId: String? = null,
    storyTitle: String? = null,
    mediaUrl: String? = null,
    mediaType: String? = null
  ) {
    repository.postAuthorStatus(statusText, storyId, storyTitle, mediaUrl, mediaType)
  }

  fun likeAuthorStatus(statusId: String) {
    repository.toggleLikeAuthorStatus(statusId)
  }

  fun deleteAuthorStatus(statusId: String) {
    repository.deleteAuthorStatus(statusId)
  }

  fun markAllNotifsRead() {
    repository.markAllNotificationsAsRead()
  }

  fun markNotificationRead(notifId: String) {
    repository.markNotificationAsRead(notifId)
  }

  fun deleteNotification(notifId: String) {
    repository.deleteNotification(notifId)
  }
}
