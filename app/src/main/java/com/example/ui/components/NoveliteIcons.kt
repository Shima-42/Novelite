package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NovelitePrimaryAccent
import com.example.ui.theme.NovelitePrimaryText
import com.example.ui.theme.NoveliteSecondaryAccent
import com.example.ui.theme.NoveliteSecondaryText
import com.example.ui.theme.NoveliteSoftAccentBg
import com.example.ui.theme.NoveliteWarmWhite

/**
 * Novelite Official Iconography System
 * Strictly adheres to Novelite official 7-color palette:
 * - #4A2C2A (NovelitePrimaryText / Deep Cocoa) for primary
 * - #C88577 (NoveliteSecondaryAccent / Warm Muted Rose) for secondary
 * - #D49A89 (NovelitePrimaryAccent / Dusty Rose) for accents
 * - #F5E1DA (NoveliteSoftAccentBg / Blush Parchment) for soft surfaces
 * - #6B7280 (NoveliteSecondaryText / Soft Gray) for muted/inactive
 * - #FFFFFF (NoveliteWarmWhite) for light surfaces
 */

// =========================================================================
// 1. THE READING ROOM ICON
// Concept: A child sitting comfortably on a magical flying carpet while deeply immersed in reading an open book.
// Emotional message: "A book can take you anywhere."
// =========================================================================

