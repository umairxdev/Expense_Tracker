package com.expensetracker.app.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = MatteBlack,
    primaryContainer = EmeraldGlow,
    onPrimaryContainer = EmeraldLight,
    secondary = CharcoalGray,
    onSecondary = SoftWhite,
    secondaryContainer = DarkCard,
    onSecondaryContainer = MutedWhite,
    tertiary = EmeraldDark,
    onTertiary = SoftWhite,
    background = MatteBlack,
    onBackground = SoftWhite,
    surface = DarkCardElevated,
    onSurface = SoftWhite,
    surfaceVariant = DarkCard,
    onSurfaceVariant = MutedWhite,
    outline = DimWhite,
    outlineVariant = CharcoalGray,
    error = ExpenseRed,
    onError = SoftWhite,
    inverseSurface = SoftWhite,
    inverseOnSurface = MatteBlack,
    surfaceTint = EmeraldGreen
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = EmeraldGlowLight,
    onPrimaryContainer = EmeraldDark,
    secondary = LightBorder,
    onSecondary = LightTextPrimary,
    secondaryContainer = LightCard,
    onSecondaryContainer = LightTextSecondary,
    tertiary = EmeraldDark,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightTextDim,
    outlineVariant = LightBorder,
    error = ExpenseRed,
    onError = Color.White,
    inverseSurface = MatteBlack,
    inverseOnSurface = SoftWhite,
    surfaceTint = EmeraldGreen
)

enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Composable
fun ExpenseTrackerTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
