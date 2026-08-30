package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.myapplication.ui.state.AppState

private val SciFiDarkScheme = darkColorScheme(
    primary = PrimaryDark,
    background = SurfaceLowestDark,
    surface = SurfaceDark,
    error = ErrorDark,
    errorContainer = ErrorContainerDark,
    onPrimary = SurfaceLowestDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    onError = SurfaceLowestDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark
)

private val SciFiLightScheme = lightColorScheme(
    primary = PrimaryLight,
    background = SurfaceLowestLight,
    surface = SurfaceLight,
    error = ErrorLight,
    errorContainer = ErrorContainerLight,
    onPrimary = SurfaceLowestLight,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    onError = SurfaceLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val isDark = AppState.isDarkMode
    val colorScheme = if (isDark) SciFiDarkScheme else SciFiLightScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}