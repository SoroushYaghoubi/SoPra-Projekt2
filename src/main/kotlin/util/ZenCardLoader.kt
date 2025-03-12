package util

import entity.*

class ZenCardLoader {
    val csvLoader = CSVLoader()

    private fun readAllGrowthCards(playerAmount: Int) : List<GrowthCard> {
        return csvLoader.readCsvFile<CSVGrowthCardEntry>("/zengrowth.csv").map {
            val type = when(it.type) {
                "log" -> TileType.WOOD
                "leaf" -> TileType.LEAF
                "blossom" -> TileType.FLOWER
                else -> TileType.FRUIT
            }

            var id = it.id
            if (playerAmount < it.minPlayerAmount) {
                id = -1
            }
            GrowthCard(type, id)
        }
    }

    private fun readAllHelperCards() : List<HelperCard> {
        return csvLoader.readCsvFile<CSVHelperCardEntry>("/zenhelper.csv").map {
            val type = when(it.type2) {
                "log" -> TileType.WOOD
                "leaf" -> TileType.LEAF
                "blossom" -> TileType.FLOWER
                else -> TileType.FRUIT
            }
            HelperCard(type, it.id)
        }
    }

    private fun readAllMasterCards(playerAmount: Int) : List<MasterCard> {
        return csvLoader.readCsvFile<CSVMasterCardEntry>("/zenmaster.csv").map {

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

            var id = it.id
            val tileTypes = mutableListOf(type1,type2,type3)
            while (tileTypes.contains(TileType.EMPTY)){
                tileTypes.remove(TileType.EMPTY)
            }

            if (playerAmount < it.minPlayerAmount) {
                id = -1
            }

            MasterCard(tileTypes, id)
        }
    }


    private fun readAllParchmentCards() : List<ParchmentCard> {
        return csvLoader.readCsvFile<CSVParchmentCardEntry>("/zenparchment.csv").map {
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

    private fun readAllToolCards(playerAmount: Int) : List<ToolCard> {
        return csvLoader.readCsvFile<CSVToolCardEntry>("/zentool.csv").map {
            var id = it.id
            if (playerAmount < it.minPlayerAmount) {
                id = -1
            }
            ToolCard(id)
        }
    }

    fun readAllZenCards(playerAmount : Int) : List<Card> {
        val fullZenDeck = readAllGrowthCards(playerAmount) +
                readAllHelperCards() +
                readAllMasterCards(playerAmount)+
                readAllParchmentCards() +
                readAllToolCards(playerAmount)
        val mutableZenDeck = fullZenDeck.toMutableList()
        mutableZenDeck.removeAll {
            it.id == -1
        }
        return mutableZenDeck
    }
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
