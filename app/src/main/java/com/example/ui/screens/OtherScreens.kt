package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NoveliteNotification
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite
import com.example.ui.theme.ShieldEmerald

@Composable
fun NotificationsScreen(viewModel: NoveliteViewModel) {
  val notifications by viewModel.notifications.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("notifications_screen")
  ) {
    // Top Bar
    Surface(
      color = NoveliteCardBeige,
      border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { viewModel.navigateTo(Screen.HOME) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NoveliteDarkBrown)
          }
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Notifications",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
        }

        TextButton(onClick = { viewModel.markAllNotifsRead() }) {
          Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp), tint = NoveliteDarkBrown)
          Spacer(modifier = Modifier.width(4.dp))
          Text("Mark all read", fontSize = 12.sp, color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
        }
      }
    }

    if (notifications.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Text("No notifications yet!", color = NoveliteTextMuted)
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(notifications) { notif ->
          NotificationCard(
            notification = notif,
            onClick = {
              if (notif.actionTargetStoryId != null) {
                viewModel.openStory(notif.actionTargetStoryId)
              } else if (notif.title.contains("Streak", ignoreCase = true) || notif.title.contains("Flame", ignoreCase = true)) {
                viewModel.openStreakDashboard()
              }
            }
          )
        }
      }
    }
  }
}

@Composable
fun NotificationCard(
  notification: NoveliteNotification,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (!notification.isRead) NoveliteCardBeige else NoveliteCreamBg.copy(alpha = 0.6f)
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, if (!notification.isRead) NoveliteDarkBrown.copy(alpha = 0.3f) else NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(
            if (notification.title.contains("Streak", ignoreCase = true)) NoveliteCaramel.copy(alpha = 0.15f)
            else NoveliteDarkBrown.copy(alpha = 0.15f)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (notification.title.contains("Streak", ignoreCase = true)) Icons.Default.LocalFireDepartment else Icons.Default.Notifications,
          contentDescription = null,
          tint = if (notification.title.contains("Streak", ignoreCase = true)) NoveliteCaramel else NoveliteDarkBrown,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = notification.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NoveliteTextPrimary
          )
          Text(text = notification.timestamp, fontSize = 10.sp, color = NoveliteTextMuted)
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = notification.message, fontSize = 12.sp, color = NoveliteTextPrimary.copy(alpha = 0.85f))
      }
    }
  }
}

@Composable
fun AdminScreen(viewModel: NoveliteViewModel) {
  val reports by viewModel.reports.collectAsState()
  val stories by viewModel.stories.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("admin_screen")
  ) {
    // Top Bar
    Surface(
      color = NoveliteCardBeige,
      border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateTo(Screen.PROFILE) }) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NoveliteDarkBrown)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Novelite Admin & Safety Center",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Platform Health Overview
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Platform Metrics & Activity",
              fontFamily = FontFamily.Serif,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              AdminMetricBox(label = "Active Readers", value = "24.8k", modifier = Modifier.weight(1f))
              AdminMetricBox(label = "Live Stories", value = "${stories.size}", modifier = Modifier.weight(1f))
              AdminMetricBox(label = "Active Streaks", value = "18.9k 🔥", modifier = Modifier.weight(1f))
            }
          }
        }
      }

      // Moderation Queue
      item {
        Text(
          text = "Reported Content Queue (${reports.count { it.status == "Pending" }} Pending)",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      }

      items(reports) { rep ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Surface(
                color = if (rep.status == "Pending") NoveliteWarmBrown.copy(alpha = 0.15f) else ShieldEmerald.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "${rep.targetType} • ${rep.status}",
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (rep.status == "Pending") NoveliteDarkBrown else ShieldEmerald
                )
              }

              Text(text = rep.timestamp, fontSize = 11.sp, color = NoveliteTextMuted)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = rep.targetTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NoveliteTextPrimary)
            Text(text = "Reason: ${rep.reason}", fontSize = 12.sp, color = NoveliteCaramel)
            if (rep.details.isNotBlank()) {
              Text(text = "Details: ${rep.details}", fontSize = 12.sp, color = NoveliteTextMuted)
            }
            Text(text = "Reported by @${rep.reporterUsername}", fontSize = 11.sp, color = NoveliteTextMuted)

            if (rep.status == "Pending") {
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Button(
                  onClick = { viewModel.resolveReport(rep.id, "Content Removed") },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                  modifier = Modifier.weight(1f),
                  contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                  Text("Remove Content", fontSize = 11.sp, color = NoveliteWhite)
                }

                OutlinedButton(
                  onClick = { viewModel.resolveReport(rep.id, "Dismissed (No Violation)") },
                  shape = RoundedCornerShape(8.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                  modifier = Modifier.weight(1f),
                  contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                  Text("Dismiss", fontSize = 11.sp, color = NoveliteDarkBrown)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun AdminMetricBox(label: String, value: String, modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier,
    color = NoveliteCreamBg,
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NoveliteDarkBrown)
      Text(text = label, fontSize = 10.sp, color = NoveliteTextMuted)
    }
  }
}
