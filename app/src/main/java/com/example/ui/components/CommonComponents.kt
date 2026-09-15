package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.Story
import com.example.data.StoryStatus
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.FlameGold
import com.example.ui.theme.FlameOrange
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

// =========================================================================
// NOVELITE INNOVATIVE LOGO & BRAND SYMBOL
// Concept: An open story transforming upward into an inspiring spark & flame ribbon.
// Represents imagination, storytelling, and continuous daily journeys.
// =========================================================================

@Composable
fun NoveliteLogoSymbol(
  modifier: Modifier = Modifier,
  size: Dp = 36.dp,
  primaryColor: Color = NoveliteDarkBrown,
  secondaryColor: Color = NoveliteWarmBrown,
  accentColor: Color = NoveliteCaramel
) {
  Canvas(
    modifier = modifier
      .size(size)
      .testTag("novelite_logo_symbol")
  ) {
    val w = this.size.width
    val h = this.size.height

    // 1. Left Book Page (Flowing outward and curving up)
    val leftPagePath = Path().apply {
      moveTo(w * 0.48f, h * 0.82f)
      cubicTo(
        w * 0.36f, h * 0.80f,
        w * 0.16f, h * 0.68f,
        w * 0.14f, h * 0.44f
      )
      cubicTo(
        w * 0.18f, h * 0.30f,
        w * 0.34f, h * 0.38f,
        w * 0.46f, h * 0.52f
      )
      cubicTo(
        w * 0.42f, h * 0.65f,
        w * 0.44f, h * 0.74f,
        w * 0.48f, h * 0.82f
      )
      close()
    }
    drawPath(
      path = leftPagePath,
      color = primaryColor,
      style = Fill
    )

    // 2. Right Page Transforming Into Ascending Flame Ribbon
    val rightFlowingPath = Path().apply {
      moveTo(w * 0.52f, h * 0.82f)
      cubicTo(
        w * 0.64f, h * 0.80f,
        w * 0.84f, h * 0.68f,
        w * 0.86f, h * 0.44f
      )
      cubicTo(
        w * 0.84f, h * 0.28f,
        w * 0.68f, h * 0.22f,
        w * 0.58f, h * 0.12f
      )
      cubicTo(
        w * 0.64f, h * 0.26f,
        w * 0.72f, h * 0.36f,
        w * 0.64f, h * 0.52f
      )
      cubicTo(
        w * 0.58f, h * 0.64f,
        w * 0.56f, h * 0.74f,
        w * 0.52f, h * 0.82f
      )
      close()
    }
    drawPath(
      path = rightFlowingPath,
      color = secondaryColor,
      style = Fill
    )

    // 3. Central Rising Spark & Inspiration Flame (Caramel Accent)
    val centralSparkPath = Path().apply {
      moveTo(w * 0.50f, h * 0.64f)
      cubicTo(
        w * 0.42f, h * 0.46f,
        w * 0.46f, h * 0.24f,
        w * 0.50f, h * 0.08f
      )
      cubicTo(
        w * 0.54f, h * 0.24f,
        w * 0.58f, h * 0.46f,
        w * 0.50f, h * 0.64f
      )
      close()
    }
    drawPath(
      path = centralSparkPath,
      color = accentColor,
      style = Fill
    )

    // 4. Subtle Story Spine Arc at Base
    val spinePath = Path().apply {
      moveTo(w * 0.24f, h * 0.85f)
      cubicTo(
        w * 0.38f, h * 0.90f,
        w * 0.62f, h * 0.90f,
        w * 0.76f, h * 0.85f
      )
    }
    drawPath(
      path = spinePath,
      color = primaryColor,
      style = Stroke(
        width = w * 0.06f,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
      )
    )

    // 5. Small Glowing Spark Particle at the Apex
    drawCircle(
      color = accentColor,
      radius = w * 0.045f,
      center = Offset(w * 0.50f, h * 0.06f)
    )
  }
}

@Composable
fun NoveliteLogo(
  modifier: Modifier = Modifier,
  symbolSize: Dp = 32.dp,
  textColor: Color = NoveliteDarkBrown,
  showWordmark: Boolean = true,
  isDark: Boolean = false
) {
  val primary = if (isDark) NoveliteCaramel else NoveliteDarkBrown
  val secondary = if (isDark) NoveliteWarmBrown else NoveliteWarmBrown
  val accent = if (isDark) NoveliteCardBeige else NoveliteCaramel
  val textCol = if (isDark) NoveliteCardBeige else textColor

  Row(
    modifier = modifier.testTag("novelite_brand_logo"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    NoveliteLogoSymbol(
      size = symbolSize,
      primaryColor = primary,
      secondaryColor = secondary,
      accentColor = accent
    )

    if (showWordmark) {
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = "Novelite",
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = (symbolSize.value * 0.72f).sp,
        letterSpacing = (-0.5).sp,
        color = textCol
      )
    }
  }
}

