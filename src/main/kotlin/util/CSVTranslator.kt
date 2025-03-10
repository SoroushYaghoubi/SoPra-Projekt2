package util

import entity.Card
import entity.ToolCard
import java.io.File

/**
 * Edit this const to toggle having/not having headers
 */
const val HEADERS: Boolean = true

fun translateToolCardsFromCSV(playerCount: Int): MutableList<Card>{
    val csvLines = parseCSVLines("./src/main/kotlin/util/zen/zentool.csv")
    val toolCards = mutableListOf<Card>()

    for ((index, line) in csvLines.withIndex()) {
        // filter unwanted cards
        val playerCountRule = line.toInt()
        if (playerCount < playerCountRule) continue

        toolCards.add(ToolCard(id = index))
    }

    return toolCards
}


private fun parseCSVLines(path: String) =
    if (HEADERS) File(path).readLines().drop(1)
    else         File(path).readLines()