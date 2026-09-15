package com.example.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

sealed class MilestoneEvent {
  data class GoalCompleted(val streakDays: Int, val minutes: Int) : MilestoneEvent()
  data class StreakMilestone(val milestoneDays: Int, val badgeName: String) : MilestoneEvent()
  data class ShieldUsed(val streakDays: Int) : MilestoneEvent()
}

class NoveliteRepository {

  // Current Logged In User State
  private val _currentUser = MutableStateFlow(
    UserProfile(
      id = "user_me",
      username = "aurora_reads",
      displayName = "Aurora Sterling",
      email = "aurora@novelite.app",
      bio = "Book addict, night owl reader, and aspiring fantasy novelist. Keeping the flame alive! 🔥📖",
      joinedDate = "Jan 2025",
      followersCount = 420,
      followingCount = 18,
      readingStreak = 12,
      writingStreak = 5,
      longestStreak = 28,
      streakShields = 2,
      readingGoalMinutes = 10,
      role = UserRole.BOTH,
      favoriteGenres = listOf("African Stories", "Fantasy", "Romance", "Mystery", "Thriller"),
      isActivityPublic = true
    )
  )
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(
    try {
      com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null
    } catch (e: Throwable) {
      false
    }
  )
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  private val _isOnboarded = MutableStateFlow(true)
  val isOnboarded: StateFlow<Boolean> = _isOnboarded.asStateFlow()

  // Stories
  private val _stories = MutableStateFlow(SampleData.getInitialStories())
  val stories: StateFlow<List<Story>> = _stories.asStateFlow()

  // Comments
  private val _comments = MutableStateFlow(SampleData.getInitialComments())
  val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

  // Notifications
  private val _notifications = MutableStateFlow(SampleData.getInitialNotifications())
  val notifications: StateFlow<List<NoveliteNotification>> = _notifications.asStateFlow()

  // Author Statuses
  private val _authorStatuses = MutableStateFlow(SampleData.getInitialAuthorStatuses())
  val authorStatuses: StateFlow<List<AuthorStatus>> = _authorStatuses.asStateFlow()

  // Achievements
  private val _achievements = MutableStateFlow(SampleData.getInitialAchievements())
  val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

  // Challenges
  private val _challenges = MutableStateFlow(SampleData.getInitialChallenges())
  val challenges: StateFlow<List<Challenge>> = _challenges.asStateFlow()

  // Custom Collections
  private val _customCollections = MutableStateFlow(
    listOf(
      CustomCollection(id = "col_1", name = "Stories That Made Me Cry 😭", icon = "😭", storyIds = listOf("story_4", "story_9")),
      CustomCollection(id = "col_2", name = "My Favorite Romance Stories ❤️", icon = "❤️", storyIds = listOf("story_1", "story_4")),
      CustomCollection(id = "col_3", name = "Weekend Reads 📚", icon = "📚", storyIds = listOf("story_2", "story_3"))
    )
  )
  val customCollections: StateFlow<List<CustomCollection>> = _customCollections.asStateFlow()

  // Daily Reading Progress State
  private val _todayMinutesRead = MutableStateFlow(8) // initial 8/10
  val todayMinutesRead: StateFlow<Int> = _todayMinutesRead.asStateFlow()

  private val _todayWordsWritten = MutableStateFlow(420)
  val todayWordsWritten: StateFlow<Int> = _todayWordsWritten.asStateFlow()

  // Milestone / Celebration Event Channel
  private val _milestoneEvents = MutableSharedFlow<MilestoneEvent>(extraBufferCapacity = 5)
  val milestoneEvents: SharedFlow<MilestoneEvent> = _milestoneEvents.asSharedFlow()

  // Reader Settings
  private val _readerConfig = MutableStateFlow(ReaderConfig())
  val readerConfig: StateFlow<ReaderConfig> = _readerConfig.asStateFlow()

  // Reports
  private val _reports = MutableStateFlow(
    listOf(
      ReportItem(
        id = "rep_101",
        targetType = "Comment",
        targetTitle = "Suspicious spam link in chapter 1",
        reporterUsername = "Kwame_Bookworm",
        reason = "Spam or advertising",
        details = "User posted telegram spam link.",
        timestamp = "3 hours ago",
        status = "Pending"
      )
    )
  )
  val reports: StateFlow<List<ReportItem>> = _reports.asStateFlow()

