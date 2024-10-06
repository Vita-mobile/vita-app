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

    primary = MainBlue , //Color principal de la app - Es el mas frecuente
    onPrimary = White, //Color de texto o iconos que superponen al color primario - Usado de contraste con el color primario
    secondary = LightTurquoise, //Color secundario utilizado en algunos elementos de la app (Complementarios)
    tertiary = Aquamarine,
    background = White, //Color de fondo de toda la aplicación
    onBackground = Black, //Color del texto de los elementos que se superponen al fondo
    surface = LightGray, //Elementos que pueden superponerse (Ej: Pop-ups)
    onSurface = Black,
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