package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NoveliteViewModel
import com.example.ui.theme.FlameGold
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteSecondaryAccent
import com.example.ui.theme.NoveliteSoftAccentBg
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

/**
 * StoryStreak UI component that displays the user's current reading/writing
 * streak days and a fire icon, aligning with the app's habit-streaking core feature.
 */
@Composable
fun StoryStreak(
  readingStreak: Int,
  writingStreak: Int,
  modifier: Modifier = Modifier,
  longestStreak: Int = maxOf(readingStreak, 14),
  todayMinutes: Int = 15,
  dailyGoalMinutes: Int = 20,
  streakShieldsCount: Int = 2,
  onStreakClick: (() -> Unit)? = null
) {
  val infiniteTransition = rememberInfiniteTransition(label = "streak_flame")
  val flameScale by infiniteTransition.animateFloat(
    initialValue = 0.94f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flame_pulse"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("story_streak_component")
      .then(
        if (onStreakClick != null) Modifier.clickable { onStreakClick() }
        else Modifier
      ),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header Row: Section title and Habit badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Pulsing Fire Icon container
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  colors = listOf(FlameGold, NoveliteCaramel, NoveliteWarmBrown)
                )
              )
              .testTag("story_streak_fire_icon"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak Fire",
              tint = NoveliteWhite,
              modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                  scaleX = flameScale
                  scaleY = flameScale
                }
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "Story Habit Streak",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = NoveliteDarkBrown
            )
            Text(
              text = "Daily literary momentum",
              fontSize = 11.sp,
              color = NoveliteTextMuted
            )
          }
        }

        // Streak Status Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = NoveliteSoftAccentBg,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteCaramel.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Streak Protection",
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$streakShieldsCount shields",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Dual Streak Metrics: Reading & Writing
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(NoveliteCreamBg)
          .border(1.dp, NoveliteBorder, RoundedCornerShape(14.dp))
          .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Reading Streak Pillar
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .weight(1f)
            .testTag("story_streak_reading_days")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = null,
              tint = NoveliteCaramel,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Reading Streak",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = NoveliteTextMuted
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$readingStreak",
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Serif,
              color = NoveliteDarkBrown
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "days 🔥",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteCaramel,
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
        }

        // Vertical divider
        Box(
          modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(NoveliteBorder)
        )

        // Writing Streak Pillar
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .weight(1f)
            .testTag("story_streak_writing_days")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Create,
              contentDescription = null,
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Writing Streak",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = NoveliteTextMuted
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$writingStreak",
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Serif,
              color = NoveliteDarkBrown
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "days ✍️",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteWarmBrown,
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Today's Goal Progress Bar
      val goalFraction = if (dailyGoalMinutes > 0) {
        (todayMinutes.toFloat() / dailyGoalMinutes).coerceIn(0f, 1f)
      } else 1f

      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Today's Habit: $todayMinutes/$dailyGoalMinutes min",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = NoveliteTextPrimary
          )
          Text(
            text = if (todayMinutes >= dailyGoalMinutes) "Goal Completed ✓" else "Record: ${longestStreak}d",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (todayMinutes >= dailyGoalMinutes) NoveliteWarmBrown else NoveliteCaramel
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { goalFraction },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = NoveliteSecondaryAccent,
          trackColor = NoveliteSoftAccentBg
        )
      }
    }
  }
}

/**
 * Compact inline chip variant of StoryStreak for headers or navigation toolbars
 */
@Composable
fun StoryStreakBadge(
  readingStreak: Int,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = NoveliteSoftAccentBg,
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    modifier = modifier
      .testTag("story_streak_badge")
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.LocalFireDepartment,
        contentDescription = "Streak Flame",
        tint = NoveliteCaramel,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "${readingStreak}d",
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = NoveliteDarkBrown
      )
    }
  }
}

/**
 * ViewModel-connected overload of StoryStreak
 */
@Composable
fun StoryStreak(
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier,
  onStreakClick: (() -> Unit)? = null
) {
  val currentUser by viewModel.currentUser.collectAsState()
  val todayMinutes by viewModel.todayMinutesRead.collectAsState()

  StoryStreak(
    readingStreak = currentUser.readingStreak,
    writingStreak = currentUser.writingStreak,
    longestStreak = currentUser.longestStreak,
    todayMinutes = todayMinutes,
    dailyGoalMinutes = currentUser.readingGoalMinutes,
    streakShieldsCount = currentUser.streakShields,
    onStreakClick = onStreakClick ?: { viewModel.openStreakDashboard() },
    modifier = modifier
  )
}
