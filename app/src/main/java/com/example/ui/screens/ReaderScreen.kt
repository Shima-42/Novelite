package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.Comment
import com.example.data.Story
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.BetweenTheLinesIcon
import com.example.ui.components.ChapterCommentSection
import com.example.ui.components.FootprintsOnPageIcon
import kotlinx.coroutines.launch
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
import com.example.ui.theme.ReaderBeigeBg
import com.example.ui.theme.ReaderBeigeText
import com.example.ui.theme.ReaderCreamBg
import com.example.ui.theme.ReaderCreamText
import com.example.ui.theme.ReaderDarkBg
import com.example.ui.theme.ReaderDarkText
import com.example.ui.theme.ReaderSepiaBg
import com.example.ui.theme.ReaderSepiaText
import com.example.ui.theme.ReaderTwilightBg
import com.example.ui.theme.ReaderTwilightText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(viewModel: NoveliteViewModel) {
  StoryReader(viewModel = viewModel)
}

/**
 * ReadingView screen composable alias for backward compatibility.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingView(viewModel: NoveliteViewModel) {
  StoryReader(viewModel = viewModel)
}

/**
 * StoryReader composable that provides a distraction-free immersive reading mode
 * with customizable font size and background theme toggles.
 * Also features a horizontal progress bar at the top indicating scroll progress
 * through the current chapter, and a heart-shaped 'Like' button with spring animation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryReader(
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier
) {
  val storyId by viewModel.selectedStoryId.collectAsState()
  val chapterId by viewModel.selectedChapterId.collectAsState()
  val stories by viewModel.stories.collectAsState()
  val comments by viewModel.comments.collectAsState()
  val readerConfig by viewModel.readerConfig.collectAsState()
  val todayMinutes by viewModel.todayMinutesRead.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()

  val story = stories.find { it.id == storyId } ?: stories.firstOrNull()
  val currentChapter = story?.chapters?.find { it.id == chapterId } ?: story?.chapters?.firstOrNull()

  var showSettingsSheet by remember { mutableStateOf(false) }
  var showChapterListSheet by remember { mutableStateOf(false) }
  var showInlineCommentModal by remember { mutableStateOf(false) }
  var selectedQuoteForComment by remember { mutableStateOf<String?>(null) }
  var inlineCommentText by remember { mutableStateOf("") }

  var isControlsVisible by remember { mutableStateOf(true) }

  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  // Track chapter reading progress dynamically
  val chapterProgress by remember {
    derivedStateOf {
      val totalItems = listState.layoutInfo.totalItemsCount
      if (totalItems > 1) {
        (listState.firstVisibleItemIndex.toFloat() / (totalItems - 1)).coerceIn(0f, 1f)
      } else {
        0f
      }
    }
  }

  // Persist reading progress when chapter finishes or when leaving screen
  DisposableEffect(story?.id, currentChapter?.id) {
    onDispose {
      val totalItems = listState.layoutInfo.totalItemsCount
      if (story != null && currentChapter != null && totalItems > 1) {
        val frac = (listState.firstVisibleItemIndex.toFloat() / (totalItems - 1)).coerceIn(0f, 1f)
        viewModel.repository.updateReadingProgress(story.id, currentChapter.id, frac)
      }
    }
  }

  if (story == null || currentChapter == null) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(NoveliteCreamBg),
      contentAlignment = Alignment.Center
    ) {
      Text("Chapter not found.", color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
    }
    return
  }

  // Theme background and high-contrast text pairing
  val (bgColor, textColor) = when (readerConfig.themeMode) {
    "Cream" -> Pair(ReaderCreamBg, ReaderCreamText)
    "Beige", "Light" -> Pair(ReaderBeigeBg, ReaderBeigeText)
    "Sepia" -> Pair(ReaderSepiaBg, ReaderSepiaText)
    "Dark" -> Pair(ReaderDarkBg, ReaderDarkText)
    else -> Pair(ReaderTwilightBg, ReaderTwilightText)
  }

  val selectedFontFamily = when (readerConfig.fontFamilyName) {
    "SansSerif" -> FontFamily.SansSerif
    "Monospace" -> FontFamily.Monospace
    "Cursive" -> FontFamily.Cursive
    else -> FontFamily.Serif
  }

  val chapterComments = comments.filter { it.storyId == story.id && it.chapterId == currentChapter.id }
  val chapterIndex = story.chapters.indexOf(currentChapter)
  val hasPrevious = chapterIndex > 0
  val hasNext = chapterIndex < story.chapters.size - 1

  // Split chapter content into readable paragraphs
  val paragraphs = currentChapter.content.split("\n\n").filter { it.isNotBlank() }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(bgColor)
      .testTag("reading_view_screen")
  ) {
    // Content LazyColumn
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures(
            onTap = {
              isControlsVisible = !isControlsVisible
            }
          )
        },
      contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 92.dp, bottom = 120.dp)
    ) {
      // Story & Chapter Title Header
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = story.title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteCaramel,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = currentChapter.title,
            fontFamily = selectedFontFamily,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Chapter ${currentChapter.chapterNumber} • by ${story.authorName} • ~${maxOf(1, currentChapter.wordsCount / 200)} min read",
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.75f)
          )
          Spacer(modifier = Modifier.height(18.dp))
          HorizontalDivider(color = textColor.copy(alpha = 0.18f), thickness = 1.dp)
        }
      }

      // Paragraphs with inline commentary affordance
      items(paragraphs) { paragraph ->
        val inlineQuotesForParagraph = chapterComments.filter { it.inlineQuote != null && paragraph.contains(it.inlineQuote) }

        Column(modifier = Modifier.padding(bottom = (readerConfig.fontSizeSp * 0.85).dp)) {
          Text(
            text = paragraph,
            color = textColor,
            fontSize = readerConfig.fontSizeSp.sp,
            fontFamily = selectedFontFamily,
            lineHeight = (readerConfig.fontSizeSp * readerConfig.lineSpacingMultiplier).sp,
            modifier = Modifier
              .fillMaxWidth()
              .pointerInput(paragraph) {
                detectTapGestures(
                  onTap = {
                    isControlsVisible = !isControlsVisible
                  },
                  onLongPress = {
                    selectedQuoteForComment = paragraph.take(120) + (if (paragraph.length > 120) "..." else "")
                    showInlineCommentModal = true
                  }
                )
              }
          )

          // Show inline comment badge if notes exist for this section
          if (inlineQuotesForParagraph.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteCardBeige.copy(alpha = 0.15f) else NoveliteDarkBrown.copy(alpha = 0.08f))
                .border(1.dp, NoveliteBorder, RoundedCornerShape(12.dp))
                .clickable {
                  selectedQuoteForComment = inlineQuotesForParagraph.firstOrNull()?.inlineQuote ?: paragraph.take(100)
                  coroutineScope.launch {
                    listState.animateScrollToItem(paragraphs.size + 1)
                  }
                }
                .padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteWhite else NoveliteDarkBrown,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "${inlineQuotesForParagraph.size} reader note${if (inlineQuotesForParagraph.size > 1) "s" else ""}",
                color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteWhite else NoveliteDarkBrown,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // End of Chapter Action Section
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          HorizontalDivider(color = textColor.copy(alpha = 0.18f), thickness = 1.dp)
          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "End of Chapter ${currentChapter.chapterNumber}",
            fontFamily = selectedFontFamily,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Chapter Engagement Card with Heart-shaped Like Button & Spring Animation
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteCardBeige.copy(alpha = 0.12f) else NoveliteCardBeige,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg.copy(alpha = 0.3f) else NoveliteBorder),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("chapter_engagement_card")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "Enjoyed this chapter?",
                fontFamily = selectedFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = textColor
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Leave a like to support ${story.authorName} and spark the author's creativity!",
                fontSize = 11.sp,
                color = textColor.copy(alpha = 0.75f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
              Spacer(modifier = Modifier.height(12.dp))
              ChapterLikeButton(
                isLiked = currentChapter.isLiked,
                likesCount = currentChapter.likesCount,
                onLikeClick = { viewModel.toggleLikeChapter(story.id, currentChapter.id) },
                tint = textColor,
                iconSize = 26.dp,
                showCount = true,
                modifier = Modifier.testTag("chapter_end_like_button")
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Chapter navigation buttons with high-contrast text & containers
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            if (hasPrevious) {
              OutlinedButton(
                onClick = {
                  val prevId = story.chapters[chapterIndex - 1].id
                  viewModel.openReader(story.id, prevId)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteBorder)
              ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(14.dp), tint = textColor)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous", color = textColor, fontWeight = FontWeight.SemiBold)
              }
            }

            if (hasNext) {
              Button(
                onClick = {
                  val nextId = story.chapters[chapterIndex + 1].id
                  viewModel.openReader(story.id, nextId)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSecondaryAccent else NoveliteDarkBrown
                )
              ) {
                Text("Next Chapter", color = NoveliteWhite, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
              }
            } else {
              Button(
                onClick = {
                  viewModel.repository.updateReadingProgress(story.id, currentChapter.id, 1.0f)
                  viewModel.navigateTo(Screen.STORY_DETAIL)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NoveliteSecondaryAccent)
              ) {
                Text("Finished Story 🎉", color = NoveliteWhite, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }

      // Integrated Chapter Comment Section Component
      item {
        ChapterCommentSection(
          viewModel = viewModel,
          storyId = story.id,
          chapterId = currentChapter.id,
          chapterTitle = currentChapter.title,
          isDarkTheme = readerConfig.themeMode in listOf("Dark", "Twilight"),
          initialAttachedQuote = selectedQuoteForComment,
          modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )
      }
    }

    // Top Navigation & Progress Bar (Toggleable on Tap)
    AnimatedVisibility(
      visible = isControlsVisible,
      enter = fadeIn() + slideInVertically(),
      exit = fadeOut() + slideOutVertically(),
      modifier = Modifier.align(Alignment.TopCenter)
    ) {
      Surface(
        color = bgColor.copy(alpha = 0.96f),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              IconButton(onClick = { viewModel.navigateTo(Screen.STORY_DETAIL) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
              }
              BetweenTheLinesIcon(size = 22.dp, isSelected = true)
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = story.title,
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = textColor,
                  maxLines = 1
                )
                Text(
                  text = "${currentChapter.title} • Reading Mode",
                  fontSize = 10.sp,
                  color = textColor.copy(alpha = 0.70f),
                  maxLines = 1
                )
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              // Heart-shaped Like Button with tactile spring animation
              ChapterLikeButton(
                isLiked = currentChapter.isLiked,
                likesCount = currentChapter.likesCount,
                onLikeClick = { viewModel.toggleLikeChapter(story.id, currentChapter.id) },
                tint = textColor,
                showCount = false,
                iconSize = 20.dp,
                modifier = Modifier.testTag("story_reader_like_button")
              )

              // Distraction-Free Immersive Mode Toggle
              IconButton(
                onClick = { isControlsVisible = false },
                modifier = Modifier.testTag("distraction_free_toggle")
              ) {
                Icon(
                  imageVector = Icons.Default.Fullscreen,
                  contentDescription = "Distraction-Free Immersive Mode",
                  tint = textColor
                )
              }

              // Jump to Chapter Comments button with badge
              IconButton(
                onClick = {
                  coroutineScope.launch {
                    listState.animateScrollToItem(paragraphs.size + 1)
                  }
                },
                modifier = Modifier.testTag("reader_comments_top_button")
              ) {
                BadgedBox(
                  badge = {
                    if (chapterComments.isNotEmpty()) {
                      Badge(
                        containerColor = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteCaramel
                      ) {
                        Text(
                          text = "${chapterComments.size}",
                          color = NoveliteWhite,
                          fontSize = 9.sp
                        )
                      }
                    }
                  }
                ) {
                  Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Chapter Comments (${chapterComments.size})",
                    tint = textColor
                  )
                }
              }

              // Quick +1 Min simulator button
              IconButton(
                onClick = { viewModel.addReadingMinute(1) },
                modifier = Modifier.testTag("reader_timer_button")
              ) {
                Icon(
                  imageVector = Icons.Outlined.Timer,
                  contentDescription = "Add 1 min reading progress",
                  tint = NoveliteCaramel
                )
              }

              IconButton(onClick = { showChapterListSheet = true }) {
                Icon(Icons.Default.List, contentDescription = "Chapters", tint = textColor)
              }

              IconButton(
                onClick = { showSettingsSheet = true },
                modifier = Modifier.testTag("reader_settings_button")
              ) {
                Icon(Icons.Default.Settings, contentDescription = "Typography and Theme Settings", tint = textColor)
              }
            }
          }

          // Quick Appearance Strip: Font Size Controls and Background Theme Toggles
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Customizable Font Size Toggles
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(textColor.copy(alpha = 0.07f))
                .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
              IconButton(
                onClick = { viewModel.updateReaderConfig(fontSize = maxOf(12, readerConfig.fontSizeSp - 2)) },
                modifier = Modifier
                  .size(26.dp)
                  .testTag("story_reader_font_decrease")
              ) {
                Text("A-", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
              }
              Text(
                text = "${readerConfig.fontSizeSp}sp",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                  .padding(horizontal = 4.dp)
                  .testTag("story_reader_font_size_display")
              )
              IconButton(
                onClick = { viewModel.updateReaderConfig(fontSize = minOf(32, readerConfig.fontSizeSp + 2)) },
                modifier = Modifier
                  .size(26.dp)
                  .testTag("story_reader_font_increase")
              ) {
                Text("A+", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
              }
            }

            // Customizable Background Theme Toggles
            Row(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              listOf(
                Triple("Cream", ReaderCreamBg, "Cream"),
                Triple("Beige", ReaderBeigeBg, "Beige"),
                Triple("Sepia", ReaderSepiaBg, "Sepia"),
                Triple("Dark", ReaderDarkBg, "Dark"),
                Triple("Twilight", ReaderTwilightBg, "Twilight")
              ).forEach { (mode, swatchColor, _) ->
                val isSelected = readerConfig.themeMode.equals(mode, ignoreCase = true) || (mode == "Beige" && readerConfig.themeMode == "Light")
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(swatchColor)
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) (if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteDarkBrown) else NoveliteBorder,
                      shape = CircleShape
                    )
                    .clickable { viewModel.updateReaderConfig(themeMode = mode) }
                    .testTag("story_reader_theme_$mode")
                )
              }
            }
          }

          // Horizontal Progress Bar at the top of the StoryReader
          ChapterReadingProgressBar(
            progress = chapterProgress,
            chapterTitle = currentChapter.title,
            textColor = textColor,
            accentColor = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteSecondaryAccent,
            modifier = Modifier.testTag("story_reader_progress_bar")
          )
        }
      }
    }

    // Horizontal Progress Bar at Top of StoryReader Screen (Continuous & In Distraction-Free Mode)
    LinearProgressIndicator(
      progress = { chapterProgress },
      modifier = Modifier
        .align(Alignment.TopCenter)
        .fillMaxWidth()
        .height(3.dp)
        .testTag("story_reader_progress_bar"),
      color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteSecondaryAccent,
      trackColor = textColor.copy(alpha = 0.08f)
    )

    // Quick Exit Affordance for Distraction-Free Immersive Mode
    AnimatedVisibility(
      visible = !isControlsVisible,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
    ) {
      Surface(
        shape = CircleShape,
        color = bgColor.copy(alpha = 0.90f),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.25f)),
        shadowElevation = 6.dp
      ) {
        IconButton(
          onClick = { isControlsVisible = true },
          modifier = Modifier
            .size(42.dp)
            .testTag("story_reader_exit_immersive")
        ) {
          Icon(
            imageVector = Icons.Default.FullscreenExit,
            contentDescription = "Exit Distraction-Free Mode",
            tint = textColor,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Bottom Navigation Bar with Chapter Stepper (Toggleable)
    AnimatedVisibility(
      visible = isControlsVisible,
      enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      Surface(
        color = bgColor.copy(alpha = 0.96f),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              if (hasPrevious) {
                viewModel.openReader(story.id, story.chapters[chapterIndex - 1].id)
              }
            },
            enabled = hasPrevious
          ) {
            Icon(
              Icons.Default.ArrowBackIosNew,
              contentDescription = "Prev Chapter",
              tint = if (hasPrevious) textColor else textColor.copy(alpha = 0.3f)
            )
          }

          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable {
                coroutineScope.launch {
                  listState.animateScrollToItem(paragraphs.size + 1)
                }
              }
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Ch ${currentChapter.chapterNumber}/${story.chapters.size}",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = textColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) Color(0xFF382626) else NoveliteSoftAccentBg
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "💬 ${chapterComments.size}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteDarkBrown
                )
              }
            }
            Spacer(modifier = Modifier.width(6.dp))
            // Quick Bottom Heart Like Button
            ChapterLikeButton(
              isLiked = currentChapter.isLiked,
              likesCount = currentChapter.likesCount,
              onLikeClick = { viewModel.toggleLikeChapter(story.id, currentChapter.id) },
              tint = textColor,
              iconSize = 18.dp,
              showCount = true,
              modifier = Modifier.testTag("story_reader_like_button_bottom")
            )
          }

          IconButton(
            onClick = {
              if (hasNext) {
                viewModel.openReader(story.id, story.chapters[chapterIndex + 1].id)
              }
            },
            enabled = hasNext
          ) {
            Icon(
              Icons.Default.ArrowForwardIos,
              contentDescription = "Next Chapter",
              tint = if (hasNext) textColor else textColor.copy(alpha = 0.3f)
            )
          }
        }
      }
    }
  }

  // Typography & Reading Experience Bottom Sheet
  if (showSettingsSheet) {
    ModalBottomSheet(
      onDismissRequest = { showSettingsSheet = false },
      containerColor = NoveliteCardBeige
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Typography & Appearance",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Live Typography Preview Card
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          color = bgColor,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Live Typography Preview",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (readerConfig.themeMode in listOf("Dark", "Twilight")) NoveliteSoftAccentBg else NoveliteCaramel
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Words become worlds when woven with care. Adjust font size, typeface, and line spacing for your ideal reading comfort.",
              fontSize = readerConfig.fontSizeSp.sp,
              fontFamily = selectedFontFamily,
              lineHeight = (readerConfig.fontSizeSp * readerConfig.lineSpacingMultiplier).sp,
              color = textColor
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Font Size Adjuster
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Font Size",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NoveliteDarkBrown
          )
          Text(
            text = "${readerConfig.fontSizeSp} sp",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteCaramel
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = {
            viewModel.updateReaderConfig(fontSize = maxOf(12, readerConfig.fontSizeSp - 2))
          }) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease font size", tint = NoveliteDarkBrown)
          }
          Slider(
            value = readerConfig.fontSizeSp.toFloat(),
            onValueChange = { viewModel.updateReaderConfig(fontSize = it.toInt()) },
            valueRange = 12f..30f,
            steps = 8,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
              thumbColor = NoveliteDarkBrown,
              activeTrackColor = NoveliteDarkBrown,
              inactiveTrackColor = NoveliteSoftAccentBg
            )
          )
          IconButton(onClick = {
            viewModel.updateReaderConfig(fontSize = minOf(30, readerConfig.fontSizeSp + 2))
          }) {
            Icon(Icons.Default.Add, contentDescription = "Increase font size", tint = NoveliteDarkBrown)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Typography Typeface Selector
        Text(
          text = "Typeface Family",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("Serif", "SansSerif", "Monospace", "Cursive").forEach { fam ->
            val isSel = readerConfig.fontFamilyName == fam
            FilterChip(
              selected = isSel,
              onClick = { viewModel.updateReaderConfig(fontFamily = fam) },
              label = {
                Text(
                  text = when (fam) {
                    "Serif" -> "Serif"
                    "SansSerif" -> "Sans"
                    "Monospace" -> "Mono"
                    "Cursive" -> "Script"
                    else -> fam
                  },
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                  fontFamily = when (fam) {
                    "SansSerif" -> FontFamily.SansSerif
                    "Monospace" -> FontFamily.Monospace
                    "Cursive" -> FontFamily.Cursive
                    else -> FontFamily.Serif
                  }
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NoveliteDarkBrown,
                selectedLabelColor = NoveliteWhite,
                containerColor = NoveliteCreamBg,
                labelColor = NoveliteDarkBrown
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSel) NoveliteDarkBrown else NoveliteBorder,
                selectedBorderColor = NoveliteDarkBrown,
                enabled = true,
                selected = isSel
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Line Spacing Multiplier
        Text(
          text = "Line Spacing",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(
            Pair("Compact", 1.3f),
            Pair("Normal", 1.6f),
            Pair("Relaxed", 1.9f),
            Pair("Spacious", 2.2f)
          ).forEach { (label, mult) ->
            val isSel = kotlin.math.abs(readerConfig.lineSpacingMultiplier - mult) < 0.1f
            FilterChip(
              selected = isSel,
              onClick = { viewModel.updateReaderConfig(lineSpacing = mult) },
              label = { Text(label, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NoveliteSecondaryAccent,
                selectedLabelColor = NoveliteWhite,
                containerColor = NoveliteCreamBg,
                labelColor = NoveliteDarkBrown
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSel) NoveliteSecondaryAccent else NoveliteBorder,
                selectedBorderColor = NoveliteSecondaryAccent,
                enabled = true,
                selected = isSel
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reading Theme Palettes
        Text(
          text = "Reading Theme",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ReaderThemeSwatch("Cream", ReaderCreamBg, ReaderCreamText, readerConfig.themeMode == "Cream") {
            viewModel.updateReaderConfig(themeMode = "Cream")
          }
          ReaderThemeSwatch("Beige", ReaderBeigeBg, ReaderBeigeText, readerConfig.themeMode == "Beige" || readerConfig.themeMode == "Light") {
            viewModel.updateReaderConfig(themeMode = "Beige")
          }
          ReaderThemeSwatch("Sepia", ReaderSepiaBg, ReaderSepiaText, readerConfig.themeMode == "Sepia") {
            viewModel.updateReaderConfig(themeMode = "Sepia")
          }
          ReaderThemeSwatch("Dark", ReaderDarkBg, ReaderDarkText, readerConfig.themeMode == "Dark") {
            viewModel.updateReaderConfig(themeMode = "Dark")
          }
          ReaderThemeSwatch("Twilight", ReaderTwilightBg, ReaderTwilightText, readerConfig.themeMode == "Twilight") {
            viewModel.updateReaderConfig(themeMode = "Twilight")
          }
        }

        Spacer(modifier = Modifier.height(28.dp))
      }
    }
  }

  // Chapters List Bottom Sheet
  if (showChapterListSheet) {
    ModalBottomSheet(
      onDismissRequest = { showChapterListSheet = false },
      containerColor = NoveliteCardBeige
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Table of Chapters",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(story.chapters) { ch ->
            val isCurrent = ch.id == currentChapter.id
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  viewModel.openReader(story.id, ch.id)
                  showChapterListSheet = false
                },
              color = if (isCurrent) NoveliteSoftAccentBg else NoveliteCreamBg,
              border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, NoveliteSecondaryAccent) else androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Chapter ${ch.chapterNumber}: ${ch.title}",
                  fontFamily = FontFamily.Serif,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                  color = NoveliteDarkBrown
                )
                if (isCurrent) {
                  Text("Reading", fontSize = 11.sp, color = NoveliteSecondaryAccent, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }

  // Inline Comment / Quote Modal Dialog
  if (showInlineCommentModal) {
    AlertDialog(
      onDismissRequest = { showInlineCommentModal = false },
      containerColor = NoveliteCardBeige,
      title = {
        Text(
          text = "Add Reader Note",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          color = NoveliteDarkBrown
        )
      },
      text = {
        Column {
          if (selectedQuoteForComment != null) {
            Surface(
              color = NoveliteCreamBg,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "“$selectedQuoteForComment”",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = NoveliteDarkBrown,
                modifier = Modifier.padding(10.dp)
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
          }

          OutlinedTextField(
            value = inlineCommentText,
            onValueChange = { inlineCommentText = it },
            placeholder = { Text("Leave your thoughts on this moment...", color = NoveliteTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteDarkBrown,
              unfocusedTextColor = NoveliteDarkBrown,
              cursorColor = NoveliteDarkBrown
            ),
            maxLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (inlineCommentText.isNotBlank()) {
              viewModel.postComment(story.id, currentChapter.id, inlineCommentText, selectedQuoteForComment)
              inlineCommentText = ""
              showInlineCommentModal = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteSecondaryAccent),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Post Note 🔥", color = NoveliteWhite, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showInlineCommentModal = false }) {
          Text("Cancel", color = NoveliteDarkBrown)
        }
      }
    )
  }
}

@Composable
fun ReaderThemeSwatch(name: String, bg: Color, text: Color, isSelected: Boolean, onClick: () -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape)
        .background(bg)
        .border(
          width = if (isSelected) 2.dp else 1.dp,
          color = if (isSelected) NoveliteDarkBrown else NoveliteBorder,
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "Aa",
        color = text,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = name,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      color = if (isSelected) NoveliteDarkBrown else NoveliteTextMuted
    )
  }
}

/**
 * ChapterReadingProgressBar component that tracks how far a user has read
 * through the current story chapter and displays it at the top of the ReadingView screen.
 */
