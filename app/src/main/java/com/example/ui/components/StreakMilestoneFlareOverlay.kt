package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteSoftAccentBg
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class SparkleParticle(
  val initialX: Float,
  val initialY: Float,
  val targetX: Float,
  val targetY: Float,
  val size: Float,
  val color: Color,
  val rotationSpeed: Float,
  val shapeType: Int // 0: Star, 1: Circle, 2: Diamond, 3: Ribbon
)

/**
 * Subtle Confetti and Flare Animation for the StreakManager UI
 * Displays an elegant particle burst and radial glow when daily goals or streak milestones are achieved.
 */
@Composable
fun StreakMilestoneFlareOverlay(
  isVisible: Boolean,
  onDismiss: () -> Unit = {},
  durationMs: Long = 2800L,
  modifier: Modifier = Modifier
) {
  val animationProgress = remember { Animatable(0f) }
  val flareAlpha = remember { Animatable(0f) }

  // Generate deterministic particles for beautiful burst effect
  val particles = remember {
    val rng = Random(42)
    val colors = listOf(
      NoveliteCaramel,
      Color(0xFFE5A962),
      Color(0xFFFFD54F),
      NoveliteSoftAccentBg,
      Color(0xFFE89A88),
      NoveliteWarmBrown,
      NoveliteWhite
    )

    List(36) { index ->
      val angle = (index * (360f / 36f) + rng.nextFloat() * 12f) * (PI.toFloat() / 180f)
      val distance = 160f + rng.nextFloat() * 260f
      SparkleParticle(
        initialX = 0f,
        initialY = 0f,
        targetX = cos(angle) * distance,
        targetY = sin(angle) * distance + (rng.nextFloat() * 60f), // gentle gravity
        size = 5f + rng.nextFloat() * 9f,
        color = colors[rng.nextInt(colors.size)],
        rotationSpeed = (rng.nextFloat() - 0.5f) * 720f,
        shapeType = rng.nextInt(4)
      )
    }
  }

  LaunchedEffect(isVisible) {
    if (isVisible) {
      animationProgress.snapTo(0f)
      flareAlpha.snapTo(0f)

      // Animate flare pulse
      flareAlpha.animateTo(
        targetValue = 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
      )

      // Burst particles
      animationProgress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMs.toInt() - 300, easing = FastOutSlowInEasing)
      )

      flareAlpha.animateTo(
        targetValue = 0f,
        animationSpec = tween(400, easing = LinearEasing)
      )

      delay(100)
      onDismiss()
    }
  }

  AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn(tween(200)),
    exit = fadeOut(tween(300)),
    modifier = modifier.testTag("streak_milestone_flare_animation")
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      // Background Radial Flare Glow
      Canvas(
        modifier = Modifier
          .size(340.dp)
      ) {
        val radius = (size.minDimension / 2f) * (0.4f + animationProgress.value * 0.6f)
        val alpha = (1f - animationProgress.value) * flareAlpha.value * 0.75f

        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              NoveliteCaramel.copy(alpha = alpha * 0.65f),
              Color(0xFFFFD54F).copy(alpha = alpha * 0.35f),
              NoveliteSoftAccentBg.copy(alpha = alpha * 0.15f),
              Color.Transparent
            ),
            center = center,
            radius = radius
          ),
          radius = radius,
          center = center
        )
      }

      // Sparkle Confetti Particles
      Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val progress = animationProgress.value
        val fadeOutProgress = (1f - (progress * 1.05f).coerceIn(0f, 1f))

        particles.forEach { particle ->
          val currentX = centerX + particle.targetX * progress
          val currentY = centerY + particle.targetY * progress + (progress * progress * 80f) // gravity
          val currentRotation = particle.rotationSpeed * progress
          val currentAlpha = fadeOutProgress * particle.color.alpha

          if (currentAlpha > 0.02f) {
            rotate(degrees = currentRotation, pivot = Offset(currentX, currentY)) {
              when (particle.shapeType) {
                0 -> drawSparkleStar(Offset(currentX, currentY), particle.size * (1f - progress * 0.3f), particle.color.copy(alpha = currentAlpha))
                1 -> drawCircle(
                  color = particle.color.copy(alpha = currentAlpha),
                  radius = particle.size * 0.5f,
                  center = Offset(currentX, currentY)
                )
                2 -> drawDiamond(Offset(currentX, currentY), particle.size, particle.color.copy(alpha = currentAlpha))
                else -> drawRect(
                  color = particle.color.copy(alpha = currentAlpha),
                  topLeft = Offset(currentX - particle.size * 0.4f, currentY - particle.size * 0.8f),
                  size = androidx.compose.ui.geometry.Size(particle.size * 0.8f, particle.size * 1.6f)
                )
              }
            }
          }
        }
      }
    }
  }
}

private fun DrawScope.drawSparkleStar(center: Offset, size: Float, color: Color) {
  val path = Path().apply {
    moveTo(center.x, center.y - size)
    quadraticBezierTo(center.x, center.y, center.x + size, center.y)
    quadraticBezierTo(center.x, center.y, center.x, center.y + size)
    quadraticBezierTo(center.x, center.y, center.x - size, center.y)
    quadraticBezierTo(center.x, center.y, center.x, center.y - size)
    close()
  }
  drawPath(path, color)
}

private fun DrawScope.drawDiamond(center: Offset, size: Float, color: Color) {
  val path = Path().apply {
    moveTo(center.x, center.y - size)
    lineTo(center.x + size * 0.7f, center.y)
    lineTo(center.x, center.y + size)
    lineTo(center.x - size * 0.7f, center.y)
    close()
  }
  drawPath(path, color)
}
