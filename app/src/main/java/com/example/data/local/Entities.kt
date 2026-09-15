package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.Chapter
import com.example.data.LibraryTab
import com.example.data.Story
import com.example.data.StoryStatus

/**
 * Room Entity to track daily reading and writing streaks, consecutive days,
 * and daily progress metadata for users.
 */
@Entity(tableName = "streak_metadata")
data class StreakMetadataEntity(
  @PrimaryKey val userId: String = "user_me",
  val consecutiveDays: Int = 12,
  val longestStreak: Int = 28,
  val lastActivityDate: String = "2026-08-26",
  val todayMinutesRead: Int = 8,
  val dailyGoalMinutes: Int = 10,
  val streakShields: Int = 2,
  val totalWordsWritten: Int = 420,
  val writingStreak: Int = 5,
  val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Room Entity for storing user stories.
 */
@Entity(tableName = "stories")
data class StoryEntity(
  @PrimaryKey val id: String,
  val title: String,
  val authorId: String,
  val authorName: String,
  val authorUsername: String,
  val authorAvatar: String = "",
  val description: String,
  val coverImageUrl: String? = null,
  val teaserVideoUrl: String? = null,
  val teaserVideoDurationSec: Int? = null,
  val coverColorHex: Long = 0xFF4A2C2A,
  val genre: String,
  val tagsCsv: String = "",
  val status: String = "ONGOING",
  val readsCount: Int = 0,
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val chaptersCount: Int = 0,
  val publishedDate: String = "Just now",
  val lastUpdated: String = "Just now",
  val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
  val isLiked: Boolean = false,
  val isInLibrary: Boolean = false,
  val libraryCategoryName: String? = null,
  val readingProgressPercent: Float = 0f,
  val lastReadChapterId: String? = null
) {
  fun toDomain(chapters: List<Chapter> = emptyList()): Story {
    return Story(
      id = id,
      title = title,
      authorId = authorId,
      authorName = authorName,
      authorUsername = authorUsername,
      authorAvatar = authorAvatar,
      description = description,
      coverDrawableRes = null,
      coverImageUrl = coverImageUrl,
      teaserVideoUrl = teaserVideoUrl,
      teaserVideoDurationSec = teaserVideoDurationSec,
      coverColorHex = coverColorHex,
      genre = genre,
      tags = if (tagsCsv.isBlank()) emptyList() else tagsCsv.split(",").map { it.trim() },
      status = try { StoryStatus.valueOf(status) } catch (e: Exception) { StoryStatus.ONGOING },
      readsCount = readsCount,
      likesCount = likesCount,
      commentsCount = commentsCount,
      chaptersCount = maxOf(chaptersCount, chapters.size),
      publishedDate = publishedDate,
      lastUpdated = lastUpdated,
      chapters = chapters,
      isLiked = isLiked,
      isInLibrary = isInLibrary,
      libraryCategory = libraryCategoryName?.let {
        try { LibraryTab.valueOf(it) } catch (e: Exception) { null }
      },
      readingProgressPercent = readingProgressPercent,
      lastReadChapterId = lastReadChapterId
    )
  }

  companion object {
    fun fromDomain(story: Story): StoryEntity {
      return StoryEntity(
        id = story.id,
        title = story.title,
        authorId = story.authorId,
        authorName = story.authorName,
        authorUsername = story.authorUsername,
        authorAvatar = story.authorAvatar,
        description = story.description,
        coverImageUrl = story.coverImageUrl,
        teaserVideoUrl = story.teaserVideoUrl,
        teaserVideoDurationSec = story.teaserVideoDurationSec,
        coverColorHex = story.coverColorHex,
        genre = story.genre,
        tagsCsv = story.tags.joinToString(","),
        status = story.status.name,
        readsCount = story.readsCount,
        likesCount = story.likesCount,
        commentsCount = story.commentsCount,
        chaptersCount = maxOf(story.chaptersCount, story.chapters.size),
        publishedDate = story.publishedDate,
        lastUpdated = story.lastUpdated,
        lastUpdatedTimestamp = System.currentTimeMillis(),
        isLiked = story.isLiked,
        isInLibrary = story.isInLibrary,
        libraryCategoryName = story.libraryCategory?.name,
        readingProgressPercent = story.readingProgressPercent,
        lastReadChapterId = story.lastReadChapterId
      )
    }
  }
}

/**
 * Room Entity for storing chapters within a story.
 */
@Entity(tableName = "chapters")
data class ChapterEntity(
  @PrimaryKey val id: String,
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
  val isLiked: Boolean = false,
  val lastUpdatedTimestamp: Long = System.currentTimeMillis()
) {
  fun toDomain(): Chapter {
    return Chapter(
      id = id,
      storyId = storyId,
      chapterNumber = chapterNumber,
      title = title,
      content = content,
      wordsCount = wordsCount,
      readsCount = readsCount,
      likesCount = likesCount,
      commentsCount = commentsCount,
      publishedDate = publishedDate,
      isDraft = isDraft,
      isLiked = isLiked
    )
  }

  companion object {
    fun fromDomain(chapter: Chapter): ChapterEntity {
      return ChapterEntity(
        id = chapter.id,
        storyId = chapter.storyId,
        chapterNumber = chapter.chapterNumber,
        title = chapter.title,
        content = chapter.content,
        wordsCount = if (chapter.wordsCount > 0) chapter.wordsCount else chapter.content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size,
        readsCount = chapter.readsCount,
        likesCount = chapter.likesCount,
        commentsCount = chapter.commentsCount,
        publishedDate = chapter.publishedDate,
        isDraft = chapter.isDraft,
        isLiked = chapter.isLiked,
        lastUpdatedTimestamp = System.currentTimeMillis()
      )
    }
  }
}

/**
 * Room Entity for storing comments associated with specific stories and chapters.
 */
@Entity(tableName = "comments")
data class CommentEntity(
  @PrimaryKey val id: String,
  val storyId: String,
  val chapterId: String,
  val userId: String,
  val userName: String,
  val userAvatar: String = "",
  val text: String,
  val timestamp: String = "Just now",
  val likes: Int = 0,
  val isLiked: Boolean = false,
  val inlineQuote: String? = null,
  val createdTimestamp: Long = System.currentTimeMillis()
) {
  fun toDomain(): com.example.data.Comment {
    return com.example.data.Comment(
      id = id,
      storyId = storyId,
      chapterId = chapterId,
      userId = userId,
      userName = userName,
      userAvatar = userAvatar,
      text = text,
      timestamp = timestamp,
      likes = likes,
      isLiked = isLiked,
      replies = emptyList(),
      inlineQuote = inlineQuote
    )
  }

  companion object {
    fun fromDomain(comment: com.example.data.Comment): CommentEntity {
      return CommentEntity(
        id = comment.id,
        storyId = comment.storyId,
        chapterId = comment.chapterId,
        userId = comment.userId,
        userName = comment.userName,
        userAvatar = comment.userAvatar,
        text = comment.text,
        timestamp = comment.timestamp,
        likes = comment.likes,
        isLiked = comment.isLiked,
        inlineQuote = comment.inlineQuote,
        createdTimestamp = System.currentTimeMillis()
      )
    }
  }
}
