package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Global theme state holding the active light/dark mode.
 * Changes to isDark automatically trigger recomposition across all composables
 * that read dynamic brand colors.
 */
object BuanThemeState {
    var isDark by mutableStateOf(true)
}

// BUAN Logistics Brand Palette (Black + BUAN Blue in Dark mode, Crisp Slate + BUAN Blue in Light mode)

val BuanBackground: Color
    get() = if (BuanThemeState.isDark) Color(0xFF050505) else Color(0xFFF6F8FB)

val BuanSurface: Color
    get() = if (BuanThemeState.isDark) Color(0xFF10141D) else Color(0xFFFFFFFF)

val BuanSurfaceVariant: Color
    get() = if (BuanThemeState.isDark) Color(0xFF171C28) else Color(0xFFF1F5F9)

val BuanSurfaceElevated: Color
    get() = if (BuanThemeState.isDark) Color(0xFF1F2636) else Color(0xFFE8EEF5)

val BuanBorder: Color
    get() = if (BuanThemeState.isDark) Color(0xFF232B3C) else Color(0xFFE2E8F0)

val BuanBorderLight: Color
    get() = if (BuanThemeState.isDark) Color(0xFF2E384D) else Color(0xFFCBD5E1)

// Brand Blues (Signature electric blue across both themes)
val BuanBluePrimary = Color(0xFF0052FF)
val BuanBlueCta = Color(0xFF1D6BFF)
val BuanBlueLight: Color
    get() = if (BuanThemeState.isDark) Color(0xFF60A5FA) else Color(0xFF2563EB)
val BuanBlueGlow = Color(0x330052FF)
val BuanBlueSubtle: Color
    get() = if (BuanThemeState.isDark) Color(0x1A0052FF) else Color(0xFFEFF6FF)

// Neutral Text
val TextPrimary: Color
    get() = if (BuanThemeState.isDark) Color(0xFFFFFFFF) else Color(0xFF0F172A)

val TextSecondary: Color
    get() = if (BuanThemeState.isDark) Color(0xFF94A3B8) else Color(0xFF475569)

val TextMuted: Color
    get() = if (BuanThemeState.isDark) Color(0xFF64748B) else Color(0xFF94A3B8)

// Status Accents
val StatusInTransit = Color(0xFF3B82F6)
val StatusDelivered = Color(0xFF10B981)
val StatusPending = Color(0xFFEAB308)
val StatusCancelled = Color(0xFFEF4444)
val StatusProcessing = Color(0xFF8B5CF6)

// Coin gold
val BuanCoinGold = Color(0xFFFBBF24)
