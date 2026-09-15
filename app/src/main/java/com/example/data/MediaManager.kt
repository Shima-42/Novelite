package com.example.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

data class MediaValidationResult(
  val isValid: Boolean,
  val errorMessage: String? = null,
  val fileName: String? = null,
  val fileSizeFormatted: String? = null,
  val durationSeconds: Int? = null,
  val mimeType: String? = null
)

data class MediaPreset(
  val id: String,
  val title: String,
  val genre: String,
  val url: String,
  val isVideo: Boolean,
  val description: String,
  val durationSec: Int? = null
)

object MediaManager {

  const val MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
  const val MAX_VIDEO_SIZE_BYTES = 50 * 1024 * 1024 // 50MB
  const val MAX_VIDEO_DURATION_SECONDS = 60 // 60 seconds max for short teasers

  val SUPPORTED_IMAGE_TYPES = listOf("image/jpeg", "image/png", "image/webp", "image/gif")
  val SUPPORTED_VIDEO_TYPES = listOf("video/mp4", "video/webm", "video/mkv", "video/3gpp", "video/quicktime", "video/x-matroska")

  // Curated Preset Covers for Authors
  val PRESET_COVERS = listOf(
    MediaPreset(
      id = "preset_cover_1",
      title = "Parchment & Crimson Flame",
      genre = "Fantasy",
      url = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800&q=80",
      isVideo = false,
      description = "Ancient leather-bound tome with golden serif engravings"
    ),
    MediaPreset(
      id = "preset_cover_2",
      title = "Lagos Neon & Rain",
      genre = "African Stories",
      url = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
      isVideo = false,
      description = "Night skyline reflecting shimmering amber and purple city lights"
    ),
    MediaPreset(
      id = "preset_cover_3",
      title = "Starlight Constellation",
      genre = "Sci-Fi",
      url = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&q=80",
      isVideo = false,
      description = "Cosmic nebula glowing with warm stardust and ancient runes"
    ),
    MediaPreset(
      id = "preset_cover_4",
      title = "Vintage Parisian Cafe",
      genre = "Romance",
      url = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&q=80",
      isVideo = false,
      description = "Warm coffee cups and journal beside rainy cobblestones"
    ),
    MediaPreset(
      id = "preset_cover_5",
      title = "River Spirit Folklore",
      genre = "African Stories",
      url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
      isVideo = false,
      description = "Sunlit sacred waters flowing beneath golden horizon"
    ),
    MediaPreset(
      id = "preset_cover_6",
      title = "Obsidian Manor Shadows",
      genre = "Mystery",
      url = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80",
      isVideo = false,
      description = "Candlelight casting dramatic shadows across old gothic library"
    )
  )

  // Curated Preset Short Video Teasers for Authors
  val PRESET_VIDEOS = listOf(
    MediaPreset(
      id = "preset_video_1",
      title = "Quill & Amber Flames Teaser",
      genre = "Fantasy",
      url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      isVideo = true,
      description = "Atmospheric fire sparks and mystical smoke rising (15s)",
      durationSec = 15
    ),
    MediaPreset(
      id = "preset_video_2",
      title = "Lagos Midnight Rain Mood",
      genre = "African Stories",
      url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
      isVideo = true,
      description = "Cinematic night drive through neon city streets (15s)",
      durationSec = 15
    ),
    MediaPreset(
      id = "preset_video_3",
      title = "Nebula Starlight Journey",
      genre = "Sci-Fi",
      url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
      isVideo = true,
      description = "Sweeping camera through cosmic stardust and celestial skies (20s)",
      durationSec = 20
    ),
    MediaPreset(
      id = "preset_video_4",
      title = "Parisian Rain & Coffee Teaser",
      genre = "Romance",
      url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
      isVideo = true,
      description = "Warm cafe ambience and slow rain on window pane (15s)",
      durationSec = 15
    )
  )

  fun validateImage(context: Context, uri: Uri): MediaValidationResult {
    try {
      val contentResolver = context.contentResolver
      val mimeType = contentResolver.getType(uri) ?: getMimeTypeFromExtension(uri.toString())
      val isSupported = mimeType.let { mt ->
        SUPPORTED_IMAGE_TYPES.any { mt.startsWith("image/") || mt.equals(it, ignoreCase = true) }
      }

      if (!isSupported) {
        return MediaValidationResult(
          isValid = false,
          errorMessage = "Unsupported image format ($mimeType). Please use JPG, PNG, WEBP, or GIF."
        )
      }

      var fileSize: Long = -1
      var fileName: String? = null

      contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
          val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
          val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
          if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
          if (nameIndex != -1) fileName = cursor.getString(nameIndex)
        }
      }

