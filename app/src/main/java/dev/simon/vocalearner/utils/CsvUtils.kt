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
fun matchWordsById(context: Context, weeks: List<String>): Pair<List<List<String>>, List<List<String>>> {
    val frenchList = mutableListOf<List<String>>()
    val englishList = mutableListOf<List<String>>()
    if (weeks.isEmpty()) {
        val t1 :List<String> = listOf("vide","néant");
        val t2 :List<String> = listOf("empty")
        frenchList.add(t1)
        englishList.add(t2)
        return Pair(frenchList, englishList)
    }

    for (week in weeks) {
        // Générer dynamiquement les noms des fichiers CSV
        val englishResId = context.resources.getIdentifier("${week}_english_words", "raw", context.packageName)
        val frenchResId = context.resources.getIdentifier("${week}_french_words", "raw", context.packageName)

        // Lire les fichiers CSV si les ressources existent
        if (englishResId != 0 && frenchResId != 0) {
            val englishWords = readCsvFromRaw(context, englishResId) // CSV pour les mots en anglais
            val frenchWords = readCsvFromRaw(context, frenchResId)   // CSV pour les mots en français

            val idToEnglishMap = englishWords.groupBy { it.first } // Groupement par identifiant (ID, mots anglais)
            val idToFrenchMap = frenchWords.groupBy { it.first }   // Groupement par identifiant (ID, mots français)

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
        }
    }



    return Pair(frenchList, englishList) // Retourner les deux listes
}
