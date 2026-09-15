package com.example.ui.screens

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DayStatus
import com.example.data.MilestoneEvent
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.HabitStreak
import com.example.ui.components.StreakCalendar
import com.example.ui.components.StreakMilestoneFlareOverlay
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteButtonBg
import com.example.ui.theme.NoveliteButtonText
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite
import com.example.ui.theme.ShieldEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StreakDashboardScreen(viewModel: NoveliteViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  val todayMinutes by viewModel.todayMinutesRead.collectAsState()
  val challenges by viewModel.challenges.collectAsState()
  val calendarDays = remember { viewModel.repository.getStreakCalendarDays() }

  var shieldMessage by remember { mutableStateOf<String?>(null) }
  var showFlareAnimation by remember { mutableStateOf(false) }

  // Event-based trigger for milestone flare celebration
  androidx.compose.runtime.LaunchedEffect(Unit) {
    viewModel.repository.milestoneEvents.collect { event ->
      if (event is MilestoneEvent.GoalCompleted || event is MilestoneEvent.StreakMilestone) {
        showFlareAnimation = true
      }
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "heroFlame")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.06f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flamePulse"
  )

  val streakTier = when {
    currentUser.readingStreak >= 100 -> "Supernova Master 🔥"
    currentUser.readingStreak >= 50 -> "Beacon of Stories 🌟"
    currentUser.readingStreak >= 30 -> "Inferno Reader ⚡"
    currentUser.readingStreak >= 14 -> "Blaze Artisan 🔥"
    currentUser.readingStreak >= 7 -> "Flame Seeker 🕯️"
    else -> "Story Spark ✨"
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .testTag("streak_dashboard_screen")
    ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      IconButton(onClick = { viewModel.navigateTo(Screen.HOME) }) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = NoveliteDarkBrown
        )
      }

      Text(
        text = "STREAK SANCTUARY",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = NoveliteDarkBrown
      )

      Box(modifier = Modifier.size(48.dp))
    }

    // Scrollable Content
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Hero Flame
      Box(
        modifier = Modifier
          .size(120.dp)
          .graphicsLayer {
            scaleX = pulseScale
            scaleY = pulseScale
          }
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(NoveliteCaramel, NoveliteWarmBrown, NoveliteDarkBrown.copy(alpha = 0.2f), Color.Transparent)
            )
          )
          .clickable { showFlareAnimation = true },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.LocalFireDepartment,
          contentDescription = "Glowing Flame",
          tint = NoveliteWhite,
          modifier = Modifier.size(70.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "${currentUser.readingStreak} DAY STREAK",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = NoveliteTextPrimary
      )

      Text(
        text = "Status: $streakTier",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = NoveliteCaramel
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Streaks Stats Overview (Reading vs Writing vs Longest)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StreakStatCard(
          title = "Reading",
          value = "${currentUser.readingStreak}d",
          subtitle = "Daily habit",
          icon = Icons.Default.LocalFireDepartment,
          tint = NoveliteCaramel,
          modifier = Modifier.weight(1f)
        )
        StreakStatCard(
          title = "Writing",
          value = "${currentUser.writingStreak}d",
          subtitle = "Active author",
          icon = Icons.Default.Star,
          tint = NoveliteDarkBrown,
          modifier = Modifier.weight(1f)
        )
        StreakStatCard(
          title = "Longest",
          value = "${currentUser.longestStreak}d",
          subtitle = "Personal best",
          icon = Icons.Default.EmojiEvents,
          tint = NoveliteWarmBrown,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Streak Shield Inventory Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(NoveliteCreamBg),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = null,
                  tint = NoveliteDarkBrown,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Streak Shields (${currentUser.streakShields} Available)",
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteTextPrimary
                )
                Text(
                  text = "Safeguards your flame if life gets in the way",
                  fontSize = 11.sp,
                  color = NoveliteTextMuted
                )
              }
            }
          }

          if (shieldMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = shieldMessage!!,
              color = NoveliteWarmBrown,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                if (currentUser.streakShields > 0) {
                  viewModel.useShield()
                  shieldMessage = "🛡️ Shield activated! Today is safeguarded."
                } else {
                  shieldMessage = "No shields remaining! Complete a 30-day streak to earn more."
                }
              },
              modifier = Modifier
                .weight(1f)
                .height(42.dp),
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = NoveliteButtonBg,
                contentColor = NoveliteButtonText
              ),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = NoveliteButtonText
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Use Shield",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteButtonText
                )
              }
            }

            Button(
              onClick = { viewModel.addReadingMinute(5) },
              modifier = Modifier
                .weight(1f)
                .height(42.dp),
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = NoveliteButtonBg,
                contentColor = NoveliteButtonText
              ),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.LocalFireDepartment,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = NoveliteButtonText
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "+5 Min Read",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteButtonText
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Interactive HabitStreak Tracker Composable
      HabitStreak(
        initialStreak = currentUser.readingStreak,
        onStreakIncrement = { newCount ->
          viewModel.addReadingMinute(1)
        }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // 30-Day Activity Calendar Grid
      StreakCalendar(viewModel = viewModel)

      Spacer(modifier = Modifier.height(20.dp))

      // Milestones Ladder
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Streak Milestones & Trophies 🏆",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )

          Spacer(modifier = Modifier.height(12.dp))

          val milestones = listOf(
            Triple(3, "Spark Starter", currentUser.readingStreak >= 3),
            Triple(7, "7-Day Flame Master", currentUser.readingStreak >= 7),
            Triple(14, "14-Day Blaze", currentUser.readingStreak >= 14),
            Triple(30, "30-Day Inferno & +1 Shield", currentUser.readingStreak >= 30),
            Triple(50, "50-Day Story Beacon", currentUser.readingStreak >= 50),
            Triple(100, "100-Day Supernova Legend", currentUser.readingStreak >= 100)
          )

          milestones.forEach { (targetDays, title, isUnlocked) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) NoveliteDarkBrown else NoveliteCreamBg),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) NoveliteCardBeige else NoveliteTextMuted,
                    modifier = Modifier.size(16.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isUnlocked) NoveliteTextPrimary else NoveliteTextMuted
                  )
                  Text(
                    text = "$targetDays Days reading streak",
                    fontSize = 11.sp,
                    color = NoveliteTextMuted
                  )
                }
              }

              Text(
                text = if (isUnlocked) "UNLOCKED" else "${targetDays - currentUser.readingStreak}d left",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) NoveliteWarmBrown else NoveliteCaramel
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Active Community Challenges
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Active Community Challenges 🎯",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )

          Spacer(modifier = Modifier.height(10.dp))

          challenges.forEach { challenge ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = challenge.title, fontWeight = FontWeight.Bold, color = NoveliteTextPrimary, fontSize = 13.sp)
                Text(text = challenge.reward, color = NoveliteCaramel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(text = challenge.description, color = NoveliteTextMuted, fontSize = 11.sp)
              Spacer(modifier = Modifier.height(6.dp))
              LinearProgressIndicator(
                progress = { (challenge.currentProgress.toFloat() / maxOf(1, challenge.targetValue)).coerceIn(0f, 1f) },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = NoveliteDarkBrown,
                trackColor = NoveliteCreamBg
              )
            }
          }
        }
      }

        Spacer(modifier = Modifier.height(30.dp))
      }
    }

    // Flare / Confetti Animation Overlay
    StreakMilestoneFlareOverlay(
      isVisible = showFlareAnimation,
      onDismiss = { showFlareAnimation = false }
    )
  }
}