  // 30-Day Streak Calendar generator
  fun getStreakCalendarDays(): List<StreakHistoryDay> {
    val list = mutableListOf<StreakHistoryDay>()
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
    for (i in 1..30) {
      val dayStatus = when {
        i == 26 -> DayStatus.TODAY
        i in 14..25 -> DayStatus.COMPLETED
        i == 13 -> DayStatus.PROTECTED
        i in 3..12 -> DayStatus.COMPLETED
        i in 1..2 -> DayStatus.MISSED
        else -> DayStatus.FUTURE
      }
      list.add(
        StreakHistoryDay(
          dateIso = "2026-08-${i.toString().padStart(2, '0')}",
          dayLabel = daysOfWeek[(i - 1) % 7],
          dayNumber = i,
          status = dayStatus
        )
      )
    }
    return list
  }

  // Auth Operations
  fun login(usernameOrEmail: String, pass: String): Boolean {
    val user = _currentUser.value
    _currentUser.value = user.copy(
      username = if (usernameOrEmail.contains("@")) usernameOrEmail.substringBefore("@") else usernameOrEmail,
      email = if (usernameOrEmail.contains("@")) usernameOrEmail else "${usernameOrEmail}@novelite.app",
      displayName = if (usernameOrEmail.contains("@")) usernameOrEmail.substringBefore("@").replaceFirstChar { it.uppercase() } else usernameOrEmail.replaceFirstChar { it.uppercase() }
    )
    _isLoggedIn.value = true
    return true
  }

  fun signup(username: String, email: String, pass: String): Boolean {
    _currentUser.value = UserProfile(
      id = "user_${UUID.randomUUID().toString().take(6)}",
      username = username,
      displayName = username.replaceFirstChar { it.uppercase() },
      email = email,
      bio = "New storyteller in the Novelite community! 🔥📖",
      readingStreak = 1,
      writingStreak = 0,
      longestStreak = 1,
      streakShields = 1,
      readingGoalMinutes = 10,
      role = UserRole.BOTH,
      favoriteGenres = emptyList()
    )
    _isLoggedIn.value = true
    _isOnboarded.value = false
    return true
  }

  fun completeOnboarding(role: UserRole, genres: List<String>, dailyGoalMinutes: Int) {
    _currentUser.update {
      it.copy(
        role = role,
        favoriteGenres = if (genres.isNotEmpty()) genres else listOf("Fantasy", "Romance"),
        readingGoalMinutes = dailyGoalMinutes
      )
    }
    _isOnboarded.value = true
  }

  fun logout() {
    _isLoggedIn.value = false
  }

  fun updateProfile(displayName: String, bio: String, isActivityPublic: Boolean, readingGoalMinutes: Int) {
    _currentUser.update {
      it.copy(
        displayName = displayName,
        bio = bio,
        isActivityPublic = isActivityPublic,
        readingGoalMinutes = readingGoalMinutes
      )
    }
  }

  // Reading & Streak Progress Tracking
  fun addReadingTime(minutes: Int) {
    val current = _todayMinutesRead.value
    val target = _currentUser.value.readingGoalMinutes
    val updated = current + minutes
    _todayMinutesRead.value = updated

    if (current < target && updated >= target) {
      // Completed daily goal!
      val newStreak = _currentUser.value.readingStreak + 1
      val newLongest = maxOf(_currentUser.value.longestStreak, newStreak)
      _currentUser.update {
        it.copy(
          readingStreak = newStreak,
          longestStreak = newLongest
        )
      }
      _milestoneEvents.tryEmit(MilestoneEvent.GoalCompleted(newStreak, target))

      // Check milestones
      if (newStreak in listOf(3, 7, 14, 30, 50, 100, 365)) {
        val badge = when (newStreak) {
          3 -> "🔥 3-Day Spark"
          7 -> "🔥 7-Day Flame"
          14 -> "🔥 14-Day Blaze"
          30 -> "🔥 30-Day Inferno"
          50 -> "🔥 50-Day Beacon"
          100 -> "🔥 100-Day Supernova"
          else -> "🔥 365-Day Legend"
        }
        _milestoneEvents.tryEmit(MilestoneEvent.StreakMilestone(newStreak, badge))
      }
    }
  }

