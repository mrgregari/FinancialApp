package com.example.core_ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2AE881),
    secondary = Color(0xFFD4FAE6),
    tertiary = Pink40,
    onSurface = Color(0xFFFEF7FF),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceContainer = Color(0xFF23272A),
    outlineVariant = Color(0xFF49454F),
    surface = Color(0xFF181A20),
    background = Color(0xFF121212),
    onPrimary = Color(0xFF181A20),
    onSecondary = Color(0xFF181A20),
    onTertiary = Color(0xFF181A20),
    onBackground = Color(0xFFD4FAE6),
    surfaceContainerHigh = Color(0xFF23272A),
    onError = Color(0xFFE46962)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2AE881),
    secondary = Color(0xFFD4FAE6),
    tertiary = Pink40,
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceContainer = Color(0xFFF3EDF7),
    outlineVariant = Color(0xFFCAC4D0),
    surface = Color(0xFFFEF7FF),
    surfaceContainerHigh = Color(0xFFECE6F0),
    onError = Color(0xFFE46962),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FinancialAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    mainColor: Color = Color(0xFF2AE881),
    secondaryColor: Color = Color(0xFFD4FAE6),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme.copy(primary = mainColor, secondary = secondaryColor)
        else -> LightColorScheme.copy(primary = mainColor, secondary = secondaryColor)
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}