@Composable
fun ReadingRoomIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  enableAnimation: Boolean = false,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  val floatOffset = 0f

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_the_reading_room")
  ) {
    val w = this.size.width
    val h = this.size.height
    val offsetY = floatOffset * (h / 24f)

    // 1. Magical Flying Carpet (Curved woven wave carrying the reader)
    val carpetPath = Path().apply {
      moveTo(w * 0.10f, h * 0.78f + offsetY)
      cubicTo(
        w * 0.28f, h * 0.88f + offsetY,
        w * 0.52f, h * 0.68f + offsetY,
        w * 0.88f, h * 0.76f + offsetY
      )
      cubicTo(
        w * 0.94f, h * 0.78f + offsetY,
        w * 0.92f, h * 0.84f + offsetY,
        w * 0.85f, h * 0.85f + offsetY
      )
      cubicTo(
        w * 0.50f, h * 0.76f + offsetY,
        w * 0.26f, h * 0.94f + offsetY,
        w * 0.08f, h * 0.84f + offsetY
      )
      close()
    }
    drawPath(path = carpetPath, color = primary, style = Fill)

    // Carpet Fringe / Tassels (left & right)
    drawLine(
      color = accent,
      start = Offset(w * 0.08f, h * 0.84f + offsetY),
      end = Offset(w * 0.03f, h * 0.89f + offsetY),
      strokeWidth = w * 0.05f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = accent,
      start = Offset(w * 0.88f, h * 0.78f + offsetY),
      end = Offset(w * 0.94f, h * 0.82f + offsetY),
      strokeWidth = w * 0.05f,
      cap = StrokeCap.Round
    )

    // 2. Child Reader (Sitting comfortably, leaning forward reading)
    // Head (imaginative profile, tilted gently towards book)
    drawCircle(
      color = primary,
      radius = w * 0.12f,
      center = Offset(w * 0.38f, h * 0.34f + offsetY)
    )

    // Child Torso & Legs (Curved seated posture on carpet)
    val bodyPath = Path().apply {
      moveTo(w * 0.38f, h * 0.44f + offsetY)
      cubicTo(
        w * 0.46f, h * 0.48f + offsetY,
        w * 0.48f, h * 0.60f + offsetY,
        w * 0.44f, h * 0.72f + offsetY
      )
      lineTo(w * 0.26f, h * 0.75f + offsetY)
      cubicTo(
        w * 0.22f, h * 0.65f + offsetY,
        w * 0.26f, h * 0.48f + offsetY,
        w * 0.38f, h * 0.44f + offsetY
      )
      close()
    }
    drawPath(path = bodyPath, color = secondary, style = Fill)

    // Child Arms outstretched holding the open book
    val armPath = Path().apply {
      moveTo(w * 0.38f, h * 0.50f + offsetY)
      cubicTo(
        w * 0.46f, h * 0.54f + offsetY,
        w * 0.56f, h * 0.54f + offsetY,
        w * 0.62f, h * 0.50f + offsetY
      )
    }
    drawPath(
      path = armPath,
      color = primary,
      style = Stroke(width = w * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 3. Open Book in Front of Child (Heart of storytelling)
    val leftBookPage = Path().apply {
      moveTo(w * 0.66f, h * 0.54f + offsetY)
      lineTo(w * 0.56f, h * 0.44f + offsetY)
      cubicTo(
        w * 0.58f, h * 0.38f + offsetY,
        w * 0.64f, h * 0.36f + offsetY,
        w * 0.70f, h * 0.40f + offsetY
      )
      lineTo(w * 0.72f, h * 0.52f + offsetY)
      close()
    }
    drawPath(path = leftBookPage, color = accent, style = Fill)

    val rightBookPage = Path().apply {
      moveTo(w * 0.72f, h * 0.52f + offsetY)
      lineTo(w * 0.70f, h * 0.40f + offsetY)
      cubicTo(
        w * 0.76f, h * 0.36f + offsetY,
        w * 0.82f, h * 0.38f + offsetY,
        w * 0.84f, h * 0.44f + offsetY
      )
      lineTo(w * 0.76f, h * 0.54f + offsetY)
      close()
    }
    drawPath(path = rightBookPage, color = primary, style = Fill)

    // 4. Dreamlike Flight Star Accents
    drawCircle(
      color = accent,
      radius = w * 0.04f,
      center = Offset(w * 0.16f, h * 0.62f + offsetY)
    )
    drawCircle(
      color = secondary,
      radius = w * 0.03f,
      center = Offset(w * 0.84f, h * 0.28f + offsetY)
    )
    drawCircle(
      color = accent,
      radius = w * 0.025f,
      center = Offset(w * 0.72f, h * 0.22f + offsetY)
    )
  }
}

// =========================================================================
// 2. ODYSSEY ICON
// Concept: A refined compass, discovery path, and navigational star.
// =========================================================================

@Composable
fun OdysseyIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_odyssey")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Outer Compass Exploration Ring
    drawCircle(
      color = primary,
      radius = w * 0.42f,
      center = Offset(w * 0.5f, h * 0.5f),
      style = Stroke(width = w * 0.07f)
    )

    // Directional Compass Rose Star (North, South, East, West)
    // North Pointer (Accent pointing up)
    val northNeedle = Path().apply {
      moveTo(w * 0.5f, h * 0.12f)
      lineTo(w * 0.58f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.44f)
      close()
    }
    drawPath(path = northNeedle, color = accent, style = Fill)

    val northNeedleShadow = Path().apply {
      moveTo(w * 0.5f, h * 0.12f)
      lineTo(w * 0.42f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.44f)
      close()
    }
    drawPath(path = northNeedleShadow, color = primary, style = Fill)

    // South Needle
    val southNeedle = Path().apply {
      moveTo(w * 0.5f, h * 0.88f)
      lineTo(w * 0.42f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.56f)
      close()
    }
    drawPath(path = southNeedle, color = secondary, style = Fill)

    val southNeedleLight = Path().apply {
      moveTo(w * 0.5f, h * 0.88f)
      lineTo(w * 0.58f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.56f)
      close()
    }
    drawPath(path = southNeedleLight, color = accent, style = Fill)

    // East & West Points
    val eastNeedle = Path().apply {
      moveTo(w * 0.88f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.44f)
      lineTo(w * 0.56f, h * 0.5f)
      close()
    }
    drawPath(path = eastNeedle, color = secondary, style = Fill)

    val westNeedle = Path().apply {
      moveTo(w * 0.12f, h * 0.5f)
      lineTo(w * 0.5f, h * 0.56f)
      lineTo(w * 0.44f, h * 0.5f)
      close()
    }
    drawPath(path = westNeedle, color = secondary, style = Fill)

    // Center Navigational Pivot
    drawCircle(
      color = NoveliteWarmWhite,
      radius = w * 0.08f,
      center = Offset(w * 0.5f, h * 0.5f)
    )
    drawCircle(
      color = primary,
      radius = w * 0.05f,
      center = Offset(w * 0.5f, h * 0.5f)
    )
  }
}

