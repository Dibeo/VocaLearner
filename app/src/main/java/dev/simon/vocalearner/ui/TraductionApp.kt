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
    val (frenchWordsList, englishWordsList) = matchWordsById(context)
    var currentIndex by remember { mutableStateOf(frenchWordsList.indices.random()) }
    var currentFrenchWord by remember { mutableStateOf(frenchWordsList[currentIndex].first()) } // Le premier mot de la liste française
    var currentEnglishWords by remember { mutableStateOf(englishWordsList[currentIndex]) }
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

        FrenchWordDisplay(word = currentFrenchWord)

        Spacer(modifier = Modifier.height(20.dp))

        UserInputField(
            userInput = userInput,
            onInputChange = { userInput = it },
            onDone = {
                feedbackMessage = if (currentEnglishWords.any { it.equals(userInput, ignoreCase = true) }) {
                    currentIndex = frenchWordsList.indices.random()
                    currentFrenchWord = frenchWordsList[currentIndex].first()
                    currentEnglishWords = englishWordsList[currentIndex]
                    userInput = ""
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
                feedbackMessage = if (currentEnglishWords.any { it.equals(userInput, ignoreCase = true) }) {
                    // Si la réponse est correcte, choisir un nouveau mot
                    currentIndex = frenchWordsList.indices.random()
                    currentFrenchWord = frenchWordsList[currentIndex].first() // Le premier mot dans la liste des synonymes
                    currentEnglishWords = englishWordsList[currentIndex]
                    userInput = ""
                    "Correct!"
                } else {
                    "Incorrect! Try again."
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        FeedbackMessage(feedbackMessage)

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.padding(16.dp)) {
            ButtonComposable(
                text = "Change Word",
                onClick = {
                    currentIndex = frenchWordsList.indices.random()
                    currentFrenchWord = frenchWordsList[currentIndex].first()
                    currentEnglishWords = englishWordsList[currentIndex]
                    feedbackMessage = ""
                }
            )

            Spacer(modifier = Modifier.width(25.dp))

            ButtonComposable(
                text = "Show Answer",
                onClick = {
                    feedbackMessage = "Answer is: ${currentEnglishWords.joinToString(", ")}"
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * Display the title
 */
@Composable
fun TitleCompose() {
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
fun UserInputField(userInput: String, onInputChange: (String) -> Unit, onDone: () -> Unit) {
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
                keyboardController?.hide()
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
fun ButtonComposable(text: String, onClick: () -> Unit) {
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
 * Send message method with personalized style
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
        TraductionApp(context = LocalContext.current)
    }
}
