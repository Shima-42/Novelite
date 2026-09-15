package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Comment
import com.example.data.CommentReply
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

/**
 * ChapterCommentSection component for story chapters.
 * Enables readers to leave chapter feedback, share reactions/moods, attach quotes,
 * and actively interact with other readers via like counters and threaded replies.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChapterCommentSection(
  storyId: String,
  chapterId: String,
  chapterTitle: String,
  comments: List<Comment>,
  currentUserId: String,
  currentUserName: String,
  onPostComment: (text: String, quote: String?) -> Unit,
  onLikeComment: (commentId: String) -> Unit,
  onReplyComment: (commentId: String, replyText: String) -> Unit,
  onDeleteComment: (commentId: String) -> Unit,
  modifier: Modifier = Modifier,
  initialAttachedQuote: String? = null,
  isDarkTheme: Boolean = false
) {
  var feedbackInput by remember { mutableStateOf("") }
  var attachedQuote by remember(initialAttachedQuote) { mutableStateOf(initialAttachedQuote) }
  var selectedSort by remember { mutableStateOf("Top") } // "Top", "Newest", "Quotes"
  var selectedMoodTag by remember { mutableStateOf<String?>(null) }
  var showPostSuccessBanner by remember { mutableStateOf(false) }

  // Theme-aware container & typography colors
  val cardBg = if (isDarkTheme) Color(0xFF231B1B) else NoveliteCardBeige
  val innerBg = if (isDarkTheme) Color(0xFF2C2222) else NoveliteCreamBg
  val borderColor = if (isDarkTheme) Color(0xFF3E3131) else NoveliteBorder
  val textColor = if (isDarkTheme) NoveliteCreamBg else NoveliteTextPrimary
  val mutedColor = if (isDarkTheme) Color(0xFFB09D9D) else NoveliteTextMuted
  val accentColor = if (isDarkTheme) NoveliteSoftAccentBg else NoveliteSecondaryAccent

  // Filter comments for this chapter
  val chapterComments = remember(comments, chapterId) {
    comments.filter { it.chapterId == chapterId }
  }

  // Sorted comments
  val sortedComments = remember(chapterComments, selectedSort) {
    when (selectedSort) {
      "Top" -> chapterComments.sortedByDescending { it.likes }
      "Newest" -> chapterComments // already newest first in repo
      "Quotes" -> chapterComments.filter { !it.inlineQuote.isNullOrBlank() }
      else -> chapterComments
    }
  }

  val totalLikesInChapter = remember(chapterComments) {
    chapterComments.sumOf { it.likes }
  }

  val feedbackMoods = listOf(
    "🔥 Mind Blown!",
    "😭 Emotional",
    "⚡ High Stakes",
    "❤️ Wholesome",
    "✍️ Beautiful Prose",
    "🤔 Fan Theory"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("chapter_comment_section"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    border = BorderStroke(1.dp, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // 1. Header & Overview
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(if (isDarkTheme) Color(0xFF382626) else NoveliteSoftAccentBg)
              .border(1.dp, accentColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.ChatBubbleOutline,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "CHAPTER REFLECTIONS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp,
              color = accentColor
            )
            Text(
              text = "Footprints on the Page",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = textColor
            )
          }
        }

        // Stats pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = innerBg,
          border = BorderStroke(1.dp, borderColor)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "💬 ${chapterComments.size}  •  ❤️ $totalLikesInChapter",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = textColor
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. Feedback Composer Box
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("comment_composer_card"),
        shape = RoundedCornerShape(16.dp),
        color = innerBg,
        border = BorderStroke(1.dp, borderColor)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          // User avatar + status indicator
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
                  .background(accentColor),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = currentUserName.take(1).uppercase(),
                  color = NoveliteWhite,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Sharing feedback as $currentUserName",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
              )
            }

            if (selectedMoodTag != null) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = accentColor.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, accentColor)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = selectedMoodTag ?: "",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove tag",
                    modifier = Modifier
                      .size(12.dp)
                      .clickable { selectedMoodTag = null },
                    tint = accentColor
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quick Mood Reaction Chips
          Text(
            text = "How did this chapter make you feel?",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = mutedColor
          )
          Spacer(modifier = Modifier.height(6.dp))
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            feedbackMoods.forEach { mood ->
              val isSelected = selectedMoodTag == mood
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) accentColor else (if (isDarkTheme) Color(0xFF382929) else NoveliteCardBeige),
                border = BorderStroke(1.dp, if (isSelected) accentColor else borderColor),
                modifier = Modifier
                  .clickable {
                    selectedMoodTag = if (isSelected) null else mood
                  }
                  .testTag("mood_chip_$mood")
              ) {
                Text(
                  text = mood,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) NoveliteWhite else textColor,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          // Attached Quote if present
          if (attachedQuote != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isDarkTheme) Color(0xFF3A2D2D) else NoveliteSoftAccentBg.copy(alpha = 0.5f),
              border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.FormatQuote,
                  contentDescription = null,
                  tint = accentColor,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "“$attachedQuote”",
                  fontStyle = FontStyle.Italic,
                  fontSize = 11.sp,
                  color = textColor,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f)
                )
                IconButton(
                  onClick = { attachedQuote = null },
                  modifier = Modifier.size(20.dp)
                ) {
                  Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove quote",
                    tint = mutedColor,
                    modifier = Modifier.size(14.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Comment Text Input
          OutlinedTextField(
            value = feedbackInput,
            onValueChange = { feedbackInput = it },
            placeholder = {
              Text(
                text = "Leave feedback, share theories, or highlight a favorite scene in $chapterTitle...",
                fontSize = 13.sp,
                color = mutedColor
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("chapter_comment_input"),
            shape = RoundedCornerShape(12.dp),
            minLines = 2,
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = if (isDarkTheme) Color(0xFF221A1A) else NoveliteWhite,
              unfocusedContainerColor = if (isDarkTheme) Color(0xFF221A1A) else NoveliteWhite,
              focusedBorderColor = accentColor,
              unfocusedBorderColor = borderColor,
              focusedTextColor = textColor,
              unfocusedTextColor = textColor,
              cursorColor = accentColor
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Action Row: Send Button & Character / Helper
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (feedbackInput.isNotEmpty()) "${feedbackInput.length} chars" else "Kind critiques help authors grow 🌱",
              fontSize = 11.sp,
              color = mutedColor
            )

            Button(
              onClick = {
                val fullText = if (selectedMoodTag != null) {
                  "[$selectedMoodTag] $feedbackInput"
                } else {
                  feedbackInput
                }
                if (fullText.isNotBlank()) {
                  onPostComment(fullText, attachedQuote)
                  feedbackInput = ""
                  attachedQuote = null
                  selectedMoodTag = null
                  showPostSuccessBanner = true
                }
              },
              enabled = feedbackInput.isNotBlank() || selectedMoodTag != null,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = NoveliteWhite,
                disabledContainerColor = borderColor,
                disabledContentColor = mutedColor
              ),
              modifier = Modifier.testTag("post_feedback_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Post Feedback", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Success notification toast
      AnimatedVisibility(
        visible = showPostSuccessBanner,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column {
          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isDarkTheme) Color(0xFF2B3A2C) else Color(0xFFE8F5E9),
            border = BorderStroke(1.dp, Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "✨ Your footprint has been pressed into the chapter!",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkTheme) Color(0xFFA5D6A7) else Color(0xFF2E7D32)
              )
              IconButton(
                onClick = { showPostSuccessBanner = false },
                modifier = Modifier.size(20.dp)
              ) {
                Icon(
                  Icons.Default.Close,
                  contentDescription = "Dismiss",
                  tint = if (isDarkTheme) Color(0xFFA5D6A7) else Color(0xFF2E7D32),
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Filter / Sort Tabs
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          listOf("Top", "Newest", "Quotes").forEach { sort ->
            val isSelected = selectedSort == sort
            FilterChip(
              selected = isSelected,
              onClick = { selectedSort = sort },
              label = {
                Text(
                  text = when (sort) {
                    "Top" -> "🔥 Top"
                    "Newest" -> "🕒 Latest"
                    "Quotes" -> "“ Quotes"
                    else -> sort
                  },
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = accentColor,
                selectedLabelColor = NoveliteWhite,
                containerColor = innerBg,
                labelColor = textColor
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = borderColor,
                selectedBorderColor = accentColor,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }

        Text(
          text = "${sortedComments.size} notes",
          fontSize = 11.sp,
          color = mutedColor,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 4. Comments List / Empty State
      if (sortedComments.isEmpty()) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          shape = RoundedCornerShape(14.dp),
          color = innerBg,
          border = BorderStroke(1.dp, borderColor)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.QuestionAnswer,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (selectedSort == "Quotes") "No quotes highlighted yet" else "No footprints on this chapter yet",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Be the first reader to share feedback, theories, or reactions!",
              fontSize = 12.sp,
              color = mutedColor,
              textAlign = TextAlign.Center
            )
          }
        }
      } else {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          sortedComments.forEach { comment ->
            ChapterCommentItem(
              comment = comment,
              isCurrentUser = comment.userId == currentUserId,
              onLike = { onLikeComment(comment.id) },
              onReply = { replyText -> onReplyComment(comment.id, replyText) },
              onDelete = { onDeleteComment(comment.id) },
              onQuoteSelect = { quote -> attachedQuote = quote },
              isDarkTheme = isDarkTheme
            )
          }
        }
      }
    }
  }
}

/**
 * Individual chapter comment item with user identity, inline quotes, like counter,
 * threaded replies drawer, and reply composer.
 */