// =========================================================================
// 3. THE STORY UNFOLDS ICON
// Concept: An open book, unfolding layered pages gradually opening into a story.
// =========================================================================

@Composable
fun TheStoryUnfoldsIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_the_story_unfolds")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Spine Curve
    val spinePath = Path().apply {
      moveTo(w * 0.5f, h * 0.32f)
      lineTo(w * 0.5f, h * 0.88f)
    }
    drawPath(path = spinePath, color = primary, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))

    // Left outer page (unfolding)
    val leftOuter = Path().apply {
      moveTo(w * 0.5f, h * 0.88f)
      cubicTo(w * 0.35f, h * 0.84f, w * 0.18f, h * 0.88f, w * 0.10f, h * 0.82f)
      lineTo(w * 0.10f, h * 0.36f)
      cubicTo(w * 0.22f, h * 0.40f, w * 0.36f, h * 0.34f, w * 0.5f, h * 0.38f)
      close()
    }
    drawPath(path = leftOuter, color = secondary, style = Fill)

    // Right outer page
    val rightOuter = Path().apply {
      moveTo(w * 0.5f, h * 0.88f)
      cubicTo(w * 0.65f, h * 0.84f, w * 0.82f, h * 0.88f, w * 0.90f, h * 0.82f)
      lineTo(w * 0.90f, h * 0.36f)
      cubicTo(w * 0.78f, h * 0.40f, w * 0.64f, h * 0.34f, w * 0.5f, h * 0.38f)
      close()
    }
    drawPath(path = rightOuter, color = primary, style = Fill)

    // Floating / Turning Unfolding Page (Accented layered sheet lifting up)
    val unfoldingPage = Path().apply {
      moveTo(w * 0.5f, h * 0.86f)
      cubicTo(w * 0.58f, h * 0.72f, w * 0.74f, h * 0.58f, w * 0.82f, h * 0.22f)
      cubicTo(w * 0.70f, h * 0.18f, w * 0.56f, h * 0.26f, w * 0.5f, h * 0.34f)
      close()
    }
    drawPath(path = unfoldingPage, color = accent, style = Fill)
    drawPath(path = unfoldingPage, color = primary, style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))
  }
}

// =========================================================================
// 4. INKLINGS ICON
// Concept: Overlapping conversation bubbles, thoughts, and dialogue symbols.
// =========================================================================

@Composable
fun InklingsIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_inklings")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Background Thought Bubble (Secondary)
    val backBubble = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.30f,
          top = h * 0.12f,
          right = w * 0.90f,
          bottom = h * 0.58f,
          cornerRadius = CornerRadius(w * 0.16f, h * 0.16f)
        )
      )
    }
    drawPath(path = backBubble, color = secondary, style = Fill)

    // Back Bubble Tail
    val backTail = Path().apply {
      moveTo(w * 0.75f, h * 0.58f)
      lineTo(w * 0.86f, h * 0.72f)
      lineTo(w * 0.65f, h * 0.58f)
      close()
    }
    drawPath(path = backTail, color = secondary, style = Fill)

    // Foreground Primary Dialogue Bubble
    val frontBubble = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.10f,
          top = h * 0.36f,
          right = w * 0.72f,
          bottom = h * 0.82f,
          cornerRadius = CornerRadius(w * 0.16f, h * 0.16f)
        )
      )
    }
    drawPath(path = frontBubble, color = primary, style = Fill)

    // Front Bubble Tail
    val frontTail = Path().apply {
      moveTo(w * 0.20f, h * 0.82f)
      lineTo(w * 0.14f, h * 0.96f)
      lineTo(w * 0.34f, h * 0.82f)
      close()
    }
    drawPath(path = frontTail, color = primary, style = Fill)

    // Thought Dots / Glyphs Inside Front Bubble
    drawCircle(color = accent, radius = w * 0.04f, center = Offset(w * 0.26f, h * 0.58f))
    drawCircle(color = NoveliteWarmWhite, radius = w * 0.04f, center = Offset(w * 0.41f, h * 0.58f))
    drawCircle(color = accent, radius = w * 0.04f, center = Offset(w * 0.56f, h * 0.58f))
  }
}

