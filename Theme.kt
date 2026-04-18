package com.asma.cinetrack.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BurntOrange,
    background = Charcoal,
    surface = WarmGray,
    onPrimary = Cream,
    onBackground = Cream,
    onSurface = Cream,
    surfaceVariant = WarmGray,
    onSurfaceVariant = Cream.copy(alpha = 0.8f)
)

private val LightColorScheme = lightColorScheme(
    primary = BurntOrange,
    background = Cream,
    surface = Cream,
    onPrimary = Charcoal,
    onBackground = Charcoal,
    onSurface = Charcoal,
    surfaceVariant = Cream,
    onSurfaceVariant = Charcoal.copy(alpha = 0.8f)
)

@Composable
fun CineTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
