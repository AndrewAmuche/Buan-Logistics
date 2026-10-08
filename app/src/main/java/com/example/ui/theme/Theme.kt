package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BuanDarkColorScheme = darkColorScheme(
    primary = BuanBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0x1A0052FF),
    onPrimaryContainer = Color(0xFF60A5FA),
    secondary = Color(0xFF60A5FA),
    onSecondary = Color(0xFF050505),
    background = Color(0xFF050505),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF10141D),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF171C28),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF232B3C),
    outlineVariant = Color(0xFF2E384D)
)

private val BuanLightColorScheme = lightColorScheme(
    primary = BuanBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEFF6FF),
    onPrimaryContainer = Color(0xFF2563EB),
    secondary = Color(0xFF2563EB),
    onSecondary = Color.White,
    background = Color(0xFFF6F8FB),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun BuanTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    BuanThemeState.isDark = darkTheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val statusBarColor = if (darkTheme) Color(0xFF050505) else Color(0xFFF6F8FB)
                val navBarColor = if (darkTheme) Color(0xFF10141D) else Color(0xFFFFFFFF)
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = navBarColor.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = if (darkTheme) BuanDarkColorScheme else BuanLightColorScheme,
        typography = Typography,
        content = content
    )
}
