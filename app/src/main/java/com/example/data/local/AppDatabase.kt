package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Novelite Room Database.
 * Persists user stories, chapters, and reading streak metadata.
 */
@Database(
  entities = [
    StreakMetadataEntity::class,
    StoryEntity::class,
    ChapterEntity::class,
    CommentEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun streakDao(): StreakDao
  abstract fun storyDao(): StoryDao
  abstract fun chapterDao(): ChapterDao
  abstract fun commentDao(): CommentDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "novelite_database"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
