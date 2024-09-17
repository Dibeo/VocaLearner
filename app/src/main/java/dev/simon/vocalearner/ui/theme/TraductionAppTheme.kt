package dev.simon.vocalearner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color.White, // Couleur du texte principal en blanc
    onPrimary = Color.Black, // Couleur du texte sur fond primaire
    background = Color.Black, // Arrière-plan en noir
    surface = Color.DarkGray, // Surface d'arrière-plan pour des éléments secondaires
    onBackground = Color.White, // Couleur du texte sur fond d'arrière-plan
    onSurface = Color.White, // Couleur du texte sur surface
    secondary = Teal200 // Garder la couleur secondaire si elle convient
)

private val LightColorPalette = lightColorScheme(
    primary = Color.Black, // Couleur du texte principal en noir
    onPrimary = Color.White, // Couleur du texte sur fond primaire
    background = Color(0xFFD8DBE2), // Arrière-plan en blanc
    surface = Color.LightGray, // Surface d'arrière-plan pour des éléments secondaires
    onBackground = Color.Black, // Couleur du texte sur fond d'arrière-plan
    onSurface = Color.Black, // Couleur du texte sur surface
    secondary = Teal200 // Garder la couleur secondaire si elle convient
    // Autres couleurs à personnaliser si nécessaire
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