@Composable
fun ChapterReadingProgressBar(
  progress: Float,
  chapterTitle: String,
  textColor: Color,
  accentColor: Color = NoveliteSecondaryAccent,
  modifier: Modifier = Modifier
) {
  val percentInt = (progress * 100).toInt().coerceIn(0, 100)
  val estMinutesLeft = maxOf(1, ((1f - progress) * 4).toInt())

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("chapter_reading_progress_bar")
      .testTag("story_reader_progress_bar")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = chapterTitle,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = textColor,
          maxLines = 1
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "• $percentInt% completed",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = accentColor
        )
      }

      Text(
        text = if (percentInt >= 100) "Chapter Finished ✓" else "~$estMinutesLeft min left",
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = textColor.copy(alpha = 0.75f)
      )
    }

    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier
        .fillMaxWidth()
        .height(3.5.dp),
      color = accentColor,
      trackColor = textColor.copy(alpha = 0.15f)
    )
  }
}

/**
 * Heart-shaped 'Like' button for story chapters that triggers a spring animation
 * when pressed to provide tactile feedback.
 */
@Composable
fun ChapterLikeButton(
  isLiked: Boolean,
  likesCount: Int,
  onLikeClick: () -> Unit,
  modifier: Modifier = Modifier,
  tint: Color = NoveliteCaramel,
  iconSize: androidx.compose.ui.unit.Dp = 22.dp,
  showCount: Boolean = true
) {
  val coroutineScope = rememberCoroutineScope()
  val scale = remember { Animatable(1f) }

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = if (isLiked) Color(0xFFFFEBEE).copy(alpha = 0.85f) else Color.Transparent,
    border = if (isLiked) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)) else null,
    modifier = modifier
      .testTag("chapter_like_button")
      .testTag("story_reader_like_button")
      .clip(RoundedCornerShape(20.dp))
      .clickable {
        coroutineScope.launch {
          // Tactile spring feedback animation: quick punch up, bouncy settle back
          scale.animateTo(
            targetValue = 1.45f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioMediumBouncy,
              stiffness = Spring.StiffnessMedium
            )
          )
          scale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioHighBouncy,
              stiffness = Spring.StiffnessLow
            )
          )
        }
        onLikeClick()
      }
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = if (isLiked) "Unlike Chapter" else "Like Chapter",
        tint = if (isLiked) Color(0xFFE53935) else tint,
        modifier = Modifier
          .size(iconSize)
          .graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value
          )
      )
      if (showCount) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (likesCount > 0) "$likesCount" else "Like",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (isLiked) Color(0xFFE53935) else tint
        )
      }
    }
  }
}