// =========================================================================
// 5. FOOTPRINTS ON THE PAGE ICON
// Concept: Elegant footprints & gentle trail moving across an open book page.
// =========================================================================

@Composable
fun FootprintsOnPageIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_footprints_on_page")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Open Page / Parchment Frame
    val pagePath = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.14f,
          top = h * 0.12f,
          right = w * 0.86f,
          bottom = h * 0.88f,
          cornerRadius = CornerRadius(w * 0.12f, h * 0.12f)
        )
      )
    }
    drawPath(path = pagePath, color = NoveliteWarmWhite, style = Fill)
    drawPath(path = pagePath, color = primary, style = Stroke(width = w * 0.06f))

    // Story Text Lines on Page
    drawLine(
      color = secondary,
      start = Offset(w * 0.24f, h * 0.26f),
      end = Offset(w * 0.76f, h * 0.26f),
      strokeWidth = w * 0.04f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = secondary,
      start = Offset(w * 0.24f, h * 0.44f),
      end = Offset(w * 0.50f, h * 0.44f),
      strokeWidth = w * 0.04f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = secondary,
      start = Offset(w * 0.48f, h * 0.62f),
      end = Offset(w * 0.76f, h * 0.62f),
      strokeWidth = w * 0.04f,
      cap = StrokeCap.Round
    )

    // Stepping Footprints / Reaction Trail Traveling Upwards
    // Step 1 (Bottom Left)
    drawOval(
      color = accent,
      topLeft = Offset(w * 0.28f, h * 0.68f),
      size = Size(w * 0.10f, h * 0.14f)
    )
    drawCircle(color = accent, radius = w * 0.025f, center = Offset(w * 0.33f, h * 0.64f))

    // Step 2 (Center Right)
    drawOval(
      color = primary,
      topLeft = Offset(w * 0.46f, h * 0.48f),
      size = Size(w * 0.10f, h * 0.14f)
    )
    drawCircle(color = primary, radius = w * 0.025f, center = Offset(w * 0.51f, h * 0.44f))

    // Step 3 (Top Left-Center)
    drawOval(
      color = accent,
      topLeft = Offset(w * 0.58f, h * 0.28f),
      size = Size(w * 0.10f, h * 0.14f)
    )
    drawCircle(color = accent, radius = w * 0.025f, center = Offset(w * 0.63f, h * 0.24f))
  }
}

// =========================================================================
// 6. LET THE QUILL FLOW ICON
// Concept: A refined fountain quill feather pen with flowing ink stream.
// =========================================================================

@Composable
fun LetTheQuillFlowIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_let_the_quill_flow")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Feather Quill Spine & Vane
    val featherPath = Path().apply {
      moveTo(w * 0.82f, h * 0.10f)
      cubicTo(w * 0.60f, h * 0.15f, w * 0.34f, h * 0.36f, w * 0.28f, h * 0.65f)
      lineTo(w * 0.36f, h * 0.62f)
      cubicTo(w * 0.46f, h * 0.42f, w * 0.66f, h * 0.28f, w * 0.82f, h * 0.10f)
      close()
    }
    drawPath(path = featherPath, color = primary, style = Fill)

    val featherSecondaryVane = Path().apply {
      moveTo(w * 0.82f, h * 0.10f)
      cubicTo(w * 0.88f, h * 0.26f, w * 0.78f, h * 0.48f, w * 0.44f, h * 0.66f)
      lineTo(w * 0.36f, h * 0.62f)
      cubicTo(w * 0.60f, h * 0.48f, w * 0.74f, h * 0.30f, w * 0.82f, h * 0.10f)
      close()
    }
    drawPath(path = featherSecondaryVane, color = secondary, style = Fill)

    // Quill Shaft & Nib (Pointing down to parchment)
    val nibPath = Path().apply {
      moveTo(w * 0.32f, h * 0.62f)
      lineTo(w * 0.22f, h * 0.78f)
      lineTo(w * 0.26f, h * 0.80f)
      lineTo(w * 0.38f, h * 0.66f)
      close()
    }
    drawPath(path = nibPath, color = accent, style = Fill)

    // Flowing Calligraphy Ink Ribbon Below Nib
    val inkFlow = Path().apply {
      moveTo(w * 0.22f, h * 0.80f)
      cubicTo(
        w * 0.12f, h * 0.86f,
        w * 0.36f, h * 0.94f,
        w * 0.58f, h * 0.86f
      )
      cubicTo(
        w * 0.74f, h * 0.80f,
        w * 0.86f, h * 0.88f,
        w * 0.90f, h * 0.92f
      )
    }
    drawPath(
      path = inkFlow,
      color = primary,
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // Small Ink Spark / Droplet
    drawCircle(
      color = accent,
      radius = w * 0.035f,
      center = Offset(w * 0.14f, h * 0.74f)
    )
  }
}

