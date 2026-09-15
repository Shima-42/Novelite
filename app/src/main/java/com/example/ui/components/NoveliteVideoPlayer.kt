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
import androidx.compose.ui.graphics.Brush
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
  var isStarted by remember { mutableStateOf(autoPlay) }
  var isPlaying by remember { mutableStateOf(autoPlay) }
  var isMuted by remember { mutableStateOf(false) }
  var currentPositionMs by remember { mutableIntStateOf(0) }
  var durationMs by remember { mutableIntStateOf(0) }
  var isControlsVisible by remember { mutableStateOf(true) }
  var isBuffering by remember { mutableStateOf(false) }
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

  // Safe position polling only while playing
  LaunchedEffect(isPlaying, isStarted) {
    while (isActive && isPlaying && isStarted) {
      try {
        videoViewRef?.let { vv ->
          if (vv.isPlaying) {
            currentPositionMs = vv.currentPosition
            if (durationMs == 0 && vv.duration > 0) {
              durationMs = vv.duration
            }
          }
        }
      } catch (_: Exception) {}
      delay(500)
    }
  }

  DisposableEffect(videoUrl) {
    onDispose {
      try {
        videoViewRef?.let { vv ->
          vv.setOnPreparedListener(null)
          vv.setOnErrorListener(null)
          vv.setOnCompletionListener(null)
          if (vv.isPlaying) {
            vv.stopPlayback()
          }
        }
      } catch (_: Exception) {}
      videoViewRef = null
      mediaPlayerRef = null
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
        if (isStarted) {
          isControlsVisible = !isControlsVisible
        } else {
          isStarted = true
          isBuffering = true
        }
      }
      .testTag("novelite_video_player")
  ) {
    if (isStarted) {
      // Lazy Video View
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

              setOnErrorListener { _, what, extra ->
                hasError = true
                isBuffering = false
                isPlaying = false
                mediaPlayerRef = null
                // Note: Never call stopPlayback() or stop() here since MediaPlayer is already in the ERROR state.
                true
              }

              setOnPreparedListener { mp ->
                try {
                  mediaPlayerRef = mp
                  durationMs = mp.duration
                  isBuffering = false
                  hasError = false
                  mp.isLooping = false
                  val vol = if (isMuted) 0f else 1f
                  mp.setVolume(vol, vol)
                  if (isPlaying) {
                    mp.start()
                  }
                } catch (_: Exception) {
                  hasError = true
                  isBuffering = false
                }
              }

              setOnCompletionListener {
                isPlaying = false
                isCompleted = true
                isControlsVisible = true
              }

              try {
                if (videoUrl.isNotBlank()) {
                  val uri = Uri.parse(videoUrl)
                  if (uri.scheme.isNullOrEmpty()) {
                    setVideoPath(videoUrl)
                  } else {
                    setVideoURI(uri)
                  }
                } else {
                  hasError = true
                  isBuffering = false
                }
              } catch (_: Exception) {
                hasError = true
                isBuffering = false
              }
            }
            videoViewRef = videoView
            addView(videoView)
          }
        },
        onRelease = { frameLayout ->
          try {
            val vv = (0 until frameLayout.childCount).mapNotNull { frameLayout.getChildAt(it) as? VideoView }.firstOrNull()
            vv?.setOnPreparedListener(null)
            vv?.setOnErrorListener(null)
            vv?.setOnCompletionListener(null)
            if (vv?.isPlaying == true) {
              vv.stopPlayback()
            }
          } catch (_: Exception) {}
        },
        modifier = Modifier.fillMaxSize()
      )
    } else {
      // High Performance Static Poster Banner (No network stalling on UI thread)
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                NoveliteDarkBrown,
                Color(0xFF2C1810)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier.padding(16.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = NoveliteCaramel,
            shadowElevation = 6.dp,
            modifier = Modifier.size(54.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Teaser",
                tint = NoveliteDarkBrown,
                modifier = Modifier.size(32.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = title ?: "Watch Story Trailer",
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Tap to load and play teaser",
            fontSize = 11.sp,
            color = NoveliteCardBeige
          )
        }
      }
    }

    // Buffering indicator
    if (isBuffering && !hasError && isStarted) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.5f)),
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
    if (hasError && isStarted) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(NoveliteDarkBrown.copy(alpha = 0.95f))
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.VideocamOff,
            contentDescription = null,
            tint = NoveliteWarmBrown,
            modifier = Modifier.size(36.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Teaser Preview Unavailable",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
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
            Text("Retry", fontSize = 11.sp, color = NoveliteDarkBrown, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Controls Overlay
    AnimatedVisibility(
      visible = isStarted && isControlsVisible && !hasError,
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
        // Top Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title ?: "Story Teaser",
            color = NoveliteWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.weight(1f)
          )

          Row {
            IconButton(
              onClick = {
                isMuted = !isMuted
                mediaPlayerRef?.let { mp ->
                  val vol = if (isMuted) 0f else 1f
                  mp.setVolume(vol, vol)
                }
              },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = NoveliteWhite,
                modifier = Modifier.size(18.dp)
              )
            }

            if (onRemove != null) {
              IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Remove video",
                  tint = NoveliteWhite,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }

        // Center Play / Pause
        IconButton(
          onClick = {
            if (isPlaying) {
              videoViewRef?.pause()
              isPlaying = false
            } else {
              if (isCompleted) {
                videoViewRef?.seekTo(0)
                isCompleted = false
              }
              videoViewRef?.start()
              isPlaying = true
            }
          },
          modifier = Modifier
            .align(Alignment.Center)
            .size(52.dp)
            .background(NoveliteDarkBrown.copy(alpha = 0.75f), CircleShape)
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else (if (isCompleted) Icons.Default.Replay else Icons.Default.PlayArrow),
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = NoveliteWhite,
            modifier = Modifier.size(28.dp)
          )
        }

        // Bottom Progress Bar
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
        ) {
          val progress = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp),
            color = NoveliteCaramel,
            trackColor = NoveliteWhite.copy(alpha = 0.3f)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = formatTime(currentPositionMs),
              color = NoveliteWhite,
              fontSize = 10.sp
            )
            Text(
              text = formatTime(durationMs),
              color = NoveliteWhite,
              fontSize = 10.sp
            )
          }
        }
      }
    }
  }
}

private fun formatTime(ms: Int): String {
  val totalSec = ms / 1000
  val min = totalSec / 60
  val sec = totalSec % 60
  return "%02d:%02d".format(min, sec)
}
