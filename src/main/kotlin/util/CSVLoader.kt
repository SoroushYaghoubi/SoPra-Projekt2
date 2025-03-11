package util

import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import entity.*


/**
 * see the main function from this data and try with it
 */
class CSVLoader {
    val csvMapper = CsvMapper().apply {
        registerModule(kotlinModule())
    }

    inline fun <reified T> readCsvFile(fileName: String): List<T> {
        val lines = object {}.javaClass.getResourceAsStream(fileName)?.bufferedReader()


        lines.use { reader ->
            return csvMapper
                .readerFor(T::class.java)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues<T>(reader)
                .readAll()
                .toList()
        }
    }
}

val csvLoader = CSVLoader()

fun readAllGrowthCards() : List<GrowthCard> {
    return csvLoader.readCsvFile<CSVGrowthCardEntry>("/zengrowth.csv").map { it ->
        val type = when(it.type) {
            "log" -> TileType.WOOD
            "leaf" -> TileType.LEAF
            "blossom" -> TileType.FLOWER
            else -> TileType.FRUIT
        }
        GrowthCard(type, it.id)
    }
}

fun readAllHelperCards() : List<HelperCard> {
    return csvLoader.readCsvFile<CSVHelperCardEntry>("/zenhelper.csv").map { it ->
        val type = when(it.type2) {
            "log" -> TileType.WOOD
            "leaf" -> TileType.LEAF
            "blossom" -> TileType.FLOWER
            else -> TileType.FRUIT
        }
        HelperCard(type, it.id)
    }
}

fun readAllMasterCards() : List<MasterCard> {
    return csvLoader.readCsvFile<CSVMasterCardEntry>("/zenmaster.csv").map { it ->

        val type1 = when(it.type1) {
            "log" -> TileType.WOOD
            "leaf" -> TileType.LEAF
            "blossom" -> TileType.FLOWER
            "fruit" -> TileType.FRUIT
            "all" -> TileType.ANY
            else -> TileType.EMPTY
        }
        val type2 = when(it.type2) {
            "log" -> TileType.WOOD
            "leaf" -> TileType.LEAF
            "blossom" -> TileType.FLOWER
            "fruit" -> TileType.FRUIT
            "all" -> TileType.ANY
            else -> TileType.EMPTY
        }
        val type3 = when(it.type3) {
            "log" -> TileType.WOOD
            "leaf" -> TileType.LEAF
            "blossom" -> TileType.FLOWER
            "fruit" -> TileType.FRUIT
            "all" -> TileType.ANY
            else -> TileType.EMPTY
        }

        val tileTypes = mutableListOf<TileType>(type1,type2,type3)
        while (tileTypes.contains(TileType.EMPTY)){
            tileTypes.remove(TileType.EMPTY)
        }

        MasterCard(tileTypes, it.id)
    }
}

// don't use
fun readAllParchmentCards() : List<ParchmentCard> {
    return csvLoader.readCsvFile<CSVParchmentCardEntry>("/zenparchment.csv").map { it ->
        val cardType = when(it.targetType) {
            "master" -> CardType.MASTERCARD
            "growth" -> CardType.GROWTHCARD
            "helper" -> CardType.HELPERCARD
            else -> null
        }

        val tileType = when(it.targetType) {
            "blossom" -> TileType.FLOWER
            "fruit" -> TileType.FRUIT
            "leaf" -> TileType.LEAF
            "log" -> TileType.WOOD
            else -> null
        }

        ParchmentCard(tileType, cardType, it.points, it.id)
    }
}

fun readAllToolCards() : List<ToolCard> {
    return csvLoader.readCsvFile<CSVToolCardEntry>("/zentool.csv").map { it ->

        ToolCard(it.id)
    }
}

fun readAllZenCards() : List<Card> {
    return readAllGrowthCards() + readAllHelperCards() +
            readAllParchmentCards() + readAllMasterCards() + readAllToolCards()
}

data class CSVGrowthCardEntry(
    val id: Int, val minPlayerAmount: Int, val type: String
)

data class CSVHelperCardEntry(
    val id: Int, val type1: String, val type2: String
)

data class CSVMasterCardEntry(
    val id: Int, val minPlayerAmount: Int,
    val type1: String, val type2: String, val type3: String
)

data class CSVParchmentCardEntry(
    val id: Int, val points: Int, val targetType: String
)

data class CSVToolCardEntry(
    val id: Int, val minPlayerAmount: Int
)

fun main() {
    val alist = readAllParchmentCards()
    alist.forEach{
        println(it.parchmentTileType)
    }
}
