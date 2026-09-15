package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NoveliteButtonBg
import com.example.ui.theme.NoveliteButtonBorder
import com.example.ui.theme.NoveliteButtonText

/**
 * Standard button corner radius adhering strictly to 12-14px border radius.
 */
val NoveliteButtonShape: Shape = RoundedCornerShape(14.dp)

/**
 * Default color configuration for all app buttons:
 * - Background: Dark Brown #4A2C2A
 * - Text & Icons: Very light cream #FFF8F6 for high contrast
 */
@Composable
fun noveliteButtonColors(
  containerColor: Color = NoveliteButtonBg,
  contentColor: Color = NoveliteButtonText,
  disabledContainerColor: Color = NoveliteButtonBg.copy(alpha = 0.5f),
  disabledContentColor: Color = NoveliteButtonText.copy(alpha = 0.6f)
): ButtonColors = ButtonDefaults.buttonColors(
  containerColor = containerColor,
  contentColor = contentColor,
  disabledContainerColor = disabledContainerColor,
  disabledContentColor = disabledContentColor
)

/**
 * Secondary outlined/bordered button that preserves dark brown background with subtle border
 * and ensures crisp visible cream text and icons.
 */
@Composable
fun noveliteSecondaryButtonColors(
  containerColor: Color = NoveliteButtonBg,
  contentColor: Color = NoveliteButtonText
): ButtonColors = ButtonDefaults.buttonColors(
  containerColor = containerColor,
  contentColor = contentColor,
  disabledContainerColor = containerColor.copy(alpha = 0.5f),
  disabledContentColor = contentColor.copy(alpha = 0.6f)
)

/**
 * Standard Dark-Brown Rounded Button Component
 *
 * Adheres strictly to the reference specification:
 * - Dark brown background (#4A2C2A)
 * - Rounded/pill shape with 14px (14dp) border radius
 * - Clean, elegant, minimal, and compact (default minHeight = 40.dp)
 * - Text and icons strictly rendered in very light cream (#FFF8F6) with maximum contrast
 * - Centered horizontally and vertically
 * - Icons sized appropriately (16–20dp)
 */
@Composable
fun NovelitePrimaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  iconDescription: String? = null,
  trailingIcon: ImageVector? = null,
  enabled: Boolean = true,
  shape: Shape = NoveliteButtonShape,
  containerColor: Color = Color(0xFFC88577),
  contentColor: Color = Color.White,
  fontSize: TextUnit = 13.sp,
  contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
  minHeight: Dp = 40.dp,
  border: BorderStroke? = null
) {
  Button(
    onClick = onClick,
    modifier = modifier.heightIn(min = minHeight),
    enabled = enabled,
    shape = shape,
    colors = noveliteButtonColors(containerColor = containerColor, contentColor = contentColor),
    contentPadding = contentPadding,
    border = border
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (icon != null) {
          Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(7.dp))
        }

        Text(
          text = text,
          color = contentColor,
          fontSize = fontSize,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        if (trailingIcon != null) {
          Spacer(modifier = Modifier.width(7.dp))
          Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

enum class NoveliteButtonStyle {
  PRIMARY,   // Background #C88577, Text #FFFFFF
  DARK,      // Background #4A2C2A, Text #FFFFFF
  SECONDARY, // Background #FFFFFF, Text #4A2C2A, Border #C88577
  SOFT,      // Background #F5E1DA, Text #4A2C2A
  ACCENT     // Background #D49A89, Text #4A2C2A
}

@Composable
fun NoveliteButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  style: NoveliteButtonStyle = NoveliteButtonStyle.PRIMARY,
  icon: ImageVector? = null,
  iconDescription: String? = null,
  trailingIcon: ImageVector? = null,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  loadingText: String = "Processing...",
  shape: Shape = NoveliteButtonShape,
  fontSize: TextUnit = 13.sp,
  contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
  minHeight: Dp = 40.dp
) {
  val (containerColor, contentColor, border) = when (style) {
    NoveliteButtonStyle.PRIMARY -> Triple(Color(0xFFC88577), Color(0xFFFFFFFF), null)
    NoveliteButtonStyle.DARK -> Triple(Color(0xFF4A2C2A), Color(0xFFFFFFFF), null)
    NoveliteButtonStyle.SECONDARY -> Triple(Color(0xFFFFFFFF), Color(0xFF4A2C2A), BorderStroke(1.dp, Color(0xFFC88577)))
    NoveliteButtonStyle.SOFT -> Triple(Color(0xFFF5E1DA), Color(0xFF4A2C2A), null)
    NoveliteButtonStyle.ACCENT -> Triple(Color(0xFFD49A89), Color(0xFF4A2C2A), null)
  }

  Button(
    onClick = onClick,
    modifier = modifier.heightIn(min = minHeight),
    enabled = enabled && !isLoading,
    shape = shape,
    colors = ButtonDefaults.buttonColors(
      containerColor = containerColor,
      contentColor = contentColor,
      disabledContainerColor = containerColor.copy(alpha = 0.5f),
      disabledContentColor = contentColor.copy(alpha = 0.6f)
    ),
    contentPadding = contentPadding,
    border = border
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (isLoading) {
          androidx.compose.material3.CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            color = contentColor,
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = loadingText,
            color = contentColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        } else {
          if (icon != null) {
            Icon(
              imageVector = icon,
              contentDescription = iconDescription,
              tint = contentColor,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
          }

          Text(
            text = text,
            color = contentColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(7.dp))
            Icon(
              imageVector = trailingIcon,
              contentDescription = null,
              tint = contentColor,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

