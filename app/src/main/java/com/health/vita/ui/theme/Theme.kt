package com.health.vita.ui.theme


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
    /**primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80*/
)

private val LightColorScheme = lightColorScheme(

    primary = MainBlue, // Primary color of the app - It's the most frequent
    onPrimary = White, // Text or icons that overlay the primary color - Used for contrast with the primary color
    secondary = LightTurquoise, // Secondary color used in some elements of the app (Complementary)
    tertiary = Aquamarine,
    onTertiary = MintGreen,
    background = White, // Background color of the entire application
    onBackground = Black, // Color of the text of elements that overlay the background
    surface = LightGray, // Elements that may overlay (e.g., Pop-ups)
    onSurface = Black,
    surfaceContainer = LightTurquoise2,
    surfaceContainerHighest = MintGreen,
    scrim = Gray



)

@Composable
fun VitaTheme(

    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}