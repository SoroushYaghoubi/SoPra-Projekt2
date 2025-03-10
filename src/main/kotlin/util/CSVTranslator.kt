package util

import entity.Card
import entity.ToolCard
import java.io.File

/**
 * Edit this const to toggle having/not having headers
 */
const val HEADERS: Boolean = true

/**
 * Edit this if you move the CSV Dir
 */
const val CSV_PATH: String = "./src/main/kotlin/util/zen"

fun translateToolCardsFromCSV(playerCount: Int): MutableList<Card>{
    val csvLines = parseCSVLines("$CSV_PATH/zentool.csv")
    val toolCards = mutableListOf<Card>()

    for ((index, line) in csvLines.withIndex()) {
        // discard unwanted cards
        val playerCountRule = line.toInt()
        if (playerCount < playerCountRule) continue

        toolCards.add(ToolCard(id = index))
    }

    return toolCards
}

private fun parseCSVLines(path: String) =
    if (HEADERS) File(path).readLines().drop(1)
    else         File(path).readLines()