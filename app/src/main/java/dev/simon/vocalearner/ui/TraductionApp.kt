package dev.simon.vocalearner.ui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.simon.vocalearner.ui.theme.TraductionAppTheme
import dev.simon.vocalearner.utils.matchWordsById // Import your new CSV matching function
import dev.simon.vocalearner.R // Import the R class for accessing raw resources

@Composable
fun TranslationScreen(context: Context) {
    val wordList by remember { mutableStateOf(matchWordsById(context)) } // Utilise la nouvelle fonction
    var currentWord by remember { mutableStateOf(wordList.random()) }
    var userInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var showAnswer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FrenchWordDisplay(word = currentWord.first) // Affiche le mot français

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(userInput = userInput, onInputChange = { userInput = it })

        Spacer(modifier = Modifier.height(20.dp))

        CheckButton(onCheck = {
            feedbackMessage = if (userInput.lowercase() == currentWord.second.lowercase()) {
                currentWord = wordList.random() // Si correct, choisir un nouveau mot
                userInput = "" // Réinitialiser le champ de saisie
                "Correct!"
            } else {
                "Incorrect! Try again."
            }
            showAnswer = ""
        })

        Spacer(modifier = Modifier.height(20.dp))

        FeedbackMessage(feedbackMessage)

        Spacer(modifier= Modifier.height((20.dp)))

        Row(modifier = Modifier
            .padding(16.dp)){
            ChangeWordButton(onChangeWord = {
                currentWord = wordList.random()
                showAnswer = ""
            })

            Spacer(modifier = Modifier.width((25.dp)))

            ShowAnswerButton(onShowAnswer = {showAnswer = currentWord.second})
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (showAnswer.isNotEmpty()) {
            Text(text = "Correct answer: $showAnswer", style = MaterialTheme.typography.bodyLarge) // Affiche la réponse
        }
    }
}

@Composable
fun FrenchWordDisplay(word: String) {
    Text(text = "Translate the word: $word", style = MaterialTheme.typography.titleMedium)
}

@Composable
fun UserInputField(userInput: String, onInputChange: (String) -> Unit) {
    BasicTextField(
        value = userInput,
        onValueChange = onInputChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        decorationBox = { innerTextField ->
            if (userInput.isEmpty()) {
                Text("Enter the English translation", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
            }
            innerTextField()
        }
    )
}

@Composable
fun CheckButton(onCheck: () -> Unit) {
    Button(
        onClick = onCheck,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface, // Arrière-plan du bouton
            contentColor = MaterialTheme.colorScheme.onSurface  // Couleur du texte
        )
    ) {
        Text("Check")
    }
}

@Composable
fun ChangeWordButton(onChangeWord: () -> Unit) {
    Button(
        onClick = onChangeWord,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface, // Arrière-plan du bouton
            contentColor = MaterialTheme.colorScheme.onSurface  // Couleur du texte
        )
    ) {
        Text("Change Word")
    }
}

@Composable
fun ShowAnswerButton(onShowAnswer: () -> Unit) {
    Button(
        onClick = onShowAnswer,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface, // Arrière-plan du bouton
            contentColor = MaterialTheme.colorScheme.onSurface  // Couleur du texte
        )
    ) {
        Text("Show Answer")
    }
}


/**
 * send message method with personalized style
 */
@Composable
fun FeedbackMessage(message: String) {
    Text(text = message, style = MaterialTheme.typography.bodyLarge)
}

@Preview(showBackground = true)
@Composable
fun PreviewTranslationScreen() {
    TraductionAppTheme {
        // Pass a mocked list for preview
        TranslationScreen(context = LocalContext.current) // Note: `LocalContext.current` for preview is not applicable. Replace with context in preview.
    }
}
