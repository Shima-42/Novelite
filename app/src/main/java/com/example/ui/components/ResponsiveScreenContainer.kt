package com.example.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ResponsiveScreenContainer dynamically calculates horizontal padding and layout orientation
 * using BoxWithConstraints and Window Size Classes (Compact < 600dp, Medium 600-840dp, Expanded > 840dp).
 */
@Composable
fun ResponsiveScreenContainer(
  modifier: Modifier = Modifier,
  compactPadding: Dp = 16.dp,
  mediumPadding: Dp = 24.dp,
  expandedPadding: Dp = 48.dp,
  content: @Composable BoxWithConstraintsScope.(isWideScreen: Boolean, horizontalPadding: Dp) -> Unit
) {
  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter
  ) {
    val isWideScreen = maxWidth >= 600.dp
    val padding = when {
      maxWidth >= 840.dp -> expandedPadding
      maxWidth >= 600.dp -> mediumPadding
      else -> compactPadding
    }
    content(isWideScreen, padding)
  }
}