@Composable
fun StreakStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tint: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = NoveliteCardBeige,
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = NoveliteTextPrimary)
      Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NoveliteWarmBrown)
      Text(text = subtitle, fontSize = 9.sp, color = NoveliteTextMuted)
    }
  }
}

@Composable
fun CalendarDayCell(day: com.example.data.StreakHistoryDay) {
  val (cellBg, cellBorder, icon) = when (day.status) {
    DayStatus.COMPLETED -> Triple(NoveliteCreamBg, NoveliteCaramel, "🔥")
    DayStatus.PROTECTED -> Triple(NoveliteCreamBg, ShieldEmerald, "🛡️")
    DayStatus.MISSED -> Triple(NoveliteCreamBg.copy(alpha = 0.5f), NoveliteBorder, "•")
    DayStatus.TODAY -> Triple(NoveliteCardBeige, NoveliteDarkBrown, "🔥")
    DayStatus.FUTURE -> Triple(NoveliteCreamBg.copy(alpha = 0.3f), NoveliteBorder.copy(alpha = 0.3f), "")
  }

  Surface(
    modifier = Modifier
      .size(38.dp)
      .clip(RoundedCornerShape(8.dp)),
    color = cellBg,
    border = androidx.compose.foundation.BorderStroke(1.dp, cellBorder)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      if (icon.isNotEmpty()) {
        Text(text = icon, fontSize = 10.sp)
      }
      Text(
        text = "${day.dayNumber}",
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        color = if (day.status == DayStatus.TODAY) NoveliteDarkBrown else NoveliteTextPrimary
      )
    }
  }
}

@Composable
fun LegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 10.sp, color = NoveliteTextMuted)
  }
}
