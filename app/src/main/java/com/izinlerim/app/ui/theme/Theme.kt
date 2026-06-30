package com.izinlerim.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AppColorScheme = lightColorScheme(
    primary = Color(0xFF0F6B57),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDF3EC),
    onPrimaryContainer = Color(0xFF063D31),
    secondary = Color(0xFF2D5F73),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2F0F5),
    tertiary = Color(0xFF8A5A16),
    tertiaryContainer = Color(0xFFFFE9C7),
    background = Color(0xFFF5F7F6),
    onBackground = Color(0xFF111817),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111817),
    surfaceVariant = Color(0xFFE8EEEC),
    onSurfaceVariant = Color(0xFF53615D),
    outline = Color(0xFFCBD6D2),
    error = Color(0xFFB42318),
    errorContainer = Color(0xFFFFE4E0),
    onErrorContainer = Color(0xFF7A130D)
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)

@Composable
fun IzinlerimTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