// =========================================================================
// 7. VERSES I HAVE WOVEN ICON
// Concept: Woven interconnected story threads intertwining with manuscripts.
// =========================================================================

@Composable
fun VersesIHaveWovenIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_verses_i_have_woven")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Layered Manuscript Page Base
    val doc1 = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.22f,
          top = h * 0.16f,
          right = w * 0.82f,
          bottom = h * 0.86f,
          cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
        )
      )
    }
    drawPath(path = doc1, color = NoveliteWarmWhite, style = Fill)
    drawPath(path = doc1, color = primary, style = Stroke(width = w * 0.06f))

    // Intertwined Woven Golden Thread 1 (Looping vertically)
    val thread1 = Path().apply {
      moveTo(w * 0.14f, h * 0.32f)
      cubicTo(w * 0.44f, h * 0.20f, w * 0.60f, h * 0.50f, w * 0.90f, h * 0.38f)
    }
    drawPath(
      path = thread1,
      color = accent,
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )

    // Intertwined Woven Thread 2 (Crossing ribbon)
    val thread2 = Path().apply {
      moveTo(w * 0.14f, h * 0.66f)
      cubicTo(w * 0.40f, h * 0.80f, w * 0.64f, h * 0.48f, w * 0.88f, h * 0.64f)
    }
    drawPath(
      path = thread2,
      color = secondary,
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )

    // Central Woven Knot Spark
    drawCircle(
      color = primary,
      radius = w * 0.06f,
      center = Offset(w * 0.52f, h * 0.52f)
    )
    drawCircle(
      color = accent,
      radius = w * 0.035f,
      center = Offset(w * 0.52f, h * 0.52f)
    )
  }
}

// =========================================================================
// 8. AN AUTHOR'S REFLECTION ICON
// Concept: An elegant reflective mirror showing radiant growth and creative insight.
// =========================================================================

@Composable
fun AnAuthorsReflectionIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_an_authors_reflection")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Mirror Oval Frame
    val mirrorFrame = Path().apply {
      addOval(
        androidx.compose.ui.geometry.Rect(
          left = w * 0.16f,
          top = h * 0.10f,
          right = w * 0.84f,
          bottom = h * 0.76f
        )
      )
    }
    drawPath(path = mirrorFrame, color = primary, style = Stroke(width = w * 0.07f))

    // Mirror Glass Surface Fill
    drawOval(
      color = NoveliteSoftAccentBg,
      topLeft = Offset(w * 0.20f, h * 0.14f),
      size = Size(w * 0.60f, h * 0.58f)
    )

    // Radiant Reflection Glint across glass
    val glint1 = Path().apply {
      moveTo(w * 0.32f, h * 0.28f)
      lineTo(w * 0.44f, h * 0.20f)
    }
    drawPath(path = glint1, color = accent, style = Stroke(width = w * 0.05f, cap = StrokeCap.Round))

    // Growth Sprout / Thoughtful Spark emerging from reflection center
    val sprout = Path().apply {
      moveTo(w * 0.50f, h * 0.58f)
      cubicTo(w * 0.48f, h * 0.44f, w * 0.42f, h * 0.36f, w * 0.50f, h * 0.28f)
      cubicTo(w * 0.58f, h * 0.36f, w * 0.52f, h * 0.44f, w * 0.50f, h * 0.58f)
      close()
    }
    drawPath(path = sprout, color = accent, style = Fill)

    // Mirror Handle Base at bottom
    drawLine(
      color = primary,
      start = Offset(w * 0.50f, h * 0.76f),
      end = Offset(w * 0.50f, h * 0.94f),
      strokeWidth = w * 0.08f,
      cap = StrokeCap.Round
    )
    drawCircle(
      color = accent,
      radius = w * 0.05f,
      center = Offset(w * 0.50f, h * 0.94f)
    )
  }
}

