package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF4E1A00),
    primaryContainer = Color(0xFF7E2A00),
    onPrimaryContainer = Color(0xFFFFDCC1),
    secondary = Color(0xFFFFB74D),
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF633F00),
    onSecondaryContainer = Color(0xFFFFDDB3),
    tertiary = Color(0xFF80CBC4),
    onTertiary = Color(0xFF003731),
    background = Color(0xFF19120C),
    surface = Color(0xFF221A14),
    onBackground = Color(0xFFEBE0D8),
    onSurface = Color(0xFFEBE0D8),
    surfaceVariant = Color(0xFF352B24),
    onSurfaceVariant = Color(0xFFD4C4B7)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = OrangePrimary,
    onPrimary = OrangeOnPrimary,
    primaryContainer = OrangePrimaryContainer,
    onPrimaryContainer = OrangeOnPrimaryContainer,
    secondary = OrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = OrangeSecondaryContainer,
    onSecondaryContainer = OrangeOnSecondaryContainer,
    tertiary = TealTertiary,
    onTertiary = Color.White,
    tertiaryContainer = TealTertiaryContainer,
    onTertiaryContainer = TealOnTertiaryContainer,
    background = BackgroundWarm,
    surface = SurfaceWarm,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = TextSecondaryMuted,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted appetizing canteen theme for consistent branding
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