// =========================================================================
// REUSABLE NOVELITE BRAND COMPONENTS
// =========================================================================

@Composable
fun StreakFlameBadge(
  streakDays: Int,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null
) {
  val infiniteTransition = rememberInfiniteTransition(label = "flameGlow")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Surface(
    modifier = modifier
      .testTag("streak_flame_badge")
      .clip(RoundedCornerShape(20.dp))
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    color = NoveliteCardBeige,
    shape = RoundedCornerShape(20.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    shadowElevation = 0.dp
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "🔥",
        fontSize = 13.sp,
        modifier = Modifier.graphicsLayer {
          scaleX = pulseScale
          scaleY = pulseScale
        }
      )
      Spacer(modifier = Modifier.width(5.dp))
      Text(
        text = "$streakDays",
        color = NoveliteDarkBrown,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
      Spacer(modifier = Modifier.width(3.dp))
      Text(
        text = "d",
        color = NoveliteCaramel,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
      )
    }
  }
}

@Composable
fun TodayGoalCard(
  minutesRead: Int,
  goalMinutes: Int,
  streakDays: Int,
  onStartReading: () -> Unit,
  onQuickAddMinute: () -> Unit,
  onOpenDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val progress = (minutesRead.toFloat() / maxOf(1, goalMinutes)).coerceIn(0f, 1f)
  val isCompleted = minutesRead >= goalMinutes
  val remainingMinutes = maxOf(0, goalMinutes - minutesRead)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("today_goal_card")
      .clickable { onOpenDashboard() },
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "DAILY READING GOAL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = NoveliteTextMuted
          )
        }
        Text(
          text = "$minutesRead / $goalMinutes mins",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (isCompleted) NoveliteDarkBrown else NoveliteCaramel
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Warm Caramel Progress Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(NoveliteCreamBg)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(progress)
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(NoveliteCaramel, NoveliteWarmBrown)
              )
            )
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = if (isCompleted) {
          "Daily goal completed! 🔥 Your $streakDays-day streak is glowing."
        } else {
          "$remainingMinutes more minutes to keep your $streakDays-day streak alive! 🔥"
        },
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = NoveliteTextPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onStartReading,
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("goal_read_now_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = NoveliteButtonBg,
            contentColor = NoveliteButtonText
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
              tint = NoveliteButtonText
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isCompleted) "Continue Story" else "Read Today",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = NoveliteButtonText
            )
          }
        }

        Button(
          onClick = onQuickAddMinute,
          modifier = Modifier
            .height(44.dp)
            .testTag("goal_quick_add_button"),
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
              imageVector = Icons.Outlined.Timer,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = NoveliteButtonText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "+1 Min",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = NoveliteButtonText
            )
          }
        }
      }
    }
  }
}