// =========================================================================
// 9. BETWEEN THE LINES ICON
// Concept: Immersive perspective into flowing lines of text and depth of a book.
// =========================================================================

@Composable
fun BetweenTheLinesIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_between_the_lines")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Flowing Open Story Depth Curves (Top & Bottom)
    val topBookArc = Path().apply {
      moveTo(w * 0.12f, h * 0.24f)
      cubicTo(w * 0.34f, h * 0.32f, w * 0.66f, h * 0.16f, w * 0.88f, h * 0.24f)
    }
    drawPath(path = topBookArc, color = primary, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))

    val bottomBookArc = Path().apply {
      moveTo(w * 0.12f, h * 0.76f)
      cubicTo(w * 0.34f, h * 0.84f, w * 0.66f, h * 0.68f, w * 0.88f, h * 0.76f)
    }
    drawPath(path = bottomBookArc, color = primary, style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))

    // Lines of Text "Between the Lines"
    drawLine(
      color = secondary,
      start = Offset(w * 0.22f, h * 0.38f),
      end = Offset(w * 0.78f, h * 0.38f),
      strokeWidth = w * 0.05f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = accent,
      start = Offset(w * 0.18f, h * 0.50f),
      end = Offset(w * 0.82f, h * 0.50f),
      strokeWidth = w * 0.06f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = secondary,
      start = Offset(w * 0.26f, h * 0.62f),
      end = Offset(w * 0.74f, h * 0.62f),
      strokeWidth = w * 0.05f,
      cap = StrokeCap.Round
    )
  }
}

// =========================================================================
// 10. WHERE I WANDER NOW (Currently Reading)
// Concept: An open book with a journey path flowing through the pages.
// =========================================================================

@Composable
fun WhereIWanderNowIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_where_i_wander_now")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Open Book Silhouette
    val leftPage = Path().apply {
      moveTo(w * 0.50f, h * 0.78f)
      cubicTo(w * 0.36f, h * 0.72f, w * 0.22f, h * 0.78f, w * 0.10f, h * 0.72f)
      lineTo(w * 0.10f, h * 0.28f)
      cubicTo(w * 0.22f, h * 0.34f, w * 0.36f, h * 0.28f, w * 0.50f, h * 0.34f)
      close()
    }
    drawPath(path = leftPage, color = secondary, style = Fill)
    drawPath(path = leftPage, color = primary, style = Stroke(width = w * 0.05f))

    val rightPage = Path().apply {
      moveTo(w * 0.50f, h * 0.78f)
      cubicTo(w * 0.64f, h * 0.72f, w * 0.78f, h * 0.78f, w * 0.90f, h * 0.72f)
      lineTo(w * 0.90f, h * 0.28f)
      cubicTo(w * 0.78f, h * 0.34f, w * 0.64f, h * 0.28f, w * 0.50f, h * 0.34f)
      close()
    }
    drawPath(path = rightPage, color = NoveliteWarmWhite, style = Fill)
    drawPath(path = rightPage, color = primary, style = Stroke(width = w * 0.05f))

    // Journey Path Winding Through Pages
    val wanderingPath = Path().apply {
      moveTo(w * 0.22f, h * 0.64f)
      cubicTo(w * 0.38f, h * 0.44f, w * 0.60f, h * 0.60f, w * 0.78f, h * 0.38f)
    }
    drawPath(
      path = wanderingPath,
      color = accent,
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )

    // Wanderer Star / Compass Marker on path
    drawCircle(
      color = primary,
      radius = w * 0.05f,
      center = Offset(w * 0.78f, h * 0.38f)
    )
  }
}

// =========================================================================
// 11. TREASURED BETWEEN THE LINES (Favourites)
// Concept: A warm book with a treasured heart held between pages.
// =========================================================================

