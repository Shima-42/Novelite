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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Chapter
import com.example.data.Comment
import com.example.data.StoryStatus
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.FootprintsOnPageIcon
import com.example.ui.components.InklingsIcon
import com.example.ui.components.NoveliteVideoPlayer
import com.example.ui.components.TheStoryUnfoldsIcon
import com.example.ui.components.formatReads
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(viewModel: NoveliteViewModel) {
  val storyId by viewModel.selectedStoryId.collectAsState()
  val stories by viewModel.stories.collectAsState()
  val comments by viewModel.comments.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()

  val story = stories.find { it.id == storyId } ?: stories.firstOrNull()

  var showReportDialog by remember { mutableStateOf(false) }
  var reportReason by remember { mutableStateOf("Inappropriate content") }
  var reportDetails by remember { mutableStateOf("") }

  var commentText by remember { mutableStateOf("") }

  if (story == null) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(NoveliteCreamBg),
      contentAlignment = Alignment.Center
    ) {
      Text("Story not found.", color = NoveliteTextPrimary)
    }
    return
  }

  val storyComments = comments.filter { it.storyId == story.id }
  val isFollowingAuthor = currentUser.followedAuthorIds.contains(story.authorId)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("story_detail_screen"),
    contentPadding = PaddingValues(bottom = 36.dp)
  ) {
    // Top Cover Hero Area
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(340.dp)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color(story.coverColorHex), NoveliteDarkBrown)
            )
          )
      ) {
        val coverModel: Any? = story.coverImageUrl ?: story.coverDrawableRes
        if (coverModel != null) {
          AsyncImage(
            model = coverModel,
            contentDescription = "Cover Image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
        }

        // Overlay Gradient
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  NoveliteDarkBrown.copy(alpha = 0.4f),
                  Color.Transparent,
                  NoveliteCreamBg
                )
              )
            )
        )

        // Top Navigation Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .align(Alignment.TopCenter),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { viewModel.navigateTo(Screen.HOME) },
            modifier = Modifier
              .clip(CircleShape)
              .background(NoveliteDarkBrown.copy(alpha = 0.7f))
              .testTag("story_detail_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = NoveliteWhite
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
              onClick = { showReportDialog = true },
              modifier = Modifier
                .clip(CircleShape)
                .background(NoveliteDarkBrown.copy(alpha = 0.7f))
                .testTag("story_report_button")
            ) {
              Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = "Report",
                tint = NoveliteWhite
              )
            }

            IconButton(
              onClick = { /* Share action */ },
              modifier = Modifier
                .clip(CircleShape)
                .background(NoveliteDarkBrown.copy(alpha = 0.7f))
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = NoveliteWhite
              )
            }
          }
        }
      }
    }

    // Story Main Information
    item {
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // Space Header: The Story Unfolds
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
          shape = RoundedCornerShape(14.dp),
          color = NoveliteCardBeige,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            TheStoryUnfoldsIcon(size = 28.dp, isSelected = true)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "The Story Unfolds",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NoveliteDarkBrown
              )
              Text(
                text = "Everything you need to know before the journey begins.",
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Badges Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = NoveliteDarkBrown,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = story.genre,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              color = NoveliteCardBeige,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Surface(
            color = if (story.status == StoryStatus.COMPLETED) NoveliteWarmBrown else NoveliteCaramel.copy(alpha = 0.2f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = story.status.name,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = if (story.status == StoryStatus.COMPLETED) NoveliteWhite else NoveliteDarkBrown,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
          text = story.title,
          fontFamily = FontFamily.Serif,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Author Card Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { viewModel.openAuthorProfile(story.authorId) }
            .padding(vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(NoveliteDarkBrown),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = story.authorName.take(1),
                color = NoveliteCardBeige,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = story.authorName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = NoveliteTextPrimary
              )
              Text(
                text = "@${story.authorUsername}",
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }
          }

          OutlinedButton(
            onClick = { viewModel.followAuthor(story.authorId) },
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (isFollowingAuthor) NoveliteCardBeige else Color.Transparent,
              contentColor = NoveliteDarkBrown
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
            modifier = Modifier.testTag("follow_author_button")
          ) {
            Text(
              text = if (isFollowingAuthor) "Following" else "+ Follow",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Bar
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            DetailStatItem(label = "Reads", value = formatReads(story.readsCount), iconText = "🔥")
            DetailStatItem(label = "Likes", value = "${story.likesCount}", iconText = "❤️")
            DetailStatItem(label = "Chapters", value = "${story.chaptersCount}", iconText = "📖")
            DetailStatItem(label = "Comments", value = "${story.commentsCount}", iconText = "💬")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              viewModel.openReader(story.id, story.lastReadChapterId)
            },
            modifier = Modifier
              .weight(1.8f)
              .height(48.dp)
              .testTag("start_reading_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = NoveliteSecondaryAccent,
              contentColor = NoveliteWhite
            )
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NoveliteWhite)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (story.readingProgressPercent > 0f) "Continue (${(story.readingProgressPercent * 100).toInt()}%)" else "Start Reading",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = NoveliteWhite
            )
          }

          OutlinedButton(
            onClick = { viewModel.toggleLibrary(story.id) },
            modifier = Modifier
              .weight(1.2f)
              .height(48.dp)
              .testTag("library_toggle_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (story.isInLibrary) NoveliteSoftAccentBg else NoveliteCardBeige,
              contentColor = NoveliteDarkBrown
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (story.isInLibrary) NoveliteSecondaryAccent else NoveliteBorder)
          ) {
            Icon(
              imageVector = if (story.isInLibrary) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = if (story.isInLibrary) NoveliteSecondaryAccent else NoveliteDarkBrown
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (story.isInLibrary) "In Library" else "+ Library",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
          }

          IconButton(
            onClick = { viewModel.toggleLike(story.id) },
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(NoveliteCardBeige)
              .border(1.dp, NoveliteBorder, RoundedCornerShape(12.dp))
              .testTag("story_like_button")
          ) {
            Icon(
              imageVector = if (story.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (story.isLiked) NoveliteCaramel else NoveliteTextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Synopsis Description
        Text(
          text = "Synopsis",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = story.description,
          style = MaterialTheme.typography.bodyMedium,
          lineHeight = 22.sp,
          color = NoveliteTextPrimary.copy(alpha = 0.9f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tags
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          story.tags.forEach { tag ->
            Surface(
              color = NoveliteCardBeige,
              shape = RoundedCornerShape(6.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
            ) {
              Text(
                text = "#$tag",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                fontSize = 11.sp,
                color = NoveliteWarmBrown,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        // Promotional Story Teaser Video (if available)
        if (!story.teaserVideoUrl.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(20.dp))
          
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("story_teaser_video_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NoveliteDarkBrown),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("🎬", fontSize = 16.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Story Teaser & Trailer",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NoveliteWhite
                  )
                }
                
                Surface(
                  color = NoveliteCaramel,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "${story.teaserVideoDurationSec ?: 15}s HD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NoveliteDarkBrown,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              NoveliteVideoPlayer(
                videoUrl = story.teaserVideoUrl,
                title = "${story.title} — Official Teaser",
                autoPlay = false,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chapters List Header
        Text(
          text = "Chapters (${story.chapters.size})",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      }
    }

    // Chapters
    items(story.chapters) { chapter ->
      ChapterItemRow(
        chapter = chapter,
        onClick = { viewModel.openReader(story.id, chapter.id) }
      )
    }

    // Comments & Discussions Section: Inklings & Footprints on the Page
    item {
      Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        
        // Official Space: Inklings Banner
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = NoveliteCardBeige,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            InklingsIcon(size = 28.dp, isSelected = true)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Inklings (${storyComments.size})",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NoveliteDarkBrown
              )
              Text(
                text = "Where thoughts about stories come alive.",
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }
            FootprintsOnPageIcon(size = 22.dp, isSelected = true)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Comment Input (Footprints on the Page)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Leave a comment...", color = NoveliteTextMuted) },
            modifier = Modifier
              .weight(1f)
              .testTag("detail_comment_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NoveliteCardBeige,
              unfocusedContainerColor = NoveliteCardBeige,
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            )
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (commentText.isNotBlank()) {
                viewModel.postComment(story.id, story.chapters.firstOrNull()?.id ?: "c1", commentText)
                commentText = ""
              }
            },
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(NoveliteDarkBrown)
              .testTag("detail_comment_submit")
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = NoveliteWhite, modifier = Modifier.size(18.dp))
          }
        }
      }
    }

    // Comments List Preview
    items(storyComments) { comment ->
      CommentCard(
        comment = comment,
        onLike = { viewModel.likeComment(comment.id) },
        onReply = { text -> viewModel.replyComment(comment.id, text) },
        onDelete = { viewModel.deleteComment(comment.id) },
        isCurrentUser = comment.userId == currentUser.id
      )
    }
  }

  // Report Modal Dialog
  if (showReportDialog) {
    AlertDialog(
      onDismissRequest = { showReportDialog = false },
      containerColor = NoveliteCardBeige,
      title = {
        Text(
          text = "Report Story: ${story.title}",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      },
      text = {
        Column {
          Text(
            text = "Help us keep Novelite safe. Why are you reporting this?",
            fontSize = 12.sp,
            color = NoveliteTextMuted
          )
          Spacer(modifier = Modifier.height(10.dp))
          listOf("Inappropriate content", "Harassment", "Copyright infringement", "Spam", "Hate speech").forEach { reason ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { reportReason = reason }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = reportReason == reason,
                onClick = { reportReason = reason },
                colors = RadioButtonDefaults.colors(selectedColor = NoveliteDarkBrown)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(reason, fontSize = 13.sp, color = NoveliteTextPrimary)
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = reportDetails,
            onValueChange = { reportDetails = it },
            placeholder = { Text("Additional details (optional)", color = NoveliteTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            ),
            maxLines = 2
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.submitReport("Story", story.title, reportReason, reportDetails)
            showReportDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Submit Report", color = NoveliteWhite, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showReportDialog = false }) {
          Text("Cancel", color = NoveliteTextMuted)
        }
      }
    )
  }
}

@Composable
fun ChapterItemRow(
  chapter: Chapter,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 4.dp)
      .clickable { onClick() }
      .testTag("chapter_item_${chapter.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          color = NoveliteCreamBg,
          shape = CircleShape,
          modifier = Modifier.size(34.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = "${chapter.chapterNumber}",
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown,
              fontSize = 13.sp
            )
          }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = chapter.title,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          Text(
            text = "${chapter.wordsCount} words • ~${maxOf(1, chapter.wordsCount / 200)} min read",
            fontSize = 11.sp,
            color = NoveliteTextMuted
          )
        }
      }

      Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = "Read chapter",
        tint = NoveliteCaramel,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
fun CommentCard(
  comment: Comment,
  onLike: () -> Unit,
  onReply: (String) -> Unit,
  onDelete: () -> Unit,
  isCurrentUser: Boolean
) {
  var showReplyInput by remember { mutableStateOf(false) }
  var replyText by remember { mutableStateOf("") }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 5.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(NoveliteDarkBrown),
            contentAlignment = Alignment.Center
          ) {
            Text(text = comment.userName.take(1), color = NoveliteCardBeige, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = comment.userName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NoveliteTextPrimary)
        }
        Text(text = comment.timestamp, fontSize = 11.sp, color = NoveliteTextMuted)
      }

      // Inline Quote if attached
      if (comment.inlineQuote != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
          color = NoveliteCreamBg,
          shape = RoundedCornerShape(6.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Text(
            text = "“${comment.inlineQuote}”",
            style = MaterialTheme.typography.bodySmall,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = NoveliteDarkBrown,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(text = comment.text, fontSize = 13.sp, color = NoveliteTextPrimary, lineHeight = 18.sp)

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Row(
            modifier = Modifier
              .clickable { onLike() }
              .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (comment.isLiked) NoveliteCaramel else NoveliteTextMuted,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${comment.likes}", fontSize = 12.sp, color = NoveliteTextMuted)
          }

          Spacer(modifier = Modifier.width(12.dp))

          Text(
            text = "Reply",
            fontSize = 12.sp,
            color = NoveliteWarmBrown,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clickable { showReplyInput = !showReplyInput }
              .padding(4.dp)
          )
        }

        if (isCurrentUser) {
          Text(
            text = "Delete",
            fontSize = 11.sp,
            color = NoveliteTextMuted,
            modifier = Modifier
              .clickable { onDelete() }
              .padding(4.dp)
          )
        }
      }

      // Replies list
      if (comment.replies.isNotEmpty()) {
        Spacer(modifier = Modifier.height(6.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          comment.replies.forEach { rep ->
            Surface(
              color = NoveliteCreamBg,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(8.dp)) {
                Text(text = rep.userName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NoveliteTextPrimary)
                Text(text = rep.text, fontSize = 12.sp, color = NoveliteTextPrimary)
              }
            }
          }
        }
      }

      // Reply Input Field
      if (showReplyInput) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          OutlinedTextField(
            value = replyText,
            onValueChange = { replyText = it },
            placeholder = { Text("Write a reply...", color = NoveliteTextMuted) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = NoveliteCreamBg,
              unfocusedContainerColor = NoveliteCreamBg,
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            ),
            singleLine = true
          )
          Spacer(modifier = Modifier.width(6.dp))
          IconButton(
            onClick = {
              if (replyText.isNotBlank()) {
                onReply(replyText)
                replyText = ""
                showReplyInput = false
              }
            }
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = NoveliteDarkBrown)
          }
        }
      }
    }
  }
}

@Composable
fun DetailStatItem(label: String, value: String, iconText: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(text = iconText, fontSize = 12.sp)
      Spacer(modifier = Modifier.width(3.dp))
      Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NoveliteTextPrimary)
    }
    Text(text = label, fontSize = 10.sp, color = NoveliteTextMuted)
  }
}
