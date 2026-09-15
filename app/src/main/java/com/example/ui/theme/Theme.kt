package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val NoveliteShapes = Shapes(
  extraSmall = RoundedCornerShape(14.dp),
  small = RoundedCornerShape(14.dp),
  medium = RoundedCornerShape(14.dp),
  large = RoundedCornerShape(14.dp),
  extraLarge = RoundedCornerShape(14.dp)
)

private val DarkColorScheme =
  darkColorScheme(
    primary = NoveliteButtonBg,
    onPrimary = NoveliteButtonText,
    primaryContainer = NoveliteSecondaryAccent,
    onPrimaryContainer = NoveliteWarmWhite,
    secondary = NoveliteSecondaryAccent,
    onSecondary = NoveliteWarmWhite,
    secondaryContainer = NovelitePrimaryText,
    onSecondaryContainer = NoveliteWarmWhite,
    tertiary = NoveliteSecondaryAccent,
    onTertiary = NoveliteWarmWhite,
    background = NovelitePrimaryText,
    onBackground = NoveliteWarmWhite,
    surface = NovelitePrimaryText,
    onSurface = NoveliteWarmWhite,
    surfaceVariant = NovelitePrimaryText,
    onSurfaceVariant = NoveliteSoftAccentBg,
    outline = NoveliteSecondaryAccent
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NoveliteButtonBg,
    onPrimary = NoveliteButtonText,
    primaryContainer = NoveliteMainBg,
    onPrimaryContainer = NovelitePrimaryText,
    secondary = NoveliteSecondaryAccent,
    onSecondary = NoveliteWarmWhite,
    secondaryContainer = NoveliteWarmWhite,
    onSecondaryContainer = NovelitePrimaryText,
    tertiary = NovelitePrimaryAccent,
    onTertiary = NoveliteWarmWhite,
    background = NoveliteMainBg,
    onBackground = NovelitePrimaryText,
    surface = NoveliteWarmWhite,
    onSurface = NovelitePrimaryText,
    surfaceVariant = NoveliteSoftAccentBg,
    onSurfaceVariant = NoveliteSecondaryText,
    outline = NoveliteSoftAccentBg
  )

@Composable
fun NoveliteTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = NoveliteShapes,
    content = content
  )
}
