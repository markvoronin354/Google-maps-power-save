package com.markvoronin.mapspowersaving.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1D9452),
    secondary = Color(0xFF1D9452),
    background = Color(0xFF000000),
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFF0E5C2F),
    onPrimaryContainer = Color.White,
    error = Color(0xFFCF6679),
    errorContainer = Color(0xFFB00020),
    onError = Color.Black,
    onErrorContainer = Color.White
)

@Composable
fun EssentialsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