@Composable
fun ChapterCommentItem(
  comment: Comment,
  isCurrentUser: Boolean,
  onLike: () -> Unit,
  onReply: (String) -> Unit,
  onDelete: () -> Unit,
  onQuoteSelect: (String) -> Unit,
  modifier: Modifier = Modifier,
  isDarkTheme: Boolean = false
) {
  var showReplies by remember { mutableStateOf(comment.replies.isNotEmpty()) }
  var showReplyComposer by remember { mutableStateOf(false) }
  var replyInput by remember { mutableStateOf("") }

  val cardBg = if (isDarkTheme) Color(0xFF2C2222) else NoveliteCreamBg
  val replyBg = if (isDarkTheme) Color(0xFF221A1A) else NoveliteCardBeige
  val borderColor = if (isDarkTheme) Color(0xFF3D2F2F) else NoveliteBorder
  val textColor = if (isDarkTheme) NoveliteCreamBg else NoveliteDarkBrown
  val mutedColor = if (isDarkTheme) Color(0xFFB09D9D) else NoveliteTextMuted
  val accentColor = if (isDarkTheme) NoveliteSoftAccentBg else NoveliteSecondaryAccent

  // Like heart bounce animation
  val heartScale by animateFloatAsState(
    targetValue = if (comment.isLiked) 1.25f else 1.0f,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "heartScale"
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("comment_item_${comment.id}")
      .animateContentSize(),
    shape = RoundedCornerShape(14.dp),
    color = cardBg,
    border = BorderStroke(1.dp, borderColor)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      // Top row: Avatar, Name, Timestamp, Delete action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(CircleShape)
              .background(if (isCurrentUser) accentColor else NoveliteDarkBrown),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = comment.userName.take(1).uppercase(),
              color = NoveliteWhite,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = comment.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = textColor
              )
              if (isCurrentUser) {
                Spacer(modifier = Modifier.width(5.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = accentColor.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = "YOU",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
            }
            Text(
              text = comment.timestamp,
              fontSize = 10.sp,
              color = mutedColor
            )
          }
        }

        if (isCurrentUser) {
          IconButton(
            onClick = onDelete,
            modifier = Modifier
              .size(26.dp)
              .testTag("delete_comment_${comment.id}")
          ) {
            Icon(
              Icons.Default.DeleteOutline,
              contentDescription = "Delete comment",
              tint = mutedColor,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Inline Quote Card (if comment was attached to a specific passage)
      if (!comment.inlineQuote.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (isDarkTheme) Color(0xFF382A2A) else NoveliteSoftAccentBg.copy(alpha = 0.4f),
          border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onQuoteSelect(comment.inlineQuote) }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.FormatQuote,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "“${comment.inlineQuote}”",
              fontSize = 11.sp,
              fontStyle = FontStyle.Italic,
              color = textColor,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Comment Body Text
      Text(
        text = comment.text,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        color = textColor
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Actions Row: Likes, Reply toggle, Reply count badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Like Button with heart animation
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { onLike() }
              .padding(horizontal = 6.dp, vertical = 4.dp)
              .testTag("like_comment_${comment.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like feedback",
              tint = if (comment.isLiked) NoveliteCaramel else mutedColor,
              modifier = Modifier
                .size(16.dp)
                .scale(heartScale)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${comment.likes}",
              fontSize = 12.sp,
              fontWeight = if (comment.isLiked) FontWeight.Bold else FontWeight.Medium,
              color = if (comment.isLiked) NoveliteCaramel else mutedColor
            )
          }

          // Reply Action Button
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { showReplyComposer = !showReplyComposer }
              .padding(horizontal = 6.dp, vertical = 4.dp)
              .testTag("reply_comment_${comment.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Reply,
              contentDescription = "Reply",
              tint = accentColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Reply",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = accentColor
            )
          }
        }

        // Threaded replies counter toggle
        if (comment.replies.isNotEmpty()) {
          TextButton(
            onClick = { showReplies = !showReplies },
            modifier = Modifier.height(28.dp)
          ) {
            Text(
              text = if (showReplies) "Hide ${comment.replies.size} replies" else "View ${comment.replies.size} replies",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = accentColor
            )
          }
        }
      }

      // Threaded Nested Replies
      AnimatedVisibility(
        visible = showReplies && comment.replies.isNotEmpty(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          comment.replies.forEach { reply ->
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = replyBg,
              border = BorderStroke(1.dp, borderColor),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(NoveliteDarkBrown),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = reply.userName.take(1).uppercase(),
                    color = NoveliteWhite,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = reply.userName,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = textColor
                    )
                    Text(
                      text = reply.timestamp,
                      fontSize = 9.sp,
                      color = mutedColor
                    )
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = reply.text,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = textColor
                  )
                }
              }
            }
          }
        }
      }

      // Inline Reply Composer
      AnimatedVisibility(
        visible = showReplyComposer,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column(modifier = Modifier.padding(top = 10.dp, start = 12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = replyInput,
              onValueChange = { replyInput = it },
              placeholder = {
                Text("Reply to ${comment.userName}...", fontSize = 12.sp, color = mutedColor)
              },
              modifier = Modifier
                .weight(1f)
                .testTag("reply_input_${comment.id}"),
              shape = RoundedCornerShape(10.dp),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = replyBg,
                unfocusedContainerColor = replyBg,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
              )
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
              onClick = {
                if (replyInput.isNotBlank()) {
                  onReply(replyInput)
                  replyInput = ""
                  showReplyComposer = false
                  showReplies = true
                }
              },
              modifier = Modifier
                .size(40.dp)
                .background(accentColor, RoundedCornerShape(10.dp))
                .testTag("send_reply_${comment.id}")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send reply",
                tint = NoveliteWhite,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }
}

/**
 * Convenience wrapper linking ChapterCommentSection to NoveliteViewModel
 */
@Composable
fun ChapterCommentSection(
  viewModel: NoveliteViewModel,
  storyId: String,
  chapterId: String,
  chapterTitle: String,
  modifier: Modifier = Modifier,
  initialAttachedQuote: String? = null,
  isDarkTheme: Boolean = false
) {
  val comments by viewModel.comments.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()

  ChapterCommentSection(
    storyId = storyId,
    chapterId = chapterId,
    chapterTitle = chapterTitle,
    comments = comments,
    currentUserId = currentUser.id,
    currentUserName = currentUser.displayName,
    onPostComment = { text, quote ->
      viewModel.postComment(storyId, chapterId, text, quote)
    },
    onLikeComment = { commentId ->
      viewModel.likeComment(commentId)
    },
    onReplyComment = { commentId, replyText ->
      viewModel.replyComment(commentId, replyText)
    },
    onDeleteComment = { commentId ->
      viewModel.deleteComment(commentId)
    },
    initialAttachedQuote = initialAttachedQuote,
    isDarkTheme = isDarkTheme,
    modifier = modifier
  )
}
