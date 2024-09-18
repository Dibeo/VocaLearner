package dev.simon.vocalearner.ui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.simon.vocalearner.ui.theme.TraductionAppTheme
import dev.simon.vocalearner.utils.matchWordsById

@Composable
fun TraductionApp(context: Context) {
    val wordList by remember { mutableStateOf(matchWordsById(context)) } // Utilise la nouvelle fonction
    var currentWord by remember { mutableStateOf(wordList.random()) }
    var userInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleCompose()

        Spacer(modifier = Modifier.height(50.dp))

        FrenchWordDisplay(word = currentWord.first) // Affiche le mot français

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            userInput = userInput,
            onInputChange = { userInput = it },
            onDone = {
                feedbackMessage = if (userInput.lowercase() == currentWord.second.lowercase()) {
                    currentWord = wordList.random() // Si correct, choisir un nouveau mot
                    userInput = "" // Réinitialiser le champ de saisie
                    "Correct!"
                } else {
                    "Incorrect! Try again."
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonComposable(
            text = "Check",
            onClick = {
            feedbackMessage = if (userInput.lowercase() == currentWord.second.lowercase()) {
                currentWord = wordList.random() // Si correct, choisir un nouveau mot
                userInput = "" // Réinitialiser le champ de saisie
                "Correct!"
            } else {
                "Incorrect! Try again."
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        FeedbackMessage(feedbackMessage)

        Spacer(modifier= Modifier.height((20.dp)))

        Row(modifier = Modifier
            .padding(16.dp)){
            ButtonComposable(
                text = "Change Word",
                onClick = {
                    currentWord = wordList.random()
                    feedbackMessage = ""
            })

            Spacer(modifier = Modifier.width((25.dp)))

            ButtonComposable(
                text = "Show Answer",
                onClick = {feedbackMessage = "Answer is : ${currentWord.second}"}
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * Display the title
 */
@Composable
fun TitleCompose(){
    Text(text = "VocaLearner", style = MaterialTheme.typography.headlineLarge)
}

/**
 * Display the french word
 *
 * @param String
 */
@Composable
fun FrenchWordDisplay(word: String) {
    Text(text = "Translate the word: $word", style = MaterialTheme.typography.titleMedium)
}

/**
 * Manage the input field
 *
 * @param String
 * @param function
 * @param function
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInputField(userInput: String, onInputChange: (String) -> Unit, onDone : () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        value = userInput,
        onValueChange = onInputChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        decorationBox = { innerTextField ->
            if (userInput.isEmpty()) {
                Text("Enter the English translation", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
            }
            innerTextField()
        }
    )
}

/**
 * Button object for the check button
 *
 * @param function to check if the word is correct or not
 */
@Composable
fun ButtonComposable(text : String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface, // Arrière-plan du bouton
            contentColor = MaterialTheme.colorScheme.onSurface  // Couleur du texte
        )
    ) {
        Text(text)
    }
}


/**
 * send message method with personalized style
 *
 * @param string
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
        TraductionApp(context = LocalContext.current) // Note: `LocalContext.current` for preview is not applicable. Replace with context in preview.
    }
}