@Composable
fun StoryCoverView(
  story: Story,
  modifier: Modifier = Modifier,
  shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp),
  showTeaserBadge: Boolean = true
) {
  Box(
    modifier = modifier
      .clip(shape)
      .background(Color(story.coverColorHex))
  ) {
    val coverModel: Any? = story.coverImageUrl ?: story.coverDrawableRes

    if (coverModel != null) {
      AsyncImage(
        model = coverModel,
        contentDescription = "Cover of ${story.title}",
        modifier = Modifier.matchParentSize(),
        contentScale = ContentScale.Crop
      )
    } else {
      // Artistic Typographic Cover Fallback
      Box(
        modifier = Modifier
          .matchParentSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                NoveliteDarkBrown,
                NoveliteWarmBrown.copy(alpha = 0.85f)
              )
            )
          )
          .padding(8.dp)
      ) {
        Column(
          modifier = Modifier.matchParentSize(),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = story.genre.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = NoveliteCaramel,
            fontWeight = FontWeight.Bold,
            fontSize = 8.sp,
            letterSpacing = 0.5.sp
          )
          Text(
            text = story.title,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = story.authorName,
            fontSize = 9.sp,
            color = NoveliteCardBeige,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }

    // Optional Teaser Badge
    if (showTeaserBadge && !story.teaserVideoUrl.isNullOrBlank()) {
      Surface(
        color = NoveliteDarkBrown.copy(alpha = 0.85f),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
          .padding(6.dp)
          .align(Alignment.BottomEnd)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("🎬", fontSize = 9.sp)
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "Teaser",
            fontSize = 8.sp,
            color = NoveliteWhite,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun StoryCard(
  story: Story,
  onClick: () -> Unit,
  onToggleLike: (() -> Unit)? = null,
  onToggleLibrary: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(152.dp)
      .testTag("story_card_${story.id}")
      .clickable { onClick() },
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column {
      // Cover Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
      ) {
        StoryCoverView(
          story = story,
          modifier = Modifier.matchParentSize(),
          shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
          showTeaserBadge = true
        )

        // Genre Pill
        Box(
          modifier = Modifier
            .padding(8.dp)
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(6.dp))
            .background(NoveliteDarkBrown.copy(alpha = 0.75f))
            .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
          Text(
            text = story.genre,
            color = NoveliteCardBeige,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Status Badge if Completed
        if (story.status == StoryStatus.COMPLETED) {
          Box(
            modifier = Modifier
              .padding(8.dp)
              .align(Alignment.TopEnd)
              .clip(RoundedCornerShape(6.dp))
              .background(NoveliteWarmBrown)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "COMPLETE",
              color = NoveliteWhite,
              fontSize = 8.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      // Story Metadata
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = story.title,
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = NoveliteTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = story.authorName,
          fontSize = 11.sp,
          color = NoveliteTextMuted,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = formatReads(story.readsCount),
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = NoveliteTextMuted
            )
          }

          if (onToggleLike != null) {
            Icon(
              imageVector = if (story.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (story.isLiked) NoveliteCaramel else NoveliteTextMuted,
              modifier = Modifier
                .size(16.dp)
                .clickable { onToggleLike() }
            )
          }
        }
      }
    }
  }
}

@Composable
fun HorizontalStoryCard(
  story: Story,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("horizontal_story_card_${story.id}")
      .clickable { onClick() },
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(modifier = Modifier.padding(12.dp)) {
      StoryCoverView(
        story = story,
        modifier = Modifier
          .width(76.dp)
          .height(104.dp),
        shape = RoundedCornerShape(12.dp),
        showTeaserBadge = true
      )

      Spacer(modifier = Modifier.width(14.dp))

      Column(
        modifier = Modifier
          .weight(1f)
          .height(104.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = story.genre.uppercase(),
              fontSize = 10.sp,
              color = NoveliteCaramel,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "${story.chaptersCount} ch",
              fontSize = 11.sp,
              color = NoveliteTextMuted
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = story.title,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = NoveliteTextPrimary
          )

          Text(
            text = story.authorName,
            fontSize = 11.sp,
            color = NoveliteTextMuted
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "${formatReads(story.readsCount)} reads", fontSize = 11.sp, color = NoveliteTextMuted)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = null,
              tint = NoveliteCaramel,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "${story.likesCount}", fontSize = 11.sp, color = NoveliteTextMuted)
          }
        }
      }
    }
  }
}

@Composable
fun CelebrationDialog(
  streakDays: Int,
  goalMinutes: Int,
  badgeName: String?,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("celebration_modal"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
      border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        NoveliteLogoSymbol(
          size = 48.dp,
          primaryColor = NoveliteDarkBrown,
          secondaryColor = NoveliteWarmBrown,
          accentColor = NoveliteCaramel
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = if (badgeName != null) "Milestone Unlocked!" else "Daily Reading Goal Complete!",
          fontFamily = FontFamily.Serif,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "🔥 Your streak is now $streakDays days!",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = NoveliteCaramel
        )

        if (badgeName != null) {
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = NoveliteCreamBg,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
          ) {
            Text(
              text = "Awarded: $badgeName",
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              color = NoveliteDarkBrown,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Keep returning every day to nurture your storytelling spark and unlock new badges.",
          style = MaterialTheme.typography.bodyMedium,
          color = NoveliteTextMuted,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("celebration_continue_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = NoveliteDarkBrown,
            contentColor = NoveliteWhite
          )
        ) {
          Text("Keep Reading 🔥", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

fun formatReads(count: Int): String {
  return when {
    count >= 1_000_000 -> "${count / 1_000_000}M"
    count >= 1_000 -> "${count / 1_000}k"
    else -> "$count"
  }
}
