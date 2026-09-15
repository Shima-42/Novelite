package com.example.data

enum class UserRole {
  READER,
  WRITER,
  BOTH
}

enum class StoryStatus {
  DRAFT,
  ONGOING,
  COMPLETED,
  HIATUS
}

enum class DayStatus {
  COMPLETED,
  PROTECTED,
  MISSED,
  TODAY,
  FUTURE
}

enum class AchievementCategory {
  READING,
  WRITING,
  COMMUNITY
}

enum class NotificationType {
  STREAK_REMINDER,
  NEW_CHAPTER,
  LIKE,
  COMMENT,
  MILESTONE,
  SHIELD,
  FOLLOWER,
  AUTHOR_STATUS
}

enum class LibraryTab(val officialName: String, val subtitle: String) {
  WHERE_I_WANDER_NOW("Where I Wander Now", "The stories I'm lost in right now."),
  TREASURED_BETWEEN_THE_LINES("Treasured Between the Lines", "Stories that found a special place in my heart."),
  PAGES_YET_TO_BE_TURNED("Pages Yet to Be Turned", "Stories waiting for me to begin their journey."),
  STORIES_THAT_STAYED("Stories That Stayed", "The stories I finished, but never truly left behind."),
  SHELVES_OF_MY_MAKING("Shelves of My Making", "Stories gathered into worlds of my own.");

  // Backwards compatibility helpers
  companion object {
    val CURRENTLY_READING = WHERE_I_WANDER_NOW
    val FAVORITES = TREASURED_BETWEEN_THE_LINES
    val WANT_TO_READ = PAGES_YET_TO_BE_TURNED
    val COMPLETED = STORIES_THAT_STAYED
    val COLLECTIONS = SHELVES_OF_MY_MAKING
  }
}

data class UserProfile(
  val id: String,
  val username: String,
  val displayName: String,
  val email: String,
  val bio: String,
  val avatarUrl: String = "",
  val joinedDate: String = "Jan 2025",
  val followersCount: Int = 0,
  val followingCount: Int = 0,
  val readingStreak: Int = 12,
  val writingStreak: Int = 5,
  val longestStreak: Int = 28,
  val streakShields: Int = 2,
  val readingGoalMinutes: Int = 10,
  val role: UserRole = UserRole.BOTH,
  val isWriter: Boolean = true,
  val favoriteGenres: List<String> = listOf("Romance", "Fantasy", "African Stories", "Mystery"),
  val isActivityPublic: Boolean = true,
  val followedAuthorIds: Set<String> = emptySet()
)

data class Chapter(
  val id: String,
  val storyId: String,
  val chapterNumber: Int,
  val title: String,
  val content: String,
  val wordsCount: Int,
  val readsCount: Int = 0,
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val publishedDate: String = "Just now",
  val isDraft: Boolean = false,
  val isLiked: Boolean = false
)

data class Story(
  val id: String,
  val title: String,
  val authorId: String,
  val authorName: String,
  val authorUsername: String,
  val authorAvatar: String = "",
  val description: String,
  val coverDrawableRes: Int? = null,
  val coverImageUrl: String? = null,
  val teaserVideoUrl: String? = null,
  val teaserVideoDurationSec: Int? = null,
  val coverColorHex: Long = 0xFF311B92,
  val genre: String,
  val tags: List<String> = emptyList(),
  val status: StoryStatus = StoryStatus.ONGOING,
  val readsCount: Int = 1240,
  val likesCount: Int = 340,
  val commentsCount: Int = 89,
  val chaptersCount: Int = 1,
  val publishedDate: String = "Jan 2025",
  val lastUpdated: String = "2 hours ago",
  val chapters: List<Chapter> = emptyList(),
  val isLiked: Boolean = false,
  val isInLibrary: Boolean = false,
  val libraryCategory: LibraryTab? = null,
  val readingProgressPercent: Float = 0f,
  val lastReadChapterId: String? = null
)

data class CommentReply(
  val id: String,
  val commentId: String,
  val userId: String,
  val userName: String,
  val userAvatar: String = "",
  val text: String,
  val timestamp: String = "Just now"
)

data class Comment(
  val id: String,
  val storyId: String,
  val chapterId: String,
  val userId: String,
  val userName: String,
  val userAvatar: String = "",
  val text: String,
  val timestamp: String = "Just now",
  val likes: Int = 0,
  val isLiked: Boolean = false,
  val replies: List<CommentReply> = emptyList(),
  val inlineQuote: String? = null
)

data class StreakHistoryDay(
  val dateIso: String,
  val dayLabel: String,
  val dayNumber: Int,
  val status: DayStatus
)

data class Achievement(
  val id: String,
  val title: String,
  val description: String,
  val iconEmoji: String = "🏆",
  val icon: String = "🏆",
  val category: AchievementCategory = AchievementCategory.READING,
  val currentProgress: Int = 1,
  val maxProgress: Int = 1,
  val isUnlocked: Boolean = false,
  val unlockedDate: String? = null
)

data class Challenge(
  val id: String,
  val title: String,
  val description: String,
  val rewardBadge: String = "🔥 Flame Badge",
  val reward: String = "🔥 Flame Badge",
  val icon: String = "🎯",
  val targetValue: Int = 7,
  val currentProgress: Int = 3,
  val isCompleted: Boolean = false,
  val daysRemaining: Int = 5
)

data class NoveliteNotification(
  val id: String,
  val title: String,
  val message: String,
  val timestamp: String = "Just now",
  val timeAgo: String = "Just now",
  val type: NotificationType = NotificationType.STREAK_REMINDER,
  val isRead: Boolean = false,
  val actionTargetStoryId: String? = null
)

data class CustomCollection(
  val id: String,
  val name: String,
  val icon: String = "📚",
  val storyIds: List<String> = emptyList()
)

data class ReportItem(
  val id: String,
  val targetType: String,
  val targetTitle: String,
  val reporterUsername: String,
  val reason: String,
  val details: String,
  val timestamp: String,
  val status: String = "Pending"
)

data class AuthorStatus(
  val id: String,
  val authorId: String,
  val authorName: String,
  val authorUsername: String,
  val authorAvatar: String = "",
  val statusText: String,
  val storyId: String? = null,
  val storyTitle: String? = null,
  val mediaUrl: String? = null,
  val mediaType: String? = null, // "IMAGE" or "VIDEO"
  val timestamp: String = "Just now",
  val likesCount: Int = 0,
  val isLiked: Boolean = false
)

data class ReaderConfig(
  val fontSizeSp: Int = 18,
  val fontFamilyName: String = "Serif",
  val lineSpacingMultiplier: Float = 1.5f,
  val themeMode: String = "Twilight" // "Light", "Sepia", "Dark", "Twilight"
)

