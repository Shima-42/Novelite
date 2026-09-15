package com.example.ui.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DayStatus
import com.example.data.StreakHistoryDay
import com.example.ui.NoveliteViewModel
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
import com.example.ui.theme.ShieldEmerald

/**
 * StreakCalendar composable that visualizes the user's reading activity
 * over the past month in a structured 7-column grid layout, using conditional
 * colors to highlight active streak days, protected shield days, and rest days.
 */
@Composable
fun StreakCalendar(
  modifier: Modifier = Modifier,
  monthName: String = "September 2026",
  calendarDays: List<StreakHistoryDay> = remember { generateDefaultMonthHistory() },
  onDaySelected: ((StreakHistoryDay) -> Unit)? = null
) {
  var selectedDay by remember { mutableStateOf<StreakHistoryDay?>(null) }

  val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")

  // Statistics calculation for the month
  val activeStreakDaysCount = calendarDays.count { it.status == DayStatus.COMPLETED || it.status == DayStatus.TODAY }
  val shieldedDaysCount = calendarDays.count { it.status == DayStatus.PROTECTED }
  val totalRecordedDays = calendarDays.count { it.status != DayStatus.FUTURE }
  val completionRate = if (totalRecordedDays > 0) {
    ((activeStreakDaysCount + shieldedDaysCount).toFloat() / totalRecordedDays * 100).toInt()
  } else 0

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("streak_calendar_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header: Month Title, Navigation and Icon
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
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = null,
              tint = NoveliteSecondaryAccent,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "STREAK ACTIVITY CALENDAR",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp,
              color = NoveliteDarkBrown
            )
            Text(
              text = monthName,
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = NoveliteTextPrimary
            )
          }
        }

        // Mini Monthly stats pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = NoveliteCreamBg,
          border = BorderStroke(1.dp, NoveliteBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "🔥 $activeStreakDaysCount Active",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Month Consistency Summary Cards
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SummaryStatBadge(
          label = "Consistency",
          value = "$completionRate%",
          color = NoveliteSecondaryAccent,
          modifier = Modifier.weight(1f)
        )
        SummaryStatBadge(
          label = "Active Days",
          value = "$activeStreakDaysCount",
          color = NoveliteCaramel,
          modifier = Modifier.weight(1f)
        )
        SummaryStatBadge(
          label = "Shield Saved",
          value = "$shieldedDaysCount",
          color = ShieldEmerald,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Days of Week Header Row (Sun -> Sat)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        daysOfWeek.forEach { dayName ->
          Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = dayName,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextMuted,
              textAlign = TextAlign.Center
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 7-Column Grid Layout for the Month
      val leadingBlankDays = 2 // September starts on Tuesday
      val totalSlots = ((leadingBlankDays + calendarDays.size + 6) / 7) * 7

      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        var currentSlot = 0
        while (currentSlot < totalSlots) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            for (col in 0 until 7) {
              val slotIndex = currentSlot + col
              val dayIndex = slotIndex - leadingBlankDays
              if (dayIndex in calendarDays.indices) {
                val day = calendarDays[dayIndex]
                StreakGridDayCell(
                  day = day,
                  isSelected = selectedDay?.dayNumber == day.dayNumber,
                  modifier = Modifier.weight(1f),
                  onClick = {
                    selectedDay = day
                    onDaySelected?.invoke(day)
                  }
                )
              } else {
                Spacer(modifier = Modifier.weight(1f))
              }
            }
          }
          currentSlot += 7
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Conditional Color Legend
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        StreakLegendItem(
          color = NoveliteSecondaryAccent,
          icon = "🔥",
          label = "Active Streak"
        )
        StreakLegendItem(
          color = ShieldEmerald,
          icon = "🛡️",
          label = "Shielded"
        )
        StreakLegendItem(
          color = NoveliteDarkBrown,
          icon = "★",
          label = "Today"
        )
        StreakLegendItem(
          color = NoveliteBorder,
          icon = "•",
          label = "Rest / Missed"
        )
      }

      // Selected Day Details Banner
      AnimatedVisibility(
        visible = selectedDay != null,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        selectedDay?.let { day ->
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = NoveliteCreamBg,
            border = BorderStroke(1.dp, NoveliteSecondaryAccent)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = when (day.status) {
                    DayStatus.COMPLETED -> "🔥"
                    DayStatus.TODAY -> "🌟"
                    DayStatus.PROTECTED -> "🛡️"
                    DayStatus.MISSED -> "💤"
                    DayStatus.FUTURE -> "⏳"
                  },
                  fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Day ${day.dayNumber} • ${day.status.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = NoveliteDarkBrown
                  )
                  Text(
                    text = when (day.status) {
                      DayStatus.COMPLETED -> "Reading goal achieved • Streak logged"
                      DayStatus.TODAY -> "Today's daily reading ritual"
                      DayStatus.PROTECTED -> "Streak Shield deployed • Protected"
                      DayStatus.MISSED -> "Rest day • No reading logged"
                      DayStatus.FUTURE -> "Upcoming reading ritual"
                    },
                    fontSize = 11.sp,
                    color = NoveliteTextMuted
                  )
                }
              }

              IconButton(
                onClick = { selectedDay = null },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  Icons.Default.Close,
                  contentDescription = "Close details",
                  tint = NoveliteDarkBrown,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Individual grid day cell with conditional background, border and icon highlights.
 */
@Composable
private fun StreakGridDayCell(
  day: StreakHistoryDay,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  // Conditional color styling based on streak status
  val (bgColor, borderColor, textColor, badgeIcon) = when (day.status) {
    DayStatus.COMPLETED -> Quadruple(
      NoveliteSoftAccentBg,
      NoveliteSecondaryAccent,
      NoveliteDarkBrown,
      "🔥"
    )
    DayStatus.TODAY -> Quadruple(
      NoveliteCreamBg,
      NoveliteDarkBrown,
      NoveliteDarkBrown,
      "★"
    )
    DayStatus.PROTECTED -> Quadruple(
      ShieldEmerald.copy(alpha = 0.12f),
      ShieldEmerald,
      NoveliteDarkBrown,
      "🛡️"
    )
    DayStatus.MISSED -> Quadruple(
      NoveliteCreamBg.copy(alpha = 0.5f),
      NoveliteBorder,
      NoveliteTextMuted,
      ""
    )
    DayStatus.FUTURE -> Quadruple(
      NoveliteCreamBg.copy(alpha = 0.25f),
      NoveliteBorder.copy(alpha = 0.4f),
      NoveliteTextMuted.copy(alpha = 0.4f),
      ""
    )
  }

  Surface(
    modifier = modifier
      .aspectRatio(1f)
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .testTag("streak_calendar_day_${day.dayNumber}"),
    color = if (isSelected) NoveliteSecondaryAccent.copy(alpha = 0.25f) else bgColor,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(
      width = if (isSelected || day.status == DayStatus.TODAY) 2.dp else 1.dp,
      color = if (isSelected) NoveliteSecondaryAccent else borderColor
    )
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      if (badgeIcon.isNotEmpty()) {
        Text(
          text = badgeIcon,
          fontSize = 9.sp,
          lineHeight = 10.sp
        )
      }
      Text(
        text = "${day.dayNumber}",
        fontSize = 10.sp,
        fontWeight = if (day.status == DayStatus.COMPLETED || day.status == DayStatus.TODAY) FontWeight.Bold else FontWeight.Medium,
        color = textColor
      )
    }
  }
}

@Composable
private fun SummaryStatBadge(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    color = NoveliteCreamBg,
    border = BorderStroke(1.dp, NoveliteBorder)
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = color
      )
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = NoveliteDarkBrown.copy(alpha = 0.75f)
      )
    }
  }
}

