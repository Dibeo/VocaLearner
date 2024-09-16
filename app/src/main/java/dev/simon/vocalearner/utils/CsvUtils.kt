package dev.simon.vocalearner.utils

import android.content.Context
import android.util.Log
import dev.simon.vocalearner.R
import java.io.BufferedReader
import java.io.InputStreamReader

fun readCsvFromRaw(context: Context): List<Pair<String, String>> {
    val wordList = mutableListOf<Pair<String, String>>()
    try {
        val inputStream = context.resources.openRawResource(R.raw.words) // Assurez-vous que le nom correspond à votre fichier CSV
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1) // Skip header if there is one
                .forEach { line ->
                    val columns = line.split(",")
                    if (columns.size >= 2) {
                        val frenchWord = columns[0].trim()
                        val englishWord = columns[1].trim()
                        wordList.add(Pair(frenchWord, englishWord))
                    }
                }
        }
    } catch (e: Exception) {
        Log.e("CSVUtils", "Error reading CSV file", e)
    }
    return wordList
}
