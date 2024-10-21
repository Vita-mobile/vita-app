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

    primary = MainBlue , //Main color
    onPrimary = White, //Color of text or icons overlaying the primary color
    secondary = LightTurquoise, //Complementary color - Secondary color used in some elements of the app
    tertiary = Aquamarine,
    background = White, //Background color
    onBackground = Black, //Color of the text that will be superimposed on the background of the app
    surface = LightGray, //Elements that can overlap (e.g. pop-ups)
    onSurface = Black,
    surfaceContainer = LightTurquoise2,
    tertiaryContainer = Cyan
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