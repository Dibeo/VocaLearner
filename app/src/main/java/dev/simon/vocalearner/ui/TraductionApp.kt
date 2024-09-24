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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.simon.vocalearner.ui.theme.TraductionAppTheme
import dev.simon.vocalearner.utils.matchWordsById
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random
import java.text.Normalizer

@Composable
fun TraductionApp(context: Context) {
    val selectedWeeks = remember { mutableStateListOf<String>() }
    val (frenchWordsList, englishWordsList) = matchWordsById(context, selectedWeeks)
    var currentIndex by remember { mutableStateOf(frenchWordsList.indices.random()) }
    var isFrenchToEnglish by remember { mutableStateOf(Random.nextBoolean()) }
    var currentFrenchWord by remember { mutableStateOf("") }
    var currentEnglishWords by remember { mutableStateOf(emptyList<String>()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val availableWeeks = listOf("Semaine 1", "Semaine 2", "Semaine 3", "Semaine 4")

    LaunchedEffect(currentIndex, isFrenchToEnglish) {
        if (isFrenchToEnglish) {
            currentFrenchWord = frenchWordsList[currentIndex].first()
            currentEnglishWords = englishWordsList[currentIndex]
        } else {
            currentFrenchWord = englishWordsList[currentIndex].first()
            currentEnglishWords = frenchWordsList[currentIndex]
        }
    }

    var userInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                availableWeeks = availableWeeks,
                selectedWeeks = selectedWeeks,
                onWeeksSelected = { weeks ->
                    selectedWeeks.clear()
                    selectedWeeks.addAll(weeks)
                },
                scope = coroutineScope,
                drawerState = drawerState
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween, // Place les éléments aux extrémités

        ) {
            Text(text = "Semaines sélectionnées : ${selectedWeeks.joinToString(", ")}")
            Button(onClick = { coroutineScope.launch { drawerState.open() } }) {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleCompose()

            Spacer(modifier = Modifier.height(50.dp))

            DisplayWord(word = currentFrenchWord, isFrenchToEnglish = isFrenchToEnglish)

            Spacer(modifier = Modifier.height(20.dp))

            UserInputField(
                userInput = userInput,
                onInputChange = { userInput = it },
                onDone = {
                    feedbackMessage = if (isAnswerCorrect(userInput, currentEnglishWords)) {
                        // Si la réponse est correcte, choisir un nouveau mot et une nouvelle direction
                        currentIndex = frenchWordsList.indices.random()
                        isFrenchToEnglish = Random.nextBoolean()
                        if (isFrenchToEnglish) {
                            currentFrenchWord = frenchWordsList[currentIndex].first()
                            currentEnglishWords = englishWordsList[currentIndex]
                        } else {
                            currentFrenchWord = englishWordsList[currentIndex].first()
                            currentEnglishWords = frenchWordsList[currentIndex]
                        }
                        userInput = ""
                        "Correct!"
                    } else {
                        "Incorrect! Try again."
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComposable(
                text = "Check",
                onClick = {
                    feedbackMessage = if (isAnswerCorrect(userInput, currentEnglishWords)) {
                        // Si la réponse est correcte, choisir un nouveau mot et une nouvelle direction
                        currentIndex = frenchWordsList.indices.random()
                        isFrenchToEnglish = Random.nextBoolean()
                        if (isFrenchToEnglish) {
                            currentFrenchWord = frenchWordsList[currentIndex].first()
                            currentEnglishWords = englishWordsList[currentIndex]
                        } else {
                            currentFrenchWord = englishWordsList[currentIndex].first()
                            currentEnglishWords = frenchWordsList[currentIndex]
                        }
                        userInput = ""
                        "Correct!"
                    } else {
                        "Incorrect! Try again."
                    }
                }

            )

            Spacer(modifier = Modifier.height(10.dp))

            FeedbackMessage(feedbackMessage)

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.padding(16.dp)) {
                ButtonComposable(
                    text = "Change Word",
                    onClick = {
                        currentIndex = frenchWordsList.indices.random()
                        isFrenchToEnglish = Random.nextBoolean()
                        if (isFrenchToEnglish) {
                            currentFrenchWord = frenchWordsList[currentIndex].first()
                            currentEnglishWords = englishWordsList[currentIndex]
                        } else {
                            currentFrenchWord = englishWordsList[currentIndex].first()
                            currentEnglishWords = frenchWordsList[currentIndex]
                        }
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
}

@Composable
fun DrawerContent(
    availableWeeks: List<String>,
    selectedWeeks: List<String>,
    onWeeksSelected: (List<String>) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState

) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(text = "Choisissez les semaines", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(10.dp))

        val selectedWeeksState =
            remember { mutableStateListOf<String>().apply { addAll(selectedWeeks) } }

        availableWeeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedWeeksState.contains(week),
                    onCheckedChange = {
                        if (it) {
                            selectedWeeksState.add(week)
                        } else {
                            selectedWeeksState.remove(week)
                        }
                    }
                )
                Text(text = week, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onWeeksSelected(selectedWeeksState.toList())
                scope.launch { drawerState.close() }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Valider")
        }
    }
}

/**
 * Display the word depending on the direction of translation
 *
 * @param word The word to be displayed
 * @param isFrenchToEnglish True if translating from French to English, False otherwise
 */
@Composable
fun DisplayWord(word: String, isFrenchToEnglish: Boolean) {
    val displayText = if (isFrenchToEnglish) {
        "Translate to English : $word"
    } else {
        "Translate to French : $word"
    }
    Text(text = displayText, style = MaterialTheme.typography.titleMedium)
}

/**
 * Normalize a string by removing accents and converting to lower case
 *
 * @param input The string to normalize
 * @return The normalized string
 */
fun normalizeString(input: String): String {
    return Normalizer.normalize(input, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        .lowercase()
}

/**
 * Check if the user's answer is correct
 *
 * @param userInput The user's input
 * @param correctAnswers The list of correct answers
 * @return True if the answer is correct, False otherwise
 */
fun isAnswerCorrect(userInput: String, correctAnswers: List<String>): Boolean {
    val normalizedUserInput = normalizeString(userInput)
    return correctAnswers.any { normalizeString(it) == normalizedUserInput }
}

/**
 * Display the title
 */
@Composable
fun TitleCompose() {
    Text(text = "VocaLearner", style = MaterialTheme.typography.headlineLarge)
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
                onDone()
            }
        ),
        decorationBox = { innerTextField ->
            if (userInput.isEmpty()) {
                Text(
                    "Enter your translation",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
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
