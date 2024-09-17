package dev.simon.vocalearner.utils

import android.content.Context
import android.util.Log
import dev.simon.vocalearner.R
import java.io.BufferedReader
import java.io.InputStreamReader

fun readCsvFromRaw(context: Context, rawResId: Int): List<Pair<String, String>> {
    val wordList = mutableListOf<Pair<String, String>>()
    try {
        val inputStream = context.resources.openRawResource(rawResId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1) // Ignore le header s'il existe
                .forEach { line ->
                    val columns = line.split(",")
                    if (columns.size >= 2) {
                        val id = columns[0].trim()
                        val word = columns[1].trim()
                        wordList.add(Pair(id, word))
                    }
                }
        }
    } catch (e: Exception) {
        Log.e("CSVUtils", "Erreur lors de la lecture du fichier CSV", e)
    }
    return wordList
}

fun matchWordsById(context: Context): List<Pair<String, String>> {
    val englishWords = readCsvFromRaw(context, R.raw.english_words) // CSV pour les mots en anglais
    val frenchWords = readCsvFromRaw(context, R.raw.french_words)   // CSV pour les mots en français

    val idToEnglishMap = englishWords.groupBy { it.first } // Groupement par identifiant
    val idToFrenchMap = frenchWords.groupBy { it.first }

    val matchedWords = mutableListOf<Pair<String, String>>()

    idToEnglishMap.forEach { (id, englishList) ->
        val frenchList = idToFrenchMap[id] // Correspondance basée sur l'identifiant
        if (frenchList != null) {
            englishList.forEach { english ->
                frenchList.forEach { french ->
                    matchedWords.add(Pair(french.second, english.second)) // Associer mot français et anglais
                }
            }
        }
    }

    return matchedWords
}
