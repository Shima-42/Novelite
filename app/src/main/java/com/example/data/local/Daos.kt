package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Streak Metadata and daily reading tracking.
 */
@Dao
interface StreakDao {

  @Query("SELECT * FROM streak_metadata WHERE userId = :userId LIMIT 1")
  fun getStreakMetadataFlow(userId: String = "user_me"): Flow<StreakMetadataEntity?>

  @Query("SELECT * FROM streak_metadata WHERE userId = :userId LIMIT 1")
  suspend fun getStreakMetadata(userId: String = "user_me"): StreakMetadataEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateStreak(streak: StreakMetadataEntity)

  @Query("UPDATE streak_metadata SET todayMinutesRead = :minutes, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
  suspend fun updateTodayMinutes(userId: String, minutes: Int, timestamp: Long)

  @Query("UPDATE streak_metadata SET consecutiveDays = :consecutive, longestStreak = :longest, lastActivityDate = :date, todayMinutesRead = :todayMins, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
  suspend fun updateStreakProgress(
    userId: String,
    consecutive: Int,
    longest: Int,
    date: String,
    todayMins: Int,
    timestamp: Long
  )

  @Query("UPDATE streak_metadata SET streakShields = :shields WHERE userId = :userId")
  suspend fun updateShields(userId: String, shields: Int)

  @Query("UPDATE streak_metadata SET totalWordsWritten = totalWordsWritten + :words, writingStreak = :writingStreak, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
  suspend fun addWritingProgress(userId: String, words: Int, writingStreak: Int, timestamp: Long)
}

/**
 * Data Access Object for User Stories.
 */
@Dao
interface StoryDao {

  @Query("SELECT * FROM stories ORDER BY lastUpdatedTimestamp DESC")
  fun getAllStoriesFlow(): Flow<List<StoryEntity>>

  @Query("SELECT * FROM stories WHERE isInLibrary = 1 ORDER BY lastUpdatedTimestamp DESC")
  fun getLibraryStoriesFlow(): Flow<List<StoryEntity>>

  @Query("SELECT * FROM stories WHERE authorId = :authorId ORDER BY lastUpdatedTimestamp DESC")
  fun getStoriesByAuthorFlow(authorId: String): Flow<List<StoryEntity>>

  @Query("SELECT * FROM stories WHERE id = :id LIMIT 1")
  fun getStoryByIdFlow(id: String): Flow<StoryEntity?>

  @Query("SELECT * FROM stories WHERE id = :id LIMIT 1")
  suspend fun getStoryById(id: String): StoryEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStory(story: StoryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStories(stories: List<StoryEntity>)

  @Update
  suspend fun updateStory(story: StoryEntity)

  @Query("UPDATE stories SET isLiked = :isLiked, likesCount = :likesCount WHERE id = :storyId")
  suspend fun updateLikeStatus(storyId: String, isLiked: Boolean, likesCount: Int)

  @Query("UPDATE stories SET isInLibrary = :isInLibrary, libraryCategoryName = :categoryName WHERE id = :storyId")
  suspend fun updateLibraryStatus(storyId: String, isInLibrary: Boolean, categoryName: String?)

  @Query("UPDATE stories SET readingProgressPercent = :progress, lastReadChapterId = :chapterId, isInLibrary = 1 WHERE id = :storyId")
  suspend fun updateReadingProgress(storyId: String, chapterId: String, progress: Float)

  @Query("SELECT * FROM stories WHERE readingProgressPercent > 0 ORDER BY lastUpdatedTimestamp DESC")
  fun getRecentlyReadStoriesFlow(): Flow<List<StoryEntity>>

  @Query("SELECT * FROM stories WHERE title LIKE '%' || :query || '%' ORDER BY lastUpdatedTimestamp DESC")
  fun searchStoriesByTitleFlow(query: String): Flow<List<StoryEntity>>

  @Query("DELETE FROM stories WHERE id = :id")
  suspend fun deleteStoryById(id: String)
}

/**
 * Data Access Object for Chapters.
 */
@Dao
interface ChapterDao {

  @Query("SELECT * FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC")
  fun getChaptersForStoryFlow(storyId: String): Flow<List<ChapterEntity>>

  @Query("SELECT * FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC")
  suspend fun getChaptersForStory(storyId: String): List<ChapterEntity>

  @Query("SELECT * FROM chapters WHERE id = :id LIMIT 1")
  fun getChapterByIdFlow(id: String): Flow<ChapterEntity?>

  @Query("SELECT * FROM chapters WHERE id = :id LIMIT 1")
  suspend fun getChapterById(id: String): ChapterEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChapter(chapter: ChapterEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChapters(chapters: List<ChapterEntity>)

  @Update
  suspend fun updateChapter(chapter: ChapterEntity)

  @Query("DELETE FROM chapters WHERE id = :id")
  suspend fun deleteChapterById(id: String)

  @Query("DELETE FROM chapters WHERE storyId = :storyId")
  suspend fun deleteChaptersForStory(storyId: String)
}

/**
 * Data Access Object for Comments.
 */
@Dao
interface CommentDao {

  @Query("SELECT * FROM comments WHERE storyId = :storyId AND chapterId = :chapterId ORDER BY createdTimestamp DESC")
  fun getCommentsForChapterFlow(storyId: String, chapterId: String): Flow<List<CommentEntity>>

  @Query("SELECT * FROM comments WHERE storyId = :storyId ORDER BY createdTimestamp DESC")
  fun getCommentsForStoryFlow(storyId: String): Flow<List<CommentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComment(comment: CommentEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComments(comments: List<CommentEntity>)

  @Query("UPDATE comments SET likes = :likes, isLiked = :isLiked WHERE id = :commentId")
  suspend fun updateCommentLike(commentId: String, likes: Int, isLiked: Boolean)

  @Query("DELETE FROM comments WHERE id = :commentId")
  suspend fun deleteComment(commentId: String)
}
