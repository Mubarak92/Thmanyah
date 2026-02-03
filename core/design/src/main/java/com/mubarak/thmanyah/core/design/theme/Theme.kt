package com.mubarak.thmanyah.core.design.theme

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
    primary = ThmanyahColors.Primary,
    onPrimary = ThmanyahColors.OnPrimary,
    secondary = ThmanyahColors.Secondary,
    background = ThmanyahColors.BackgroundDark,
    onBackground = ThmanyahColors.OnBackgroundDark,
    surface = ThmanyahColors.SurfaceDark,
    onSurface = ThmanyahColors.OnSurfaceDark,
    surfaceVariant = ThmanyahColors.SurfaceVariantDark,
    onSurfaceVariant = ThmanyahColors.Gray500,
    error = ThmanyahColors.Error
)

private val LightColorScheme = lightColorScheme(
    primary = ThmanyahColors.Primary,
    onPrimary = ThmanyahColors.OnPrimary,
    secondary = ThmanyahColors.Secondary,
    background = ThmanyahColors.BackgroundLight,
    onBackground = ThmanyahColors.OnBackgroundLight,
    surface = ThmanyahColors.SurfaceLight,
    onSurface = ThmanyahColors.OnSurfaceLight,
    onSurfaceVariant = ThmanyahColors.Gray600,
    error = ThmanyahColors.Error
)

@Composable
fun ThmanyahTheme(
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

    MaterialTheme(colorScheme = colorScheme, typography = ThmanyahTypography, content = content)
}
