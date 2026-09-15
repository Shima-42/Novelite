package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Achievement
import com.example.data.SampleData
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.AnAuthorsReflectionIcon
import com.example.ui.components.AuthorProfile
import com.example.ui.components.DailyGoalSetting
import com.example.ui.components.StoryCard
import com.example.ui.components.VersesIHaveWovenIcon
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(viewModel: NoveliteViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  val selectedAuthorId by viewModel.selectedAuthorId.collectAsState()
  val achievements by viewModel.achievements.collectAsState()
  val stories by viewModel.stories.collectAsState()

  val isViewingSelf = selectedAuthorId == null || selectedAuthorId == currentUser.id

  if (!isViewingSelf && selectedAuthorId != null) {
    AuthorProfile(
      authorId = selectedAuthorId!!,
      viewModel = viewModel,
      onBackClick = { viewModel.openAuthorProfile(currentUser.id) }
    )
    return
  }

  val profileUser = currentUser

  var showEditProfileDialog by remember { mutableStateOf(false) }
  var editDisplayName by remember { mutableStateOf(currentUser.displayName) }
  var editBio by remember { mutableStateOf(currentUser.bio) }
  var editGoalMinutes by remember { mutableStateOf(currentUser.readingGoalMinutes) }
  var editIsPublic by remember { mutableStateOf(currentUser.isActivityPublic) }

  val authorStories = stories.filter { it.authorId == profileUser.id || it.authorName == profileUser.displayName }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("profile_screen"),
    contentPadding = PaddingValues(bottom = 36.dp)
  ) {
    // Profile Hero Header (Novelite warm aesthetic)
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(NoveliteCardBeige)
          .border(androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder))
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Action Icons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (!isViewingSelf) {
            TextButton(onClick = { viewModel.openAuthorProfile(currentUser.id) }) {
              Text("← Back", color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
            }
          } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
              AnAuthorsReflectionIcon(size = 22.dp, isSelected = true)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "An Author's Reflection",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = NoveliteDarkBrown
              )
            }
          }

          Row {
            if (isViewingSelf) {
              IconButton(onClick = { viewModel.navigateTo(Screen.ADMIN) }) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Panel", tint = NoveliteTextMuted)
              }
              IconButton(onClick = { showEditProfileDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = NoveliteTextMuted)
              }
            }
          }
        }

        // Avatar
        Box(
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(NoveliteDarkBrown)
            .border(3.dp, NoveliteCreamBg, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = profileUser.displayName.take(1),
            color = NoveliteCardBeige,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = profileUser.displayName,
          fontFamily = FontFamily.Serif,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )

        Text(
          text = "@${profileUser.username} • Joined ${profileUser.joinedDate}",
          fontSize = 12.sp,
          color = NoveliteTextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = profileUser.bio,
          fontSize = 13.sp,
          color = NoveliteTextPrimary.copy(alpha = 0.85f),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp),
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Bar (Warm Card)
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCreamBg),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            ProfileStat("Followers", "${profileUser.followersCount}")
            ProfileStat("Following", "${profileUser.followingCount}")
            ProfileStat("Streak", "${profileUser.readingStreak}d")
            ProfileStat("Longest", "${profileUser.longestStreak}d")
          }
        }
      }
    }

    // Daily Reading Goal Setting (Saves to local storage and updates profile)
    if (isViewingSelf) {
      item {
        DailyGoalSetting(
          viewModel = viewModel,
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
      }
    }

    // Achievements & Badges Showcase
    item {
      Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Achievements",
            fontFamily = FontFamily.Serif,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          Text(
            text = "${achievements.count { it.isUnlocked }} / ${achievements.size} Unlocked",
            fontSize = 12.sp,
            color = NoveliteWarmBrown,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          achievements.forEach { ach ->
            BadgeItemChip(achievement = ach)
          }
        }
      }
    }

    // Authored Stories Section: Verses I Have Woven
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          VersesIHaveWovenIcon(size = 22.dp, isSelected = true)
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = if (isViewingSelf) "Verses I Have Woven (${authorStories.size})" else "Stories by ${profileUser.displayName} (${authorStories.size})",
              fontFamily = FontFamily.Serif,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextPrimary
            )
            if (isViewingSelf) {
              Text(
                text = "Stories shaped by your imagination.",
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(10.dp))

      if (authorStories.isEmpty()) {
        Text(
          text = "No published stories yet.",
          color = NoveliteTextMuted,
          fontSize = 13.sp,
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
      } else {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(authorStories) { story ->
            StoryCard(
              story = story,
              onClick = { viewModel.openStory(story.id) }
            )
          }
        }
      }
    }

    // Settings & Logout section
    if (isViewingSelf) {
      item {
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Button(
            onClick = { viewModel.navigateTo(Screen.ADMIN) },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = NoveliteCardBeige,
              contentColor = NoveliteDarkBrown
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
          ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Admin & Moderation Center", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = { viewModel.logout() },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("profile_logout_button"),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = NoveliteCreamBg,
              contentColor = NoveliteDarkBrown
            )
          ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp), tint = NoveliteDarkBrown)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out of Novelite", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NoveliteDarkBrown)
          }
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog) {
    AlertDialog(
      onDismissRequest = { showEditProfileDialog = false },
      containerColor = NoveliteCardBeige,
      title = {
        Text(
          text = "Edit Profile & Goals",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = NoveliteTextPrimary
        )
      },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          OutlinedTextField(
            value = editDisplayName,
            onValueChange = { editDisplayName = it },
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = editBio,
            onValueChange = { editBio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text("Daily Reading Goal", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NoveliteTextMuted)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(5, 10, 20, 30).forEach { mins ->
              FilterChip(
                selected = editGoalMinutes == mins,
                onClick = { editGoalMinutes = mins },
                label = { Text("$mins min", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = NoveliteDarkBrown,
                  selectedLabelColor = NoveliteWhite
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Public Activity & Streaks", fontSize = 13.sp, color = NoveliteTextPrimary)
            Switch(
              checked = editIsPublic,
              onCheckedChange = { editIsPublic = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = NoveliteWhite,
                checkedTrackColor = NoveliteDarkBrown
              )
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.updateProfile(editDisplayName, editBio, editIsPublic, editGoalMinutes)
            showEditProfileDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Save Changes", color = NoveliteWhite)
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditProfileDialog = false }) {
          Text("Cancel", color = NoveliteTextMuted)
        }
      }
    )
  }
}

@Composable
fun ProfileStat(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NoveliteTextPrimary)
    Text(text = label, fontSize = 10.sp, color = NoveliteTextMuted)
  }
}

@Composable
fun BadgeItemChip(achievement: Achievement) {
  Surface(
    color = if (achievement.isUnlocked) NoveliteCardBeige else NoveliteCreamBg.copy(alpha = 0.5f),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (achievement.isUnlocked) NoveliteBorder else NoveliteBorder.copy(alpha = 0.4f)
    )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = achievement.iconEmoji, fontSize = 16.sp)
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = achievement.title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (achievement.isUnlocked) NoveliteTextPrimary else NoveliteTextMuted
        )
        Text(
          text = achievement.description,
          fontSize = 10.sp,
          color = NoveliteTextMuted
        )
      }
    }
  }
}
