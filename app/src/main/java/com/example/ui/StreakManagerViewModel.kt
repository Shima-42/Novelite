package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NoveliteRepository
import com.example.data.local.StreakMetadataEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * StreakManager ViewModel
 * Dedicated ViewModel to track daily reading/writing activity, manage streak freezes/shields,
 * and calculate active consecutive streak day count reactively from the Room database.
 */
class StreakManagerViewModel(
  private val repository: NoveliteRepository = NoveliteRepository()
) : ViewModel() {

  val currentUser = repository.currentUser
  val todayMinutesRead = repository.todayMinutesRead
  val todayWordsWritten = repository.todayWordsWritten
  val milestoneEvents = repository.milestoneEvents

  // Active consecutive reading streak count
  val activeStreakDays: StateFlow<Int> = repository.currentUser
    .combine(repository.todayMinutesRead) { user, todayMins ->
      user.readingStreak
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = 12
    )

  // Longest streak achieved
  val longestStreakDays: StateFlow<Int> = repository.currentUser
    .combine(repository.todayMinutesRead) { user, _ ->
      user.longestStreak
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = 28
    )

  // Daily goal progress (0f to 1f+)
  val dailyGoalProgressFraction: StateFlow<Float> = combine(
    repository.todayMinutesRead,
    repository.currentUser
  ) { minutes, user ->
    val goal = if (user.readingGoalMinutes > 0) user.readingGoalMinutes else 10
    (minutes.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 0.8f
  )

  // Is daily goal completed today
  val isGoalMetToday: StateFlow<Boolean> = combine(
    repository.todayMinutesRead,
    repository.currentUser
  ) { minutes, user ->
    minutes >= user.readingGoalMinutes
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = false
  )

  /**
   * Record reading activity time in minutes.
   * Updates streak count if daily goal is achieved.
   */
  fun recordReadingActivity(minutes: Int) {
    repository.addReadingTime(minutes)
  }

  /**
   * Record word writing activity.
   */
  fun recordWritingActivity(words: Int) {
    repository.addWritingWords(words)
  }

  /**
   * Consume a streak freeze shield to protect the streak.
   */
  fun useStreakShield(): Boolean {
    return repository.useStreakShield()
  }

  /**
   * Calculate current streak health status message.
   */
  fun getStreakStatusDescription(): String {
    val user = currentUser.value
    val todayMins = todayMinutesRead.value
    val target = user.readingGoalMinutes
    return if (todayMins >= target) {
      "Daily Goal Achieved! Your ${user.readingStreak}-day streak is protected for today. 🔥"
    } else {
      "${target - todayMins} more minutes needed today to keep your ${user.readingStreak}-day flame burning!"
    }
  }

  /**
   * Formats the current date label.
   */
  fun getCurrentDateLabel(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return sdf.format(Date())
  }
}