  fun addWritingWords(words: Int) {
    _todayWordsWritten.update { it + words }
    if (_todayWordsWritten.value >= 100) {
      val newStreak = _currentUser.value.writingStreak + 1
      _currentUser.update { it.copy(writingStreak = newStreak) }
    }
  }

  fun useStreakShield(): Boolean {
    if (_currentUser.value.streakShields > 0) {
      _currentUser.update {
        it.copy(streakShields = it.streakShields - 1)
      }
      _milestoneEvents.tryEmit(MilestoneEvent.ShieldUsed(_currentUser.value.readingStreak))
      return true
    }
    return false
  }

  // Story Interactions
  fun toggleLikeStory(storyId: String) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val newLiked = !story.isLiked
          story.copy(
            isLiked = newLiked,
            likesCount = if (newLiked) story.likesCount + 1 else maxOf(0, story.likesCount - 1)
          )
        } else story
      }
    }
  }

  fun toggleLikeChapter(storyId: String, chapterId: String) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val updatedChapters = story.chapters.map { chapter ->
            if (chapter.id == chapterId) {
              val newLiked = !chapter.isLiked
              chapter.copy(
                isLiked = newLiked,
                likesCount = if (newLiked) chapter.likesCount + 1 else maxOf(0, chapter.likesCount - 1)
              )
            } else chapter
          }
          story.copy(chapters = updatedChapters)
        } else story
      }
    }
  }

  fun toggleLibraryStory(storyId: String, tab: LibraryTab = LibraryTab.CURRENTLY_READING) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val inLib = !story.isInLibrary
          story.copy(
            isInLibrary = inLib,
            libraryCategory = if (inLib) tab else null
          )
        } else story
      }
    }
  }

  fun updateReadingProgress(storyId: String, chapterId: String, progressFraction: Float) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          story.copy(
            readingProgressPercent = progressFraction,
            lastReadChapterId = chapterId,
            isInLibrary = true,
            libraryCategory = if (progressFraction >= 0.98f) LibraryTab.COMPLETED else LibraryTab.CURRENTLY_READING
          )
        } else story
      }
    }
  }

  fun toggleFollowAuthor(authorId: String) {
    _currentUser.update { user ->
      val set = user.followedAuthorIds.toMutableSet()
      if (set.contains(authorId)) {
        set.remove(authorId)
      } else {
        set.add(authorId)
      }
      user.copy(followedAuthorIds = set)
    }
  }

  // Comments
  fun addComment(storyId: String, chapterId: String, text: String, inlineQuote: String? = null) {
    val newComm = Comment(
      id = "comm_${UUID.randomUUID().toString().take(6)}",
      storyId = storyId,
      chapterId = chapterId,
      userId = _currentUser.value.id,
      userName = _currentUser.value.displayName,
      text = text,
      timestamp = "Just now",
      likes = 0,
      inlineQuote = inlineQuote
    )
    _comments.update { listOf(newComm) + it }

    // Update story comments count
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) story.copy(commentsCount = story.commentsCount + 1) else story
      }
    }
  }

  fun replyToComment(commentId: String, text: String) {
    val reply = CommentReply(
      id = "rep_${UUID.randomUUID().toString().take(6)}",
      commentId = commentId,
      userId = _currentUser.value.id,
      userName = _currentUser.value.displayName,
      text = text,
      timestamp = "Just now"
    )
    _comments.update { list ->
      list.map { comment ->
        if (comment.id == commentId) {
          comment.copy(replies = comment.replies + reply)
        } else comment
      }
    }
  }

  fun toggleLikeComment(commentId: String) {
    _comments.update { list ->
      list.map { comment ->
        if (comment.id == commentId) {
          val newLiked = !comment.isLiked
          comment.copy(
            isLiked = newLiked,
            likes = if (newLiked) comment.likes + 1 else maxOf(0, comment.likes - 1)
          )
        } else comment
      }
    }
  }

  fun deleteComment(commentId: String) {
    _comments.update { it.filterNot { c -> c.id == commentId } }
  }

  // Writing Studio
  fun createStory(
    title: String,
    description: String,
    genre: String,
    tags: List<String>,
    status: StoryStatus = StoryStatus.DRAFT,
    coverImageUrl: String? = null,
    teaserVideoUrl: String? = null,
    teaserVideoDurationSec: Int? = null
  ): Story {
    val newStory = Story(
      id = "story_${UUID.randomUUID().toString().take(6)}",
      title = title,
      authorId = _currentUser.value.id,
      authorName = _currentUser.value.displayName,
      authorUsername = _currentUser.value.username,
      description = description,
      coverDrawableRes = null,
      coverImageUrl = coverImageUrl,
      teaserVideoUrl = teaserVideoUrl,
      teaserVideoDurationSec = teaserVideoDurationSec,
      coverColorHex = 0xFF4A2C2A,
      genre = genre,
      tags = tags,
      status = status,
      readsCount = 0,
      likesCount = 0,
      commentsCount = 0,
      chaptersCount = 0,
      publishedDate = "Just now",
      lastUpdated = "Just now",
      chapters = emptyList()
    )
    _stories.update { listOf(newStory) + it }
    addWritingWords(50)
    return newStory
  }

  fun updateStoryMedia(
    storyId: String,
    coverImageUrl: String?,
    teaserVideoUrl: String?,
    teaserVideoDurationSec: Int? = null
  ) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          story.copy(
            coverImageUrl = coverImageUrl,
            teaserVideoUrl = teaserVideoUrl,
            teaserVideoDurationSec = teaserVideoDurationSec,
            lastUpdated = "Just now"
          )
        } else story
      }
    }
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
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          story.copy(
            title = title,
            description = description,
            genre = genre,
            tags = tags,
            status = status,
            coverImageUrl = coverImageUrl,
            teaserVideoUrl = teaserVideoUrl,
            teaserVideoDurationSec = teaserVideoDurationSec,
            lastUpdated = "Just now"
          )
        } else story
      }
    }
  }

  fun updateStoryStatus(storyId: String, newStatus: StoryStatus) {
    _stories.update { list ->
      list.map { if (it.id == storyId) it.copy(status = newStatus) else it }
    }
  }

  fun addChapterToStory(
    storyId: String,
    title: String,
    content: String,
    isDraft: Boolean
  ): Chapter {
    val wordCount = content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    val chapter = Chapter(
      id = "ch_${UUID.randomUUID().toString().take(6)}",
      storyId = storyId,
      chapterNumber = (_stories.value.find { it.id == storyId }?.chapters?.size ?: 0) + 1,
      title = title,
      content = content,
      wordsCount = wordCount,
      publishedDate = "Just now",
      isDraft = isDraft
    )

    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val updatedChapters = story.chapters + chapter
          story.copy(
            chapters = updatedChapters,
            chaptersCount = updatedChapters.size,
            lastUpdated = "Just now"
          )
        } else story
      }
    }
    addWritingWords(wordCount)
    return chapter
  }

  fun updateChapter(
    storyId: String,
    chapterId: String,
    title: String,
    content: String,
    isDraft: Boolean
  ) {
    val wordCount = content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val updatedChapters = story.chapters.map { ch ->
            if (ch.id == chapterId) {
              ch.copy(title = title, content = content, wordsCount = wordCount, isDraft = isDraft)
            } else ch
          }
          story.copy(chapters = updatedChapters, lastUpdated = "Just now")
        } else story
      }
    }
  }

  fun deleteChapter(storyId: String, chapterId: String) {
    _stories.update { list ->
      list.map { story ->
        if (story.id == storyId) {
          val updatedChapters = story.chapters.filterNot { it.id == chapterId }
          story.copy(chapters = updatedChapters, chaptersCount = updatedChapters.size)
        } else story
      }
    }
  }

  // Custom Collections
  fun createCollection(name: String, icon: String = "📚") {
    val col = CustomCollection(
      id = "col_${UUID.randomUUID().toString().take(6)}",
      name = name,
      icon = icon,
      storyIds = emptyList()
    )
    _customCollections.update { it + col }
  }

  fun addStoryToCollection(collectionId: String, storyId: String) {
    _customCollections.update { list ->
      list.map { col ->
        if (col.id == collectionId && !col.storyIds.contains(storyId)) {
          col.copy(storyIds = col.storyIds + storyId)
        } else col
      }
    }
  }

  fun removeStoryFromCollection(collectionId: String, storyId: String) {
    _customCollections.update { list ->
      list.map { col ->
        if (col.id == collectionId) {
          col.copy(storyIds = col.storyIds - storyId)
        } else col
      }
    }
  }

  // Reader Configuration
  fun updateReaderConfig(fontSize: Int? = null, fontFamily: String? = null, lineSpacing: Float? = null, themeMode: String? = null) {
    _readerConfig.update {
      it.copy(
        fontSizeSp = fontSize ?: it.fontSizeSp,
        fontFamilyName = fontFamily ?: it.fontFamilyName,
        lineSpacingMultiplier = lineSpacing ?: it.lineSpacingMultiplier,
        themeMode = themeMode ?: it.themeMode
      )
    }
  }

  // Moderation Reports
  fun submitReport(targetType: String, targetTitle: String, reason: String, details: String) {
    val rep = ReportItem(
      id = "rep_${UUID.randomUUID().toString().take(6)}",
      targetType = targetType,
      targetTitle = targetTitle,
      reporterUsername = _currentUser.value.username,
      reason = reason,
      details = details,
      timestamp = "Just now",
      status = "Pending"
    )
    _reports.update { listOf(rep) + it }
  }

  fun resolveReport(reportId: String, actionTaken: String) {
    _reports.update { list ->
      list.map { if (it.id == reportId) it.copy(status = "Resolved: $actionTaken") else it }
    }
  }

  // Author Statuses Operations
  fun postAuthorStatus(
    statusText: String,
    storyId: String? = null,
    storyTitle: String? = null,
    mediaUrl: String? = null,
    mediaType: String? = null
  ) {
    val newStatus = AuthorStatus(
      id = "status_${UUID.randomUUID().toString().take(6)}",
      authorId = _currentUser.value.id,
      authorName = _currentUser.value.displayName,
      authorUsername = _currentUser.value.username,
      statusText = statusText,
      storyId = storyId,
      storyTitle = storyTitle,
      mediaUrl = mediaUrl,
      mediaType = mediaType,
      timestamp = "Just now",
      likesCount = 0,
      isLiked = false
    )
    _authorStatuses.update { listOf(newStatus) + it }
    addWritingWords(30)
    addNotification(
      title = "Status Published! ✍️",
      message = "Your readers have been notified of your new update: \"${statusText.take(40)}...\"",
      type = NotificationType.AUTHOR_STATUS,
      targetStoryId = storyId
    )
  }

  fun toggleLikeAuthorStatus(statusId: String) {
    _authorStatuses.update { list ->
      list.map { status ->
        if (status.id == statusId) {
          val newLiked = !status.isLiked
          status.copy(
            isLiked = newLiked,
            likesCount = if (newLiked) status.likesCount + 1 else maxOf(0, status.likesCount - 1)
          )
        } else status
      }
    }
  }

  fun deleteAuthorStatus(statusId: String) {
    _authorStatuses.update { it.filterNot { s -> s.id == statusId } }
  }

  // Notification Operations
  fun addNotification(title: String, message: String, type: NotificationType, targetStoryId: String? = null) {
    val notif = NoveliteNotification(
      id = "notif_${UUID.randomUUID().toString().take(6)}",
      title = title,
      message = message,
      timeAgo = "Just now",
      type = type,
      isRead = false,
      actionTargetStoryId = targetStoryId
    )
    _notifications.update { listOf(notif) + it }
  }

  fun deleteNotification(notifId: String) {
    _notifications.update { it.filterNot { n -> n.id == notifId } }
  }

  fun markNotificationAsRead(notifId: String) {
    _notifications.update { list ->
      list.map { if (it.id == notifId) it.copy(isRead = true) else it }
    }
  }

  fun markAllNotificationsAsRead() {
    _notifications.update { list -> list.map { it.copy(isRead = true) } }
  }
}
