package dev.simon.vocalearner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import dev.simon.vocalearner.ui.TranslationScreen
import dev.simon.vocalearner.ui.theme.TraductionAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraductionAppTheme {
                // Conteneur principal utilisant la couleur de fond du thème
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Appel à TranslationScreen en passant le contexte
                    TranslationScreen(context = this)
                }
            }
        }
    }
}

