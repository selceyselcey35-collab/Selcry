package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LuxuryGoldBlackColorScheme = darkColorScheme(
  primary = ImperialGold,
  onPrimary = ObsidianBlack,
  primaryContainer = ImperialGoldContainer,
  onPrimaryContainer = ImperialGoldLight,
  secondary = ImperialGoldLight,
  onSecondary = ObsidianBlack,
  tertiary = ImperialGoldDark,
  onTertiary = TextPureWhite,
  background = ObsidianBlack,
  onBackground = TextPureWhite,
  surface = ObsidianSurface,
  onSurface = TextPureWhite,
  surfaceVariant = ObsidianSurfaceVariant,
  onSurfaceVariant = TextMuted,
  outline = ImperialGoldBorder,
  outlineVariant = ObsidianStroke,
  error = CrimsonAlert,
  onError = ObsidianBlack
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = LuxuryGoldBlackColorScheme,
    typography = Typography,
    content = content
  )
}
