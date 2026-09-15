package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Chapter
import com.example.data.MediaManager
import com.example.data.MediaPreset
import com.example.data.Story
import com.example.data.StoryStatus
import com.example.ui.NoveliteViewModel
import com.example.ui.components.AnAuthorsReflectionIcon
import com.example.ui.components.LetTheQuillFlowIcon
import com.example.ui.components.NoveliteVideoPlayer
import com.example.ui.components.StoryCoverView
import com.example.ui.components.VersesIHaveWovenIcon
import com.example.ui.components.formatReads
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

enum class StudioView {
  DASHBOARD,
  STORY_DETAILS_MANAGE,
  CHAPTER_EDITOR
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WritingStudioScreen(viewModel: NoveliteViewModel) {
  val context = LocalContext.current
  val currentUser by viewModel.currentUser.collectAsState()
  val stories by viewModel.stories.collectAsState()
  val todayWords by viewModel.todayWordsWritten.collectAsState()

  var studioView by remember { mutableStateOf(StudioView.DASHBOARD) }
  var selectedStoryId by remember { mutableStateOf<String?>(null) }
  var selectedChapterId by remember { mutableStateOf<String?>(null) }

  // New Story Dialog state
  var showNewStoryDialog by remember { mutableStateOf(false) }
  var newStoryTitle by remember { mutableStateOf("") }
  var newStoryDesc by remember { mutableStateOf("") }
  var newStoryGenre by remember { mutableStateOf("Fantasy") }
  var newStoryTags by remember { mutableStateOf("magic, adventure") }
  var newStoryCoverUrl by remember { mutableStateOf<String?>(null) }
  var newStoryVideoUrl by remember { mutableStateOf<String?>(null) }
  var newStoryVideoDuration by remember { mutableStateOf<Int?>(null) }
  var newStoryErrorMessage by remember { mutableStateOf<String?>(null) }

  // Presets dialog state
  var showCoverPresetPicker by remember { mutableStateOf(false) }
  var showVideoPresetPicker by remember { mutableStateOf(false) }
  var presetPickerTargetStoryId by remember { mutableStateOf<String?>(null) } // null = new story dialog, else story id

  // Author Status Dialog state
  var showPostStatusDialog by remember { mutableStateOf(false) }
  var authorStatusText by remember { mutableStateOf("") }
  var selectedStatusStoryId by remember { mutableStateOf<String?>(null) }
  var statusMediaUrl by remember { mutableStateOf<String?>(null) }
  var statusMediaType by remember { mutableStateOf("IMAGE") }

  // Chapter Editor state
  var editorTitle by remember { mutableStateOf("") }
  var editorContent by remember { mutableStateOf("") }
  var editorIsDraft by remember { mutableStateOf(false) }
  var autoSaveMessage by remember { mutableStateOf("All changes saved") }

  val myStories = stories.filter { it.authorId == currentUser.id || it.authorName == currentUser.displayName }
  val activeStory = stories.find { it.id == selectedStoryId }

  val totalMyReads = myStories.sumOf { it.readsCount }
  val totalMyLikes = myStories.sumOf { it.likesCount }

  // Media Pickers for New Story Creation
  val newStoryImageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateImage(context, it)
      if (validation.isValid) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = false)
        newStoryCoverUrl = saved
        newStoryErrorMessage = null
      } else {
        newStoryErrorMessage = validation.errorMessage
      }
    }
  }

  val newStoryVideoLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateVideo(context, it)
      if (validation.isValid) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = true)
        newStoryVideoUrl = saved
        newStoryVideoDuration = validation.durationSeconds ?: 15
        newStoryErrorMessage = null
      } else {
        newStoryErrorMessage = validation.errorMessage
      }
    }
  }

  // Media Pickers for Managing Existing Stories
  val manageStoryImageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateImage(context, it)
      if (validation.isValid && activeStory != null) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = false)
        viewModel.updateStoryMedia(
          storyId = activeStory.id,
          coverImageUrl = saved,
          teaserVideoUrl = activeStory.teaserVideoUrl,
          teaserVideoDurationSec = activeStory.teaserVideoDurationSec
        )
      }
    }
  }

  val manageStoryVideoLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateVideo(context, it)
      if (validation.isValid && activeStory != null) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = true)
        viewModel.updateStoryMedia(
          storyId = activeStory.id,
          coverImageUrl = activeStory.coverImageUrl,
          teaserVideoUrl = saved,
          teaserVideoDurationSec = validation.durationSeconds ?: 15
        )
      }
    }
  }

  // Media Pickers for Author Status Updates
  val statusImageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateImage(context, it)
      if (validation.isValid) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = false)
        statusMediaUrl = saved
        statusMediaType = "IMAGE"
      }
    }
  }

  val statusVideoLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri ->
    uri?.let {
      val validation = MediaManager.validateVideo(context, it)
      if (validation.isValid) {
        val saved = MediaManager.saveUriToAppStorage(context, it, isVideo = true)
        statusMediaUrl = saved
        statusMediaType = "VIDEO"
      }
    }
  }

  when (studioView) {
    StudioView.DASHBOARD -> {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(NoveliteCreamBg)
          .testTag("writing_studio_dashboard")
      ) {
        // Header: Let the Quill Flow
        Surface(
          color = NoveliteCardBeige,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              LetTheQuillFlowIcon(size = 32.dp, isSelected = true)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Let the Quill Flow",
                  fontFamily = FontFamily.Serif,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteTextPrimary
                )
                Text(
                  text = "Where your stories come to life.",
                  fontSize = 12.sp,
                  color = NoveliteTextMuted
                )
              }
            }

            Button(
              onClick = { showNewStoryDialog = true },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = NoveliteDarkBrown,
                contentColor = NoveliteWhite
              ),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.testTag("new_story_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = NoveliteWhite)
              Spacer(modifier = Modifier.width(4.dp))
              Text("New Story", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }

        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Analytics & Streak: An Author's Reflection
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    AnAuthorsReflectionIcon(size = 24.dp, isSelected = true)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text(
                        text = "An Author's Reflection",
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NoveliteTextPrimary
                      )
                      Text(
                        text = "Look back at how your stories are growing.",
                        fontSize = 11.sp,
                        color = NoveliteTextMuted
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Surface(
                    modifier = Modifier.weight(1f),
                    color = NoveliteCreamBg,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
                  ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = NoveliteCaramel, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Author Streak", fontSize = 11.sp, color = NoveliteWarmBrown, fontWeight = FontWeight.Bold)
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text("${currentUser.writingStreak} Days", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = NoveliteTextPrimary)
                    }
                  }

                  Surface(
                    modifier = Modifier.weight(1f),
                    color = NoveliteCreamBg,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
                  ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Create, contentDescription = null, tint = NoveliteDarkBrown, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Words Today", fontSize = 11.sp, color = NoveliteWarmBrown, fontWeight = FontWeight.Bold)
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text("$todayWords words", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = NoveliteTextPrimary)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Total Reads: ${formatReads(totalMyReads)}", fontSize = 12.sp, color = NoveliteTextMuted)
                  Text("Total Likes: $totalMyLikes ❤️", fontSize = 12.sp, color = NoveliteWarmBrown, fontWeight = FontWeight.Bold)
                  Text("Stories: ${myStories.size}", fontSize = 12.sp, color = NoveliteTextMuted)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Post Status Action Button
                OutlinedButton(
                  onClick = { showPostStatusDialog = true },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("post_author_status_button"),
                  shape = RoundedCornerShape(10.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = NoveliteCreamBg,
                    contentColor = NoveliteDarkBrown
                  )
                ) {
                  Icon(Icons.Default.Create, contentDescription = null, modifier = Modifier.size(15.dp), tint = NoveliteDarkBrown)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Post Author Update to Readers", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
              }
            }
          }

          // Stories Section: Verses I Have Woven
          item {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              VersesIHaveWovenIcon(size = 24.dp, isSelected = true)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Verses I Have Woven (${myStories.size})",
                  fontFamily = FontFamily.Serif,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteTextPrimary
                )
                Text(
                  text = "Stories shaped by your imagination.",
                  fontSize = 11.sp,
                  color = NoveliteTextMuted
                )
              }
            }
          }

          if (myStories.isEmpty()) {
            item {
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
                border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text("📚", fontSize = 36.sp)
                  Spacer(modifier = Modifier.height(8.dp))
                  Text("You haven't published any stories yet", fontWeight = FontWeight.Bold, color = NoveliteTextPrimary)
                  Text("Create your first story to share your voice.", fontSize = 12.sp, color = NoveliteTextMuted)
                  Spacer(modifier = Modifier.height(14.dp))
                  Button(
                    onClick = { showNewStoryDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Text("Create New Story", color = NoveliteWhite)
                  }
                }
              }
            }
          } else {
            items(myStories) { story ->
              WriterStoryCard(
                story = story,
                onManage = {
                  selectedStoryId = story.id
                  studioView = StudioView.STORY_DETAILS_MANAGE
                },
                onAddChapter = {
                  selectedStoryId = story.id
                  editorTitle = "Chapter ${story.chapters.size + 1}"
                  editorContent = ""
                  editorIsDraft = false
                  selectedChapterId = null
                  studioView = StudioView.CHAPTER_EDITOR
                }
              )
            }
          }
        }
      }
    }

    StudioView.STORY_DETAILS_MANAGE -> {
      if (activeStory == null) {
        studioView = StudioView.DASHBOARD
      } else {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .background(NoveliteCreamBg)
        ) {
          // Top Nav
          Surface(
            color = NoveliteCardBeige,
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(onClick = { studioView = StudioView.DASHBOARD }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NoveliteDarkBrown)
              }
              Spacer(modifier = Modifier.width(4.dp))
              Column {
                Text(
                  text = activeStory.title,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = NoveliteTextPrimary,
                  maxLines = 1
                )
                Text(
                  text = "${activeStory.chapters.size} Chapters • ${activeStory.status.name}",
                  fontSize = 11.sp,
                  color = NoveliteTextMuted
                )
              }
            }
          }

          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Status Selector Card
            item {
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
                border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text("Story Status", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = NoveliteTextPrimary)
                  Spacer(modifier = Modifier.height(8.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    StoryStatus.entries.forEach { st ->
                      val isSelected = activeStory.status == st
                      FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateStoryStatus(activeStory.id, st) },
                        label = { Text(st.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                          selectedContainerColor = NoveliteDarkBrown,
                          selectedLabelColor = NoveliteWhite
                        )
                      )
                    }
                  }
                }
              }
            }

            // 1. STORY COVER MEDIA MANAGEMENT CARD
            item {
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("manage_story_cover_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
                border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Image, contentDescription = null, tint = NoveliteDarkBrown, modifier = Modifier.size(18.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "Story Cover",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = NoveliteDarkBrown
                      )
                    }

                    if (activeStory.coverImageUrl != null) {
                      Surface(
                        color = NoveliteWarmBrown.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                      ) {
                        Text(
                          text = "Custom Upload",
                          fontSize = 9.sp,
                          color = NoveliteDarkBrown,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    // Cover Preview Box
                    StoryCoverView(
                      story = activeStory,
                      modifier = Modifier
                        .width(90.dp)
                        .height(125.dp),
                      shape = RoundedCornerShape(10.dp),
                      showTeaserBadge = false
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                      modifier = Modifier.weight(1f),
                      verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Text(
                        text = if (activeStory.coverImageUrl != null) "Uploaded custom cover active across Novelite." else "Using default typographic cover.",
                        fontSize = 11.sp,
                        color = NoveliteTextMuted,
                        lineHeight = 15.sp
                      )

                      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                          onClick = { manageStoryImageLauncher.launch("image/*") },
                          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                          shape = RoundedCornerShape(8.dp),
                          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                          Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
                          Spacer(modifier = Modifier.width(4.dp))
                          Text(
                            text = if (activeStory.coverImageUrl != null) "Replace" else "Upload",
                            fontSize = 11.sp,
                            color = NoveliteWhite
                          )
                        }

                        OutlinedButton(
                          onClick = {
                            presetPickerTargetStoryId = activeStory.id
                            showCoverPresetPicker = true
                          },
                          shape = RoundedCornerShape(8.dp),
                          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                          Icon(Icons.Default.Collections, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteDarkBrown)
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("Presets", fontSize = 11.sp, color = NoveliteDarkBrown)
                        }
                      }

                      if (activeStory.coverImageUrl != null) {
                        TextButton(
                          onClick = {
                            viewModel.updateStoryMedia(
                              storyId = activeStory.id,
                              coverImageUrl = null,
                              teaserVideoUrl = activeStory.teaserVideoUrl,
                              teaserVideoDurationSec = activeStory.teaserVideoDurationSec
                            )
                          },
                          contentPadding = PaddingValues(0.dp)
                        ) {
                          Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(13.dp), tint = NoveliteWarmBrown)
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("Remove Custom Cover", fontSize = 11.sp, color = NoveliteWarmBrown)
                        }
                      }
                    }
                  }
                }
              }
            }

            // 2. SHORT VIDEO TEASER MEDIA MANAGEMENT CARD
            item {
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("manage_story_teaser_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
                border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Videocam, contentDescription = null, tint = NoveliteDarkBrown, modifier = Modifier.size(18.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "Short Video Teaser (Optional)",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = NoveliteDarkBrown
                      )
                    }

                    if (!activeStory.teaserVideoUrl.isNullOrBlank()) {
                      Surface(
                        color = NoveliteCaramel,
                        shape = RoundedCornerShape(6.dp)
                      ) {
                        Text(
                          text = "${activeStory.teaserVideoDurationSec ?: 15}s Teaser Active",
                          fontSize = 9.sp,
                          color = NoveliteDarkBrown,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))

                  if (!activeStory.teaserVideoUrl.isNullOrBlank()) {
                    // Video Player Preview
                    NoveliteVideoPlayer(
                      videoUrl = activeStory.teaserVideoUrl,
                      title = "${activeStory.title} Teaser",
                      autoPlay = false,
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                          onClick = { manageStoryVideoLauncher.launch("video/*") },
                          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                          shape = RoundedCornerShape(8.dp),
                          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                          Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("Replace Video", fontSize = 11.sp, color = NoveliteWhite)
                        }

                        OutlinedButton(
                          onClick = {
                            presetPickerTargetStoryId = activeStory.id
                            showVideoPresetPicker = true
                          },
                          shape = RoundedCornerShape(8.dp),
                          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                          Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteDarkBrown)
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("Presets", fontSize = 11.sp, color = NoveliteDarkBrown)
                        }
                      }

                      IconButton(
                        onClick = {
                          viewModel.updateStoryMedia(
                            storyId = activeStory.id,
                            coverImageUrl = activeStory.coverImageUrl,
                            teaserVideoUrl = null,
                            teaserVideoDurationSec = null
                          )
                        }
                      ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete video", tint = NoveliteWarmBrown)
                      }
                    }
                  } else {
                    // Empty state invitation for video teasers
                    Surface(
                      color = NoveliteCreamBg,
                      shape = RoundedCornerShape(10.dp),
                      border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                        Text(
                          text = "Bring your story to life with a cinematic 15-60s trailer.",
                          fontSize = 12.sp,
                          color = NoveliteDarkBrown,
                          fontWeight = FontWeight.Medium
                        )
                        Text(
                          text = "Max 50MB • MP4, WEBM • Up to 60s",
                          fontSize = 10.sp,
                          color = NoveliteTextMuted
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                          Button(
                            onClick = { manageStoryVideoLauncher.launch("video/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                          ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Upload Short Video", fontSize = 11.sp, color = NoveliteWhite)
                          }

                          OutlinedButton(
                            onClick = {
                              presetPickerTargetStoryId = activeStory.id
                              showVideoPresetPicker = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                          ) {
                            Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteDarkBrown)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Choose Teaser Preset", fontSize = 11.sp, color = NoveliteDarkBrown)
                          }
                        }
                      }
                    }
                  }
                }
              }
            }

            // Chapters List Header
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Chapters",
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.titleMedium,
                  color = NoveliteTextPrimary
                )
                Button(
                  onClick = {
                    editorTitle = "Chapter ${activeStory.chapters.size + 1}"
                    editorContent = ""
                    editorIsDraft = false
                    selectedChapterId = null
                    studioView = StudioView.CHAPTER_EDITOR
                  },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Add Chapter", fontSize = 11.sp, color = NoveliteWhite)
                }
              }
            }

            if (activeStory.chapters.isEmpty()) {
              item {
                Text("No chapters yet. Click '+ Add Chapter' to start writing!", color = NoveliteTextMuted, fontSize = 12.sp)
              }
            } else {
              items(activeStory.chapters) { ch ->
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
                  border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(
                        text = "Ch ${ch.chapterNumber}: ${ch.title}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = NoveliteTextPrimary
                      )
                      Text(
                        text = "${ch.wordsCount} words • ${if (ch.isDraft) "Draft" else "Published"}",
                        fontSize = 11.sp,
                        color = NoveliteTextMuted
                      )
                    }

                    Row {
                      IconButton(onClick = {
                        selectedChapterId = ch.id
                        editorTitle = ch.title
                        editorContent = ch.content
                        editorIsDraft = ch.isDraft
                        studioView = StudioView.CHAPTER_EDITOR
                      }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NoveliteWarmBrown)
                      }

                      IconButton(onClick = {
                        viewModel.repository.deleteChapter(activeStory.id, ch.id)
                      }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NoveliteTextMuted)
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    StudioView.CHAPTER_EDITOR -> {
      val wordCount = remember(editorContent) {
        editorContent.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
      }
      val charCount = editorContent.length
      val estReadMins = maxOf(1, wordCount / 200)

      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(NoveliteCreamBg)
          .testTag("chapter_editor_screen")
      ) {
        // Editor Action Bar
        Surface(
          color = NoveliteCardBeige,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(onClick = { studioView = StudioView.STORY_DETAILS_MANAGE }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NoveliteDarkBrown)
              }
              Column {
                Text("Chapter Editor", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NoveliteTextPrimary)
                Text(autoSaveMessage, fontSize = 10.sp, color = NoveliteWarmBrown)
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
              ) {
                Text(if (editorIsDraft) "Draft" else "Publish", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NoveliteTextPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                  checked = !editorIsDraft,
                  onCheckedChange = { editorIsDraft = !it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = NoveliteWhite,
                    checkedTrackColor = NoveliteDarkBrown
                  )
                )
              }

              Button(
                onClick = {
                  if (activeStory != null) {
                    if (selectedChapterId != null) {
                      viewModel.updateChapter(activeStory.id, selectedChapterId!!, editorTitle, editorContent, editorIsDraft)
                    } else {
                      viewModel.addChapter(activeStory.id, editorTitle, editorContent, editorIsDraft)
                    }
                    autoSaveMessage = "Saved successfully!"
                    studioView = StudioView.STORY_DETAILS_MANAGE
                  }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                modifier = Modifier.testTag("editor_save_button")
              ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save", color = NoveliteWhite)
              }
            }
          }
        }

        // Live Editor Stats Bar
        Surface(
          color = NoveliteCardBeige,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🔥 Words: $wordCount", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NoveliteCaramel)
            Text(text = "Chars: $charCount", fontSize = 11.sp, color = NoveliteTextMuted)
            Text(text = "~$estReadMins min read", fontSize = 11.sp, color = NoveliteTextMuted)
          }
        }

        // Title and Content Inputs
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
          OutlinedTextField(
            value = editorTitle,
            onValueChange = {
              editorTitle = it
              autoSaveMessage = "Editing..."
            },
            placeholder = { Text("Chapter Title (e.g. The Call of the Spirits)", color = NoveliteTextMuted) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("editor_title_input"),
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = NoveliteTextPrimary),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NoveliteCardBeige,
              unfocusedContainerColor = NoveliteCardBeige,
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = editorContent,
            onValueChange = {
              editorContent = it
              autoSaveMessage = "Editing..."
            },
            placeholder = { Text("Start writing your chapter here... Your daily writing streak grows with every word!", color = NoveliteTextMuted) },
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .testTag("editor_content_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NoveliteCardBeige,
              unfocusedContainerColor = NoveliteCardBeige,
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )
        }
      }
    }
  }

  // Create Story Modal Dialog
  if (showNewStoryDialog) {
    AlertDialog(
      onDismissRequest = { showNewStoryDialog = false },
      containerColor = NoveliteCardBeige,
      title = {
        Text(
          text = "Create New Story",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          OutlinedTextField(
            value = newStoryTitle,
            onValueChange = { newStoryTitle = it },
            label = { Text("Story Title") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("new_story_title_input"),
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
            value = newStoryDesc,
            onValueChange = { newStoryDesc = it },
            label = { Text("Synopsis / Logline") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text("Genre", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NoveliteTextMuted)
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            listOf("African Stories", "Fantasy", "Romance", "Mystery", "Sci-Fi").forEach { g ->
              FilterChip(
                selected = newStoryGenre == g,
                onClick = { newStoryGenre = g },
                label = { Text(g, fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = NoveliteDarkBrown,
                  selectedLabelColor = NoveliteWhite
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = newStoryTags,
            onValueChange = { newStoryTags = it },
            label = { Text("Tags (comma separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(14.dp))

          // --- 1. Story Cover Upload Section ---
          Text(
            text = "Story Cover (Image)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteDarkBrown
          )
          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (newStoryCoverUrl != null) {
                Box(
                  modifier = Modifier
                    .size(54.dp, 76.dp)
                    .clip(RoundedCornerShape(8.dp))
                ) {
                  AsyncImage(
                    model = newStoryCoverUrl,
                    contentDescription = "Cover preview",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                  )
                }
              } else {
                Box(
                  modifier = Modifier
                    .size(54.dp, 76.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NoveliteDarkBrown),
                  contentAlignment = Alignment.Center
                ) {
                  Text("📚", fontSize = 18.sp)
                }
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = if (newStoryCoverUrl != null) "Cover Selected ✓" else "No custom cover selected",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteDarkBrown
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Button(
                    onClick = { newStoryImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(if (newStoryCoverUrl != null) "Change" else "Upload", fontSize = 10.sp, color = NoveliteWhite)
                  }

                  OutlinedButton(
                    onClick = {
                      presetPickerTargetStoryId = null
                      showCoverPresetPicker = true
                    },
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text("Presets", fontSize = 10.sp, color = NoveliteDarkBrown)
                  }

                  if (newStoryCoverUrl != null) {
                    IconButton(
                      onClick = { newStoryCoverUrl = null },
                      modifier = Modifier.size(24.dp)
                    ) {
                      Icon(Icons.Default.Close, contentDescription = "Clear cover", tint = NoveliteWarmBrown, modifier = Modifier.size(14.dp))
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // --- 2. Short Video Teaser Section ---
          Text(
            text = "Short Video Teaser (Optional)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteDarkBrown
          )
          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              if (newStoryVideoUrl != null) {
                NoveliteVideoPlayer(
                  videoUrl = newStoryVideoUrl!!,
                  title = "Teaser Preview",
                  autoPlay = false,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("🎬 Attached (${newStoryVideoDuration ?: 15}s)", fontSize = 11.sp, color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
                  TextButton(
                    onClick = {
                      newStoryVideoUrl = null
                      newStoryVideoDuration = null
                    },
                    contentPadding = PaddingValues(0.dp)
                  ) {
                    Text("Remove", fontSize = 11.sp, color = NoveliteWarmBrown)
                  }
                }
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(NoveliteCardBeige),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = NoveliteDarkBrown, modifier = Modifier.size(20.dp))
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text("Add 15-60s cinematic trailer", fontSize = 11.sp, color = NoveliteDarkBrown)
                    Text("MP4, WEBM • Max 50MB", fontSize = 9.sp, color = NoveliteTextMuted)
                  }
                  Button(
                    onClick = { newStoryVideoLauncher.launch("video/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text("Upload", fontSize = 10.sp, color = NoveliteWhite)
                  }
                  Spacer(modifier = Modifier.width(4.dp))
                  OutlinedButton(
                    onClick = {
                      presetPickerTargetStoryId = null
                      showVideoPresetPicker = true
                    },
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text("Presets", fontSize = 10.sp, color = NoveliteDarkBrown)
                  }
                }
              }
            }
          }

          if (newStoryErrorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = newStoryErrorMessage!!,
              color = Color.Red,
              fontSize = 11.sp
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newStoryTitle.isNotBlank()) {
              val tagsList = newStoryTags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
              val created = viewModel.createStory(
                title = newStoryTitle,
                desc = newStoryDesc.ifBlank { "An exciting tale waiting to unfold on Novelite." },
                genre = newStoryGenre,
                tags = tagsList,
                status = StoryStatus.ONGOING,
                coverImageUrl = newStoryCoverUrl,
                teaserVideoUrl = newStoryVideoUrl,
                teaserVideoDurationSec = newStoryVideoDuration
              )
              showNewStoryDialog = false
              newStoryTitle = ""
              newStoryDesc = ""
              newStoryCoverUrl = null
              newStoryVideoUrl = null
              newStoryVideoDuration = null
              selectedStoryId = created.id
              studioView = StudioView.STORY_DETAILS_MANAGE
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Create Story 🔥", color = NoveliteWhite)
        }
      },
      dismissButton = {
        TextButton(onClick = { showNewStoryDialog = false }) { Text("Cancel", color = NoveliteTextMuted) }
      }
    )
  }

  // Post Author Status / Whisper Dialog
  if (showPostStatusDialog) {
    AlertDialog(
      onDismissRequest = { showPostStatusDialog = false },
      containerColor = NoveliteCardBeige,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("✍️", fontSize = 20.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Post Author Update",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
        }
      },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "Notify your readers about upcoming chapters, writing milestones, or thoughts.",
            fontSize = 12.sp,
            color = NoveliteTextMuted,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = authorStatusText,
            onValueChange = { authorStatusText = it },
            label = { Text("What's on your mind?") },
            placeholder = { Text("e.g. Just completed Chapter 4! Releasing tomorrow...") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("author_status_input"),
            minLines = 3,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )

          // Attached Media Preview in Status
          if (!statusMediaUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NoveliteDarkBrown)
            ) {
              AsyncImage(
                model = statusMediaUrl,
                contentDescription = "Status media preview",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
              )
              IconButton(
                onClick = { statusMediaUrl = null },
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(4.dp)
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(NoveliteDarkBrown.copy(alpha = 0.8f))
              ) {
                Icon(Icons.Default.Close, contentDescription = "Remove", tint = NoveliteWhite, modifier = Modifier.size(16.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Media Attachment buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { statusImageLauncher.launch("image/*") },
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteDarkBrown)
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Photo", fontSize = 11.sp, color = NoveliteDarkBrown)
            }

            OutlinedButton(
              onClick = { statusVideoLauncher.launch("video/*") },
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteDarkBrown)
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Video", fontSize = 11.sp, color = NoveliteDarkBrown)
            }
          }

          if (myStories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Tag a Story (Optional)",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              myStories.forEach { st ->
                val isSelected = selectedStatusStoryId == st.id
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    selectedStatusStoryId = if (isSelected) null else st.id
                  },
                  label = { Text(st.title, fontSize = 11.sp, maxLines = 1) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NoveliteDarkBrown,
                    selectedLabelColor = NoveliteWhite
                  )
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (authorStatusText.isNotBlank()) {
              val linkedStory = myStories.find { it.id == selectedStatusStoryId }
              viewModel.postAuthorStatus(
                statusText = authorStatusText.trim(),
                storyId = linkedStory?.id,
                storyTitle = linkedStory?.title,
                mediaUrl = statusMediaUrl,
                mediaType = statusMediaType
              )
              authorStatusText = ""
              statusMediaUrl = null
              selectedStatusStoryId = null
              showPostStatusDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Publish Update 🚀", color = NoveliteWhite, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showPostStatusDialog = false }) {
          Text("Cancel", color = NoveliteTextMuted)
        }
      }
    )
  }

  // Cover Preset Chooser Dialog
  if (showCoverPresetPicker) {
    CoverPresetDialog(
      onSelect = { selectedUrl ->
        if (presetPickerTargetStoryId == null) {
          newStoryCoverUrl = selectedUrl
        } else {
          viewModel.updateStoryMedia(
            storyId = presetPickerTargetStoryId!!,
            coverImageUrl = selectedUrl,
            teaserVideoUrl = activeStory?.teaserVideoUrl,
            teaserVideoDurationSec = activeStory?.teaserVideoDurationSec
          )
        }
        showCoverPresetPicker = false
      },
      onDismiss = { showCoverPresetPicker = false }
    )
  }

  // Video Preset Chooser Dialog
  if (showVideoPresetPicker) {
    VideoPresetDialog(
      onSelect = { preset ->
        if (presetPickerTargetStoryId == null) {
          newStoryVideoUrl = preset.url
          newStoryVideoDuration = preset.durationSec ?: 15
        } else {
          viewModel.updateStoryMedia(
            storyId = presetPickerTargetStoryId!!,
            coverImageUrl = activeStory?.coverImageUrl,
            teaserVideoUrl = preset.url,
            teaserVideoDurationSec = preset.durationSec ?: 15
          )
        }
        showVideoPresetPicker = false
      },
      onDismiss = { showVideoPresetPicker = false }
    )
  }
}

@Composable
fun CoverPresetDialog(
  onSelect: (String) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = NoveliteCardBeige,
    title = {
      Text(
        text = "Curated Cover Presets",
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        color = NoveliteDarkBrown
      )
    },
    text = {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.height(340.dp)
      ) {
        items(MediaManager.PRESET_COVERS) { preset ->
          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelect(preset.url) }
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              AsyncImage(
                model = preset.url,
                contentDescription = preset.title,
                modifier = Modifier
                  .size(54.dp, 76.dp)
                  .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
              )

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = preset.genre.uppercase(),
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = NoveliteWarmBrown
                )
                Text(
                  text = preset.title,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = NoveliteDarkBrown
                )
                Text(
                  text = preset.description,
                  fontSize = 10.sp,
                  color = NoveliteTextMuted,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", color = NoveliteDarkBrown)
      }
    }
  )
}

@Composable
fun VideoPresetDialog(
  onSelect: (MediaPreset) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = NoveliteCardBeige,
    title = {
      Text(
        text = "Curated Video Teasers",
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        color = NoveliteDarkBrown
      )
    },
    text = {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.height(340.dp)
      ) {
        items(MediaManager.PRESET_VIDEOS) { preset ->
          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelect(preset) }
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(60.dp, 45.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(NoveliteDarkBrown),
                contentAlignment = Alignment.Center
              ) {
                Text("🎬", fontSize = 18.sp)
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = preset.genre.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = NoveliteWarmBrown
                  )
                  Text(
                    text = "${preset.durationSec}s HD",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = NoveliteDarkBrown
                  )
                }
                Text(
                  text = preset.title,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = NoveliteDarkBrown
                )
                Text(
                  text = preset.description,
                  fontSize = 10.sp,
                  color = NoveliteTextMuted,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", color = NoveliteDarkBrown)
      }
    }
  )
}

@Composable
fun WriterStoryCard(
  story: Story,
  onManage: () -> Unit,
  onAddChapter: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onManage() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      StoryCoverView(
        story = story,
        modifier = Modifier
          .size(54.dp, 76.dp),
        shape = RoundedCornerShape(10.dp),
        showTeaserBadge = true
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(6.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
          ) {
            Text(
              text = story.genre,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              fontSize = 10.sp,
              color = NoveliteDarkBrown,
              fontWeight = FontWeight.Bold
            )
          }

          Surface(
            color = NoveliteCaramel.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = story.status.name,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              fontSize = 10.sp,
              color = NoveliteWarmBrown,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = story.title,
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "${story.chapters.size} Chapters • ${formatReads(story.readsCount)} Reads • ${story.likesCount} Likes",
          fontSize = 11.sp,
          color = NoveliteTextMuted
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        IconButton(onClick = onAddChapter) {
          Icon(Icons.Default.Add, contentDescription = "Add chapter", tint = NoveliteCaramel)
        }
        IconButton(onClick = onManage) {
          Icon(Icons.Default.Edit, contentDescription = "Manage story", tint = NoveliteDarkBrown)
        }
      }
    }
  }
}
