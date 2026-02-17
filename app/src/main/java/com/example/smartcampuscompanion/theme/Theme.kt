package com.example.smartcampuscompanion.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = Black,
    surface = Black,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    tertiary = AccentDark,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = White,
    surface = White,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    tertiary = Accent
)

@Composable
fun SmartCampusCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}