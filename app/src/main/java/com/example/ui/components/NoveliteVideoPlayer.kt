package com.example.ui.components

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun NoveliteVideoPlayer(
  videoUrl: String,
  modifier: Modifier = Modifier,
  autoPlay: Boolean = false,
  aspectRatio: Float = 16f / 9f,
  title: String? = null,
  showControls: Boolean = true,
  onRemove: (() -> Unit)? = null
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(autoPlay) }
  var isMuted by remember { mutableStateOf(false) }
  var currentPositionMs by remember { mutableStateOf(0) }
  var durationMs by remember { mutableStateOf(0) }
  var isControlsVisible by remember { mutableStateOf(true) }
  var isBuffering by remember { mutableStateOf(true) }
  var hasError by remember { mutableStateOf(false) }
  var isCompleted by remember { mutableStateOf(false) }

  var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
  var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

  // Auto hide controls after 4 seconds of inactivity if playing
  LaunchedEffect(isControlsVisible, isPlaying) {
    if (isControlsVisible && isPlaying) {
      delay(4000)
      isControlsVisible = false
    }
  }

  // Position poll
  LaunchedEffect(videoViewRef, isPlaying) {
    while (isActive) {
      videoViewRef?.let { vv ->
        if (vv.isPlaying) {
          currentPositionMs = vv.currentPosition
          if (durationMs == 0 && vv.duration > 0) {
            durationMs = vv.duration
          }
        }
      }
      delay(300)
    }
  }

  Box(
    modifier = modifier
      .aspectRatio(aspectRatio)
      .clip(RoundedCornerShape(16.dp))
      .background(NoveliteDarkBrown)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
      ) {
        isControlsVisible = !isControlsVisible
      }
      .testTag("novelite_video_player")
  ) {
    // Video View
    AndroidView(
      factory = { ctx ->
        FrameLayout(ctx).apply {
          layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          val videoView = VideoView(ctx).apply {
            layoutParams = FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT
            )

            try {
              if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://") || videoUrl.startsWith("content://") || videoUrl.startsWith("file://")) {
                setVideoURI(Uri.parse(videoUrl))
              } else {
                setVideoPath(videoUrl)
              }
            } catch (e: Exception) {
              hasError = true
              isBuffering = false
            }

            setOnPreparedListener { mp ->
              mediaPlayerRef = mp
              durationMs = mp.duration
              isBuffering = false
              hasError = false
              mp.isLooping = false
              if (autoPlay) {
                mp.start()
                isPlaying = true
              }
            }

            setOnCompletionListener {
              isPlaying = false
              isCompleted = true
              isControlsVisible = true
            }

            setOnErrorListener { _, _, _ ->
              hasError = true
              isBuffering = false
              true
            }
          }
          videoViewRef = videoView
          addView(videoView)
        }
      },
      update = { _ ->
        // No-op or updates
      },
      modifier = Modifier.fillMaxSize()
    )

    // Buffering indicator
    if (isBuffering && !hasError) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CircularProgressIndicator(
            color = NoveliteCaramel,
            modifier = Modifier.size(36.dp),
            strokeWidth = 3.dp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Loading Teaser...",
            color = NoveliteWhite,
            fontSize = 11.sp,
            fontFamily = FontFamily.Serif
          )
        }
      }
    }

    // Error State
    if (hasError) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(NoveliteDarkBrown.copy(alpha = 0.9f))
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.VideocamOff,
            contentDescription = null,
            tint = NoveliteWarmBrown,
            modifier = Modifier.size(40.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Teaser Preview Unavailable",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Video preview could not be loaded or is in an unsupported codec.",
            color = NoveliteCardBeige,
            fontSize = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = {
              hasError = false
              isBuffering = true
              videoViewRef?.let { vv ->
                try {
                  if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://") || videoUrl.startsWith("content://") || videoUrl.startsWith("file://")) {
                    vv.setVideoURI(Uri.parse(videoUrl))
                  } else {
                    vv.setVideoPath(videoUrl)
                  }
                  vv.start()
                  isPlaying = true
                } catch (_: Exception) {}
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NoveliteCaramel),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text("Retry Playback", fontSize = 11.sp, color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Controls Overlay
    AnimatedVisibility(
      visible = isControlsVisible && !hasError,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier.fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.45f))
          .padding(12.dp)
      ) {
        // Top Bar (Title & Remove / Mute)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              color = NoveliteCaramel,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "🎬 STORY TEASER",
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = NoveliteDarkBrown
              )
            }
            if (title != null) {
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = title,
                color = NoveliteWhite,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            // Mute toggle
            IconButton(
              onClick = {
                mediaPlayerRef?.let { mp ->
                  isMuted = !isMuted
                  val vol = if (isMuted) 0f else 1f
                  mp.setVolume(vol, vol)
                }
              },
              modifier = Modifier
                .size(32.dp)
                .background(NoveliteDarkBrown.copy(alpha = 0.6f), CircleShape)
            ) {
              Icon(
                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = NoveliteWhite,
                modifier = Modifier.size(16.dp)
              )
            }

            if (onRemove != null) {
              Spacer(modifier = Modifier.width(6.dp))
              IconButton(
                onClick = onRemove,
                modifier = Modifier
                  .size(32.dp)
                  .background(NoveliteDarkBrown.copy(alpha = 0.6f), CircleShape)
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Remove video",
                  tint = NoveliteCardBeige,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }

        // Center Play / Pause / Replay Button
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(52.dp)
            .clip(CircleShape)
            .background(NoveliteDarkBrown.copy(alpha = 0.85f))
            .clickable {
              videoViewRef?.let { vv ->
                if (isCompleted) {
                  vv.seekTo(0)
                  vv.start()
                  isPlaying = true
                  isCompleted = false
                } else if (isPlaying) {
                  vv.pause()
                  isPlaying = false
                } else {
                  vv.start()
                  isPlaying = true
                }
              }
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = when {
              isCompleted -> Icons.Default.Replay
              isPlaying -> Icons.Default.Pause
              else -> Icons.Default.PlayArrow
            },
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = NoveliteWhite,
            modifier = Modifier.size(28.dp)
          )
        }

        // Bottom Progress & Time Controls
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = formatTime(currentPositionMs),
              fontSize = 10.sp,
              color = NoveliteWhite,
              fontWeight = FontWeight.Bold
            )

            Text(
              text = formatTime(durationMs),
              fontSize = 10.sp,
              color = NoveliteCardBeige
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          // Progress Bar
          val progress = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = NoveliteCaramel,
            trackColor = NoveliteDarkBrown.copy(alpha = 0.5f)
          )
        }
      }
    }
  }
}

private fun formatTime(millis: Int): String {
  val totalSeconds = millis / 1000
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  return String.format("%02d:%02d", minutes, seconds)
}