@Composable
fun TreasuredBetweenTheLinesIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_treasured_between_the_lines")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Book Base
    val leftPage = Path().apply {
      moveTo(w * 0.50f, h * 0.86f)
      cubicTo(w * 0.34f, h * 0.80f, w * 0.20f, h * 0.84f, w * 0.12f, h * 0.78f)
      lineTo(w * 0.12f, h * 0.38f)
      cubicTo(w * 0.22f, h * 0.44f, w * 0.34f, h * 0.38f, w * 0.50f, h * 0.44f)
      close()
    }
    drawPath(path = leftPage, color = secondary, style = Fill)
    drawPath(path = leftPage, color = primary, style = Stroke(width = w * 0.05f))

    val rightPage = Path().apply {
      moveTo(w * 0.50f, h * 0.86f)
      cubicTo(w * 0.66f, h * 0.80f, w * 0.80f, h * 0.84f, w * 0.88f, h * 0.78f)
      lineTo(w * 0.88f, h * 0.38f)
      cubicTo(w * 0.78f, h * 0.44f, w * 0.66f, h * 0.38f, w * 0.50f, h * 0.44f)
      close()
    }
    drawPath(path = rightPage, color = secondary, style = Fill)
    drawPath(path = rightPage, color = primary, style = Stroke(width = w * 0.05f))

    // Treasured Heart Held Above/Between Pages
    val heartPath = Path().apply {
      moveTo(w * 0.50f, h * 0.46f)
      cubicTo(w * 0.40f, h * 0.34f, w * 0.28f, h * 0.24f, w * 0.36f, h * 0.14f)
      cubicTo(w * 0.44f, h * 0.06f, w * 0.50f, h * 0.18f, w * 0.50f, h * 0.22f)
      cubicTo(w * 0.50f, h * 0.18f, w * 0.56f, h * 0.06f, w * 0.64f, h * 0.14f)
      cubicTo(w * 0.72f, h * 0.24f, w * 0.60f, h * 0.34f, w * 0.50f, h * 0.46f)
      close()
    }
    drawPath(path = heartPath, color = accent, style = Fill)
    drawPath(path = heartPath, color = primary, style = Stroke(width = w * 0.04f))
  }
}

// =========================================================================
// 12. PAGES YET TO BE TURNED (Want to Read)
// Concept: An unopened book with a hanging bookmark ribbon waiting to turn.
// =========================================================================

@Composable
fun PagesYetToBeTurnedIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_pages_yet_to_be_turned")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Closed Tome Silhouette
    val tomePath = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.22f,
          top = h * 0.14f,
          right = w * 0.78f,
          bottom = h * 0.86f,
          cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
        )
      )
    }
    drawPath(path = tomePath, color = primary, style = Fill)

    // Inner Pages block edge
    drawRect(
      color = NoveliteWarmWhite,
      topLeft = Offset(w * 0.68f, h * 0.18f),
      size = Size(w * 0.06f, h * 0.64f)
    )

    // Bookmark Ribbon Hanging From Top
    val bookmark = Path().apply {
      moveTo(w * 0.38f, h * 0.12f)
      lineTo(w * 0.54f, h * 0.12f)
      lineTo(w * 0.54f, h * 0.58f)
      lineTo(w * 0.46f, h * 0.50f)
      lineTo(w * 0.38f, h * 0.58f)
      close()
    }
    drawPath(path = bookmark, color = accent, style = Fill)

    // Turning Corner Accent on bottom right
    val cornerFold = Path().apply {
      moveTo(w * 0.62f, h * 0.86f)
      lineTo(w * 0.78f, h * 0.70f)
      lineTo(w * 0.62f, h * 0.70f)
      close()
    }
    drawPath(path = cornerFold, color = secondary, style = Fill)
  }
}

// =========================================================================
// 13. STORIES THAT STAYED (Completed)
// Concept: A finished closed book with a lingering memory heart and gentle star sparkle.
// =========================================================================