@Composable
private fun StreakLegendItem(
  color: Color,
  icon: String,
  label: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Surface(
      modifier = Modifier.size(14.dp),
      shape = RoundedCornerShape(4.dp),
      color = color.copy(alpha = 0.25f),
      border = BorderStroke(1.dp, color)
    ) {
      Box(contentAlignment = Alignment.Center) {
        if (icon.length == 1) {
          Text(text = icon, fontSize = 7.sp, color = color, fontWeight = FontWeight.Bold)
        }
      }
    }
    Text(
      text = label,
      fontSize = 9.sp,
      fontWeight = FontWeight.SemiBold,
      color = NoveliteDarkBrown
    )
  }
}

/**
 * Convenience wrapper linking StreakCalendar to NoveliteViewModel
 */
@Composable
fun StreakCalendar(
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier
) {
  val calendarDays = remember { viewModel.repository.getStreakCalendarDays() }
  StreakCalendar(
    modifier = modifier,
    monthName = "September 2026",
    calendarDays = calendarDays
  )
}

/**
 * Generates default 30-day mock history for stand-alone previews and testing.
 */
fun generateDefaultMonthHistory(): List<StreakHistoryDay> {
  val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
  return (1..30).map { day ->
    val status = when {
      day == 26 -> DayStatus.TODAY
      day in 14..25 -> DayStatus.COMPLETED
      day == 13 -> DayStatus.PROTECTED
      day in 3..12 -> DayStatus.COMPLETED
      day in 1..2 -> DayStatus.MISSED
      else -> DayStatus.FUTURE
    }
    StreakHistoryDay(
      dateIso = "2026-08-${day.toString().padStart(2, '0')}",
      dayLabel = daysOfWeek[(day - 1) % 7],
      dayNumber = day,
      status = status
    )
  }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
