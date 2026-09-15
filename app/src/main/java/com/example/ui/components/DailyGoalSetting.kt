package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

private const val PREFS_NAME = "novelite_storage"
private const val KEY_DAILY_READING_GOAL = "pref_daily_reading_goal_minutes"

/**
 * DailyGoalSetting composable that allows users to define a target number
 * of minutes for their reading habit and persist this value to local storage.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyGoalSetting(
  modifier: Modifier = Modifier,
  initialGoalMinutes: Int = 10,
  onGoalSaved: ((Int) -> Unit)? = null
) {
  val context = LocalContext.current
  val sharedPreferences = remember(context) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  // Load persisted goal from SharedPreferences or fallback to initialGoalMinutes
  var targetMinutes by remember {
    val saved = sharedPreferences.getInt(KEY_DAILY_READING_GOAL, initialGoalMinutes)
    mutableIntStateOf(if (saved in 1..180) saved else initialGoalMinutes)
  }

  var savedSuccessNotification by remember { mutableStateOf(false) }

  // Quick preset targets
  val presetGoals = listOf(
    Pair(5, "Bite ⚡"),
    Pair(10, "Daily 📖"),
    Pair(15, "Steady 🌿"),
    Pair(20, "Focused 🎯"),
    Pair(30, "Avid 🔥"),
    Pair(45, "Scholar 📜"),
    Pair(60, "Master 👑")
  )

  // Estimated benefits breakdown based on chosen minutes
  val estimatedMonthlyWords = targetMinutes * 200 * 30
  val estimatedBooksPerMonth = maxOf(1, estimatedMonthlyWords / 50000)

  fun saveGoalToLocalStorage(minutes: Int) {
    targetMinutes = minutes
    sharedPreferences.edit().putInt(KEY_DAILY_READING_GOAL, minutes).apply()
    savedSuccessNotification = true
    onGoalSaved?.invoke(minutes)
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("daily_goal_setting_card"),
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
      // Header
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
              .background(NoveliteSoftAccentBg)
              .border(1.dp, NoveliteSecondaryAccent, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = NoveliteSecondaryAccent,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "DAILY READING GOAL",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp,
              color = NoveliteDarkBrown
            )
            Text(
              text = "Define your daily reading habit",
              fontSize = 13.sp,
              color = NoveliteDarkBrown.copy(alpha = 0.75f)
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = NoveliteSoftAccentBg,
          border = BorderStroke(1.dp, NoveliteBorder)
        ) {
          Text(
            text = "Saved Local",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteDarkBrown,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Main Target Display Box with Steppers
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = NoveliteCreamBg,
        border = BorderStroke(1.dp, NoveliteBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              if (targetMinutes > 5) {
                val next = targetMinutes - 5
                targetMinutes = next
                saveGoalToLocalStorage(next)
              }
            },
            enabled = targetMinutes > 5,
            modifier = Modifier
              .size(40.dp)
              .testTag("decrease_goal_button"),
            colors = IconButtonDefaults.iconButtonColors(
              containerColor = NoveliteCardBeige,
              contentColor = NoveliteDarkBrown
            )
          ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease 5 minutes", tint = NoveliteDarkBrown)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = "$targetMinutes",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = NoveliteDarkBrown
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "min / day",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = NoveliteDarkBrown.copy(alpha = 0.85f),
                modifier = Modifier.padding(bottom = 6.dp)
              )
            }
            Text(
              text = "~$estimatedBooksPerMonth book${if (estimatedBooksPerMonth > 1) "s" else ""} per month (~${estimatedMonthlyWords / 1000}k words)",
              fontSize = 11.sp,
              color = NoveliteSecondaryAccent,
              fontWeight = FontWeight.SemiBold
            )
          }

          IconButton(
            onClick = {
              if (targetMinutes < 120) {
                val next = targetMinutes + 5
                targetMinutes = next
                saveGoalToLocalStorage(next)
              }
            },
            enabled = targetMinutes < 120,
            modifier = Modifier
              .size(40.dp)
              .testTag("increase_goal_button"),
            colors = IconButtonDefaults.iconButtonColors(
              containerColor = NoveliteCardBeige,
              contentColor = NoveliteDarkBrown
            )
          ) {
            Icon(Icons.Default.Add, contentDescription = "Increase 5 minutes", tint = NoveliteDarkBrown)
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Interactive Goal Slider
      Slider(
        value = targetMinutes.toFloat(),
        onValueChange = { targetMinutes = it.toInt() },
        onValueChangeFinished = { saveGoalToLocalStorage(targetMinutes) },
        valueRange = 5f..90f,
        steps = 16,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("goal_slider"),
        colors = SliderDefaults.colors(
          thumbColor = NoveliteDarkBrown,
          activeTrackColor = NoveliteSecondaryAccent,
          inactiveTrackColor = NoveliteSoftAccentBg
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Preset Goal Chips
      Text(
        text = "Quick Presets",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = NoveliteDarkBrown
      )

      Spacer(modifier = Modifier.height(6.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        presetGoals.forEach { (mins, label) ->
          val isSelected = targetMinutes == mins
          FilterChip(
            selected = isSelected,
            onClick = {
              targetMinutes = mins
              saveGoalToLocalStorage(mins)
            },
            label = {
              Text(
                text = "$mins min • $label",
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = NoveliteDarkBrown,
              selectedLabelColor = NoveliteWhite,
              containerColor = NoveliteCreamBg,
              labelColor = NoveliteDarkBrown
            ),
            border = FilterChipDefaults.filterChipBorder(
              borderColor = if (isSelected) NoveliteDarkBrown else NoveliteBorder,
              selectedBorderColor = NoveliteDarkBrown,
              enabled = true,
              selected = isSelected
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Save Action Button
      Button(
        onClick = { saveGoalToLocalStorage(targetMinutes) },
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("save_goal_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = NoveliteSecondaryAccent,
          contentColor = NoveliteWhite
        )
      ) {
        Icon(Icons.Default.Save, contentDescription = null, tint = NoveliteWhite, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Save Goal to Storage ($targetMinutes min)",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NoveliteWhite
        )
      }

      // Success Feedback
      AnimatedVisibility(
        visible = savedSuccessNotification,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
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
              Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = NoveliteDarkBrown,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Target set to $targetMinutes min/day and saved locally!",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NoveliteDarkBrown
              )
            }
            Text(
              text = "OK",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteSecondaryAccent,
              modifier = Modifier.clickable { savedSuccessNotification = false }
            )
          }
        }
      }
    }
  }
}

/**
 * Convenience wrapper linking DailyGoalSetting to the shared NoveliteViewModel
 */
@Composable
fun DailyGoalSetting(
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier
) {
  val currentUser by viewModel.currentUser.collectAsState()
  DailyGoalSetting(
    modifier = modifier,
    initialGoalMinutes = currentUser.readingGoalMinutes,
    onGoalSaved = { newGoalMinutes ->
      viewModel.updateProfile(
        displayName = currentUser.displayName,
        bio = currentUser.bio,
        isPublic = currentUser.isActivityPublic,
        goalMins = newGoalMinutes
      )
    }
  )
}