@Composable
fun StoriesThatStayedIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_stories_that_stayed")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Closed Tome with Spine & Accent Embossing
    val bookCover = Path().apply {
      addRoundRect(
        RoundRect(
          left = w * 0.16f,
          top = h * 0.22f,
          right = w * 0.84f,
          bottom = h * 0.88f,
          cornerRadius = CornerRadius(w * 0.08f, h * 0.08f)
        )
      )
    }
    drawPath(path = bookCover, color = primary, style = Fill)

    // Spine border
    drawLine(
      color = accent,
      start = Offset(w * 0.28f, h * 0.22f),
      end = Offset(w * 0.28f, h * 0.88f),
      strokeWidth = w * 0.04f
    )

    // Embossed Memory Seal in Center of Cover
    drawCircle(
      color = accent,
      radius = w * 0.12f,
      center = Offset(w * 0.56f, h * 0.55f)
    )
    drawCircle(
      color = primary,
      radius = w * 0.07f,
      center = Offset(w * 0.56f, h * 0.55f)
    )

    // Emerging Sparkles / Lingering memory stars
    val sparkle1 = Path().apply {
      moveTo(w * 0.80f, h * 0.10f)
      lineTo(w * 0.82f, h * 0.16f)
      lineTo(w * 0.88f, h * 0.18f)
      lineTo(w * 0.82f, h * 0.20f)
      lineTo(w * 0.80f, h * 0.26f)
      lineTo(w * 0.78f, h * 0.20f)
      lineTo(w * 0.72f, h * 0.18f)
      lineTo(w * 0.78f, h * 0.16f)
      close()
    }
    drawPath(path = sparkle1, color = accent, style = Fill)

    // Sparkle 2
    drawCircle(
      color = accent,
      radius = w * 0.035f,
      center = Offset(w * 0.38f, h * 0.12f)
    )
  }
}

// =========================================================================
// 14. SHELVES OF MY MAKING (Collections)
// Concept: Organized bookshelf holding personal gathered worlds of stories.
// =========================================================================

@Composable
fun ShelvesOfMyMakingIcon(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  isSelected: Boolean = true,
  tint: Color? = null
) {
  val primary = tint ?: if (isSelected) NovelitePrimaryText else NoveliteSecondaryText
  val secondary = tint ?: if (isSelected) NoveliteSecondaryAccent else NoveliteSecondaryText
  val accent = tint ?: if (isSelected) NovelitePrimaryAccent else NoveliteSecondaryText

  Canvas(
    modifier = modifier
      .size(size)
      .testTag("icon_shelves_of_my_making")
  ) {
    val w = this.size.width
    val h = this.size.height

    // Shelf Plank (Base)
    drawRoundRect(
      color = primary,
      topLeft = Offset(w * 0.08f, h * 0.78f),
      size = Size(w * 0.84f, h * 0.10f),
      cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
    )

    // Book 1 (Left Tall Tome)
    drawRoundRect(
      color = secondary,
      topLeft = Offset(w * 0.14f, h * 0.24f),
      size = Size(w * 0.14f, h * 0.54f),
      cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
    )
    // Book 1 spine stripe
    drawLine(
      color = accent,
      start = Offset(w * 0.17f, h * 0.32f),
      end = Offset(w * 0.25f, h * 0.32f),
      strokeWidth = w * 0.03f
    )

    // Book 2 (Medium Accent Tome)
    drawRoundRect(
      color = accent,
      topLeft = Offset(w * 0.32f, h * 0.36f),
      size = Size(w * 0.14f, h * 0.42f),
      cornerRadius = CornerRadius(w * 0.03f, h * 0.03f)
    )

    // Book 3 (Slanted Book leaning right)
    val slantedBook = Path().apply {
      moveTo(w * 0.50f, h * 0.78f)
      lineTo(w * 0.62f, h * 0.78f)
      lineTo(w * 0.76f, h * 0.38f)
      lineTo(w * 0.64f, h * 0.38f)
      close()
    }
    drawPath(path = slantedBook, color = primary, style = Fill)

    // Bookend on Right
    val bookend = Path().apply {
      moveTo(w * 0.78f, h * 0.78f)
      lineTo(w * 0.86f, h * 0.78f)
      lineTo(w * 0.86f, h * 0.50f)
      lineTo(w * 0.82f, h * 0.50f)
      lineTo(w * 0.82f, h * 0.74f)
      lineTo(w * 0.78f, h * 0.74f)
      close()
    }
    drawPath(path = bookend, color = secondary, style = Fill)
  }
}
