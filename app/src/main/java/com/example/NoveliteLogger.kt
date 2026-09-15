package com.example

import android.util.Log

object NoveliteLogger {
  private const val TAG = "NoveliteDebug"
  private val recompositionCounts = mutableMapOf<String, Int>()
  private val navigationTimings = mutableListOf<Long>()

  fun logRecomposition(componentName: String) {
    val count = recompositionCounts.getOrPut(componentName) { 0 } + 1
    recompositionCounts[componentName] = count
    if (count % 10 == 0) {
      Log.w(TAG, "RECOMPOSITION CASCADE WARNING: [$componentName] has recomposed $count times!")
    } else {
      Log.d(TAG, "Recomposition: [$componentName] (Count: $count)")
    }
  }

  fun <T> trackNavigationTiming(actionName: String, block: () -> T): T {
    val startTime = System.currentTimeMillis()
    Log.d(TAG, "Navigation Start: $actionName at $startTime")
    return try {
      val result = block()
      val duration = System.currentTimeMillis() - startTime
      navigationTimings.add(duration)
      Log.d(TAG, "Navigation Success: $actionName took ${duration}ms")
      if (duration > 300) {
        Log.w(TAG, "SLOW NAVIGATION WARNING: $actionName took ${duration}ms (>300ms)")
      }
      result
    } catch (e: Throwable) {
      val duration = System.currentTimeMillis() - startTime
      Log.e(TAG, "Navigation Error: $actionName failed after ${duration}ms", e)
      throw e
    }
  }
}
