package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NoveliteViewModel
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteSecondaryAccent
import com.example.ui.theme.NoveliteSoftAccentBg
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteWarmWhite
import com.example.ui.theme.NoveliteWhite

/**
 * HabitStreak composable that tracks and displays the user's daily reading streak,
 * using a simple state variable to increment the count and celebrate consistency.
 */
@Composable
fun HabitStreak(
  modifier: Modifier = Modifier,
  initialStreak: Int = 12,
  onStreakIncrement: ((Int) -> Unit)? = null
) {
  // Simple state variable to track and increment reading streak count
  var streakCount by remember { mutableIntStateOf(initialStreak) }
  var hasCheckedInToday by remember { mutableStateOf(false) }
  var showCelebration by remember { mutableStateOf(false) }

  // Gentle pulsating animation for the streak flame
  val infiniteTransition = rememberInfiniteTransition(label = "habitFlamePulse")
  val flameScale by infiniteTransition.animateFloat(
    initialValue = 0.94f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flamePulse"
  )

  // Target milestones (e.g. 7 days, 14 days, 30 days, 50 days, 100 days)
  val nextMilestone = when {
    streakCount < 7 -> 7
    streakCount < 14 -> 14
    streakCount < 30 -> 30
    streakCount < 50 -> 50
    streakCount < 100 -> 100
    else -> ((streakCount / 50) + 1) * 50
  }

  val prevMilestone = when {
    streakCount < 7 -> 0
    streakCount < 14 -> 7
    streakCount < 30 -> 14
    streakCount < 50 -> 30
    streakCount < 100 -> 50
    else -> (streakCount / 50) * 50
  }

  val milestoneProgress = ((streakCount - prevMilestone).toFloat() / maxOf(1, nextMilestone - prevMilestone)).coerceIn(0f, 1f)

  val streakTitle = when {
    streakCount >= 50 -> "Legendary Storyteller 👑"
    streakCount >= 30 -> "Bookworm Blaze 🔥"
    streakCount >= 14 -> "Flame Keeper ⚡"
    streakCount >= 7 -> "Spark Igniter ✨"
    else -> "Reading Sprout 🌱"
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("habit_streak_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // Header: Title and Tier
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "DAILY HABIT STREAK",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = NoveliteDarkBrown
          )
          Text(
            text = streakTitle,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = NoveliteSecondaryAccent
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = NoveliteSoftAccentBg,
          border = BorderStroke(1.dp, NoveliteBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak flame",
              tint = NoveliteSecondaryAccent,
              modifier = Modifier
                .size(16.dp)
                .graphicsLayer {
                  scaleX = flameScale
                  scaleY = flameScale
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$streakCount Days",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = NoveliteDarkBrown
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Center Display: Large Streak Counter
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = NoveliteCreamBg,
        border = BorderStroke(1.dp, NoveliteBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(NoveliteSoftAccentBg)
                .border(1.dp, NoveliteSecondaryAccent, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "🔥",
                fontSize = 24.sp,
                modifier = Modifier.graphicsLayer {
                  scaleX = flameScale
                  scaleY = flameScale
                }
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = "$streakCount",
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  fontSize = 32.sp,
                  color = NoveliteDarkBrown
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "days in a row",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = NoveliteDarkBrown.copy(alpha = 0.8f)
                )
              }
              Text(
                text = if (hasCheckedInToday) "Checked in today! 🎉" else "Read today to keep the flame alive",
                fontSize = 11.sp,
                color = if (hasCheckedInToday) NoveliteSecondaryAccent else NoveliteDarkBrown.copy(alpha = 0.7f),
                fontWeight = if (hasCheckedInToday) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Milestone Progress Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Next milestone: $nextMilestone days",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = NoveliteDarkBrown
        )
        Text(
          text = "${nextMilestone - streakCount} days left",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = NoveliteSecondaryAccent
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { milestoneProgress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = NoveliteSecondaryAccent,
        trackColor = NoveliteSoftAccentBg
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Action Buttons using State Variable to Increment Count
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Primary button: Increments the simple streak count state variable
        Button(
          onClick = {
            streakCount += 1
            hasCheckedInToday = true
            showCelebration = true
            onStreakIncrement?.invoke(streakCount)
          },
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("habit_streak_increment_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = NoveliteSecondaryAccent,
            contentColor = NoveliteWhite
          )
        ) {
          Icon(
            imageVector = if (hasCheckedInToday) Icons.Default.Check else Icons.Default.Add,
            contentDescription = null,
            tint = NoveliteWhite,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (hasCheckedInToday) "+1 Extra Day" else "Log Reading (+1)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NoveliteWhite
          )
        }

        // Secondary outline button: Quick reset / simulate
        OutlinedButton(
          onClick = {
            streakCount = maxOf(1, streakCount - 1)
            onStreakIncrement?.invoke(streakCount)
          },
          modifier = Modifier
            .height(44.dp)
            .testTag("habit_streak_adjust_button"),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, NoveliteBorder),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = NoveliteCreamBg,
            contentColor = NoveliteDarkBrown
          )
        ) {
          Text(
            text = "-1",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NoveliteDarkBrown
          )
        }
      }

      // Celebratory feedback banner when streak increments
      AnimatedVisibility(
        visible = showCelebration,
        enter = fadeIn() + scaleIn()
      ) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          color = NoveliteSoftAccentBg,
          border = BorderStroke(1.dp, NoveliteSecondaryAccent)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "🎉", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Streak updated to $streakCount days! Keep reading!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NoveliteDarkBrown
              )
            }
            Text(
              text = "Dismiss",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteSecondaryAccent,
              modifier = Modifier.clickable { showCelebration = false }
            )
          }
        }
      }
    }
  }
}

/**
 * Convenience wrapper linking HabitStreak to the shared NoveliteViewModel
 */
@Composable
fun HabitStreak(
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier
) {
  val user = viewModel.currentUser.value
  HabitStreak(
    initialStreak = user.readingStreak,
    modifier = modifier,
    onStreakIncrement = { newCount ->
      viewModel.addReadingMinute(1)
    }
  )
}