      if (fileSize > MAX_IMAGE_SIZE_BYTES) {
        val sizeMb = String.format("%.1f", fileSize / (1024.0 * 1024.0))
        return MediaValidationResult(
          isValid = false,
          errorMessage = "Image is too large (${sizeMb}MB). Maximum allowed size is 10MB.",
          fileName = fileName,
          fileSizeFormatted = "${sizeMb}MB"
        )
      }

      return MediaValidationResult(
        isValid = true,
        fileName = fileName ?: "cover_${UUID.randomUUID().toString().take(6)}.jpg",
        fileSizeFormatted = if (fileSize > 0) "${String.format("%.1f", fileSize / 1024.0)} KB" else "Valid",
        mimeType = mimeType
      )
    } catch (e: Exception) {
      return MediaValidationResult(isValid = true, fileName = "selected_cover.jpg")
    }
  }

  fun validateVideo(context: Context, uri: Uri): MediaValidationResult {
    try {
      val contentResolver = context.contentResolver
      val mimeType = contentResolver.getType(uri) ?: getMimeTypeFromExtension(uri.toString())
      val isSupported = mimeType.let { mt ->
        SUPPORTED_VIDEO_TYPES.any { mt.startsWith("video/") || mt.equals(it, ignoreCase = true) }
      }

      if (!isSupported) {
        return MediaValidationResult(
          isValid = false,
          errorMessage = "Unsupported video format ($mimeType). Please use MP4, WEBM, MKV, or 3GP."
        )
      }

      var fileSize: Long = -1
      var fileName: String? = null

      contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
          val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
          val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
          if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
          if (nameIndex != -1) fileName = cursor.getString(nameIndex)
        }
      }

      if (fileSize > MAX_VIDEO_SIZE_BYTES) {
        val sizeMb = String.format("%.1f", fileSize / (1024.0 * 1024.0))
        return MediaValidationResult(
          isValid = false,
          errorMessage = "Video is too large (${sizeMb}MB). Maximum teaser size is 50MB.",
          fileName = fileName,
          fileSizeFormatted = "${sizeMb}MB"
        )
      }

      // Check duration
      var durationSec = 15
      try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val timeStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeMs = timeStr?.toLongOrNull() ?: 0L
        durationSec = (timeMs / 1000).toInt()
        retriever.release()

        if (durationSec > MAX_VIDEO_DURATION_SECONDS) {
          return MediaValidationResult(
            isValid = false,
            errorMessage = "Video duration is ${durationSec}s. Short teasers must be 60 seconds or less.",
            fileName = fileName,
            durationSeconds = durationSec
          )
        }
      } catch (_: Exception) {
        // Duration retrieval may not be available on all content providers
      }

      return MediaValidationResult(
        isValid = true,
        fileName = fileName ?: "teaser_${UUID.randomUUID().toString().take(6)}.mp4",
        fileSizeFormatted = if (fileSize > 0) "${String.format("%.1f", fileSize / (1024.0 * 1024.0))} MB" else "Valid",
        durationSeconds = durationSec,
        mimeType = mimeType
      )
    } catch (e: Exception) {
      return MediaValidationResult(isValid = true, fileName = "selected_video.mp4", durationSeconds = 15)
    }
  }

  fun saveUriToAppStorage(context: Context, uri: Uri, isVideo: Boolean): String {
    try {
      val mediaDir = File(context.filesDir, "novelite_media")
      if (!mediaDir.exists()) mediaDir.mkdirs()

      val extension = if (isVideo) "mp4" else "jpg"
      val targetFile = File(mediaDir, "media_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}.$extension")

      context.contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(targetFile).use { outputStream ->
          inputStream.copyTo(outputStream)
        }
      }
      return targetFile.absolutePath
    } catch (e: Exception) {
      return uri.toString()
    }
  }

  private fun getMimeTypeFromExtension(path: String): String {
    val lower = path.lowercase()
    return when {
      lower.endsWith(".jpg") || lower.endsWith(".jpeg") -> "image/jpeg"
      lower.endsWith(".png") -> "image/png"
      lower.endsWith(".webp") -> "image/webp"
      lower.endsWith(".gif") -> "image/gif"
      lower.endsWith(".mp4") -> "video/mp4"
      lower.endsWith(".webm") -> "video/webm"
      lower.endsWith(".mkv") -> "video/mkv"
      lower.endsWith(".3gp") -> "video/3gpp"
      else -> if (lower.contains("video")) "video/mp4" else "image/jpeg"
    }
  }
}
