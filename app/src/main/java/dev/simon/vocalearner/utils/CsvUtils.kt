package dev.simon.vocalearner.utils

import android.content.Context
import android.util.Log
import dev.simon.vocalearner.R
import java.io.BufferedReader
import java.io.InputStreamReader

// Fonction pour lire un fichier CSV et récupérer une liste de paires (ID, List<Mot ou Synonymes>)
fun readCsvFromRaw(context: Context, rawResId: Int): List<Pair<String, List<String>>> {
    val wordList = mutableListOf<Pair<String, List<String>>>()
    try {
        val inputStream = context.resources.openRawResource(rawResId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1) // Ignorer l'en-tête
                .forEach { line ->
                    val columns = line.split(",")
                    if (columns.size >= 2) {
                        val id = columns[0].trim()
                        // Le premier mot est dans la deuxième colonne, les synonymes sont dans les suivantes
                        val wordsAndSynonyms = columns.drop(1).map { it.trim() }.filter { it.isNotEmpty() }
                        wordList.add(Pair(id, wordsAndSynonyms)) // Stocker l'ID et la liste de mots/synonymes
                    }
                }
        }
    } catch (e: Exception) {
        Log.e("CSVUtils", "Erreur lors de la lecture du fichier CSV", e)
    }
    return wordList
}

// Fonction pour faire correspondre les mots par ID et retourner deux listes de mots et synonymes pour chaque langue
fun matchWordsById(context: Context): Pair<List<List<String>>, List<List<String>>> {
    val englishWords = readCsvFromRaw(context, R.raw.english_words) // CSV pour les mots en anglais
    val frenchWords = readCsvFromRaw(context, R.raw.french_words)   // CSV pour les mots en français

    val idToEnglishMap = englishWords.groupBy { it.first } // Groupement par identifiant (ID, mots anglais)
    val idToFrenchMap = frenchWords.groupBy { it.first }   // Groupement par identifiant (ID, mots français)

    val frenchList = mutableListOf<List<String>>()
    val englishList = mutableListOf<List<String>>()

    // Associer les mots basés sur l'ID
    idToEnglishMap.forEach { (id, englishEntry) ->
        val frenchEntry = idToFrenchMap[id]
        if (frenchEntry != null) {
            // On suppose qu'il n'y a qu'un seul élément dans chaque liste par ID
            val englishWordsAndSynonyms = englishEntry.first().second
            val frenchWordsAndSynonyms = frenchEntry.first().second

            englishList.add(englishWordsAndSynonyms) // Ajouter les mots et synonymes anglais
            frenchList.add(frenchWordsAndSynonyms)   // Ajouter les mots et synonymes français
        }
    }

    return Pair(frenchList, englishList) // Retourner les deux listes
}
