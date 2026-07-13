package com.example.mementomori.ui.theme
import androidx.compose.ui.graphics.Color
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MementoDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7DD3FC),
    secondary = Color(0xFFA78BFA),
    tertiary = Color(0xFF38BDF8),

    background = Color(0xFF070B1A),
    surface = Color(0xFF10172A),
    surfaceVariant = Color(0xFF172033),

    onPrimary = Color(0xFF06111F),
    onSecondary = Color(0xFF120A24),
    onTertiary = Color(0xFF03121F),

    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFE5E7EB),
    onSurfaceVariant = Color(0xFFCBD5E1),

    error = Color(0xFFF87171),
    onError = Color(0xFF1F0505)
)

@Composable
fun MementoMoriTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = MementoDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}