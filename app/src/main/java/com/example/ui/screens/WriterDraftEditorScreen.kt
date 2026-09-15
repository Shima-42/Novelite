package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.Story
import com.example.ui.NoveliteViewModel
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteButtonBg
import com.example.ui.theme.NoveliteButtonText
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Simple text input screen for writers to draft their stories,
 * utilizing Compose state management for real-time text autosaving.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriterDraftEditorScreen(
  viewModel: NoveliteViewModel,
  storyId: String? = null,
  chapterId: String? = null,
  onBackClick: () -> Unit = {}
) {
  val stories by viewModel.stories.collectAsState()
  val activeStory = remember(stories, storyId) {
    stories.find { it.id == storyId } ?: stories.firstOrNull()
  }

  val activeChapter = remember(activeStory, chapterId) {
    activeStory?.chapters?.find { it.id == chapterId }
      ?: activeStory?.chapters?.firstOrNull()
  }

  // Local State for Editor
  var titleText by remember(activeChapter?.id) {
    mutableStateOf(activeChapter?.title ?: "Chapter 1: The First Inscription")
  }
  var contentText by remember(activeChapter?.id) {
    mutableStateOf(
      activeChapter?.content ?: "The parchment was smooth beneath my fingers. Every word written is a spark in the dark..."
    )
  }

  // Autosave State
  var isAutosaving by remember { mutableStateOf(false) }
  var lastSavedTime by remember { mutableStateOf(SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())) }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  // Real-time metrics
  val wordCount = remember(contentText) {
    contentText.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
  }
  val charCount = remember(contentText) { contentText.length }
  val estimatedReadMinutes = remember(wordCount) {
    maxOf(1, (wordCount / 200))
  }

  // Real-time Autosave Debounce with Compose State and stable dependency keys
  LaunchedEffect(activeStory?.id, activeChapter?.id, titleText, contentText) {
    if (activeStory != null && activeChapter != null) {
      isAutosaving = true
      delay(600) // 600ms debounce
      viewModel.updateChapter(
        storyId = activeStory.id,
        chapterId = activeChapter.id,
        title = titleText,
        content = contentText,
        isDraft = true
      )
      lastSavedTime = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(Date())
      isAutosaving = false
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = activeStory?.title ?: "Writer Studio",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Serif,
              color = NoveliteDarkBrown,
              maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (isAutosaving) {
                CircularProgressIndicator(
                  modifier = Modifier.size(10.dp),
                  color = NoveliteWarmBrown,
                  strokeWidth = 1.5.dp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Autosaving...",
                  fontSize = 11.sp,
                  color = NoveliteWarmBrown
                )
              } else {
                Icon(
                  imageVector = Icons.Default.CloudDone,
                  contentDescription = null,
                  tint = NoveliteSecondaryAccent,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Saved at $lastSavedTime",
                  fontSize = 11.sp,
                  color = NoveliteTextMuted
                )
              }
            }
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("writer_back_button")
          ) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = NoveliteDarkBrown
            )
          }
        },
        actions = {
          Button(
            onClick = {
              if (activeStory != null && activeChapter != null) {
                viewModel.updateChapter(
                  storyId = activeStory.id,
                  chapterId = activeChapter.id,
                  title = titleText,
                  content = contentText,
                  isDraft = false
                )
                scope.launch {
                  snackbarHostState.showSnackbar("Chapter published successfully! ✨")
                }
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = NoveliteButtonBg,
              contentColor = NoveliteButtonText
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier
              .padding(end = 8.dp)
              .height(36.dp)
              .testTag("writer_publish_button")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Publish,
                contentDescription = null,
                tint = NoveliteButtonText,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Publish",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = NoveliteButtonText
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = NoveliteCreamBg
        )
      )
    },
    containerColor = NoveliteCreamBg
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Live Metrics Bar
      Surface(
        color = NoveliteCardBeige,
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
              text = "$wordCount words",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
            Text(
              text = "$charCount chars",
              fontSize = 12.sp,
              color = NoveliteTextMuted
            )
            Text(
              text = "~$estimatedReadMinutes min read",
              fontSize = 12.sp,
              color = NoveliteTextMuted
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = NoveliteSoftAccentBg
          ) {
            Text(
              text = "Draft Autosave Active",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteWarmBrown,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      HorizontalDivider(color = NoveliteBorder, thickness = 1.dp)

      // Chapter Title Input
      OutlinedTextField(
        value = titleText,
        onValueChange = { titleText = it },
        placeholder = {
          Text(
            "Chapter Title...",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            color = NoveliteTextMuted
          )
        },
        textStyle = TextStyle(
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = NoveliteDarkBrown
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .testTag("draft_chapter_title_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Transparent,
          unfocusedBorderColor = Color.Transparent,
          focusedContainerColor = Color.Transparent,
          unfocusedContainerColor = Color.Transparent
        ),
        singleLine = true
      )

      HorizontalDivider(
        color = NoveliteBorder.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      // Story Content Drafting Area
      Box(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f)
          .padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        BasicTextField(
          value = contentText,
          onValueChange = { contentText = it },
          textStyle = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            color = NoveliteTextPrimary
          ),
          cursorBrush = SolidColor(NoveliteDarkBrown),
          modifier = Modifier
            .fillMaxSize()
            .testTag("draft_story_content_input"),
          decorationBox = { innerTextField ->
            if (contentText.isEmpty()) {
              Text(
                text = "Let the quill flow... Write your story here. Real-time autosave is keeping every word secure.",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                color = NoveliteTextMuted
              )
            }
            innerTextField()
          }
        )
      }
    }
  }
}
