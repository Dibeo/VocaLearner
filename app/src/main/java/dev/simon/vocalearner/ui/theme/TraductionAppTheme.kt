package dev.simon.vocalearner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFD9E0A4), // Couleur du texte principal en blanc
    onPrimary = Color(0xFFD9E0A4), // Couleur du texte sur fond primaire
    background = Color(0xFF19485F), // Arrière-plan en noir
    surface = Color(0xFFD9E0A4), // Surface d'arrière-plan pour des éléments secondaires
    onBackground = Color(0xFFD9E0A4), // Couleur du texte sur fond d'arrière-plan
    onSurface = Color(0xFF19485F)     // Couleur du texte sur Surface
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF19485F), // Couleur du texte principal en noir
    onPrimary = Color(0xFF19485F), // Couleur du texte sur fond primaire
    background = Color(0xFFD9E0A4), // Arrière-plan en blanc
    surface = Color(0xFF19485F), // Surface d'arrière-plan pour des éléments secondaires
    onBackground = Color(0xFF19485F), // Couleur du texte sur fond d'arrière-plan
    onSurface = Color(0xFFD9E0A4)       //Couleur du texte sur Surface
)

@Composable
fun TraductionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
