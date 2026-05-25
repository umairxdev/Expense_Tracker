package com.expensetracker.app.core.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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
    surface = DarkSurface,
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

@Composable
fun ExpenseTrackerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
