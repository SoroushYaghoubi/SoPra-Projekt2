package util

import entity.*


/**
 * this class is to create the zenDeck
 * usage sees [readAllZenCards]
 *
 * the private functions read the csv files then map the data class to our zenCard
 * at the end it returns a list of the chosen cards
 */
class ZenCardLoader {
    val csvLoader = CSVLoader()

    private fun readAllGrowthCards(playerAmount: Int) : List<GrowthCard> {
        return csvLoader.readCsvFile<CSVGrowthCardEntry>("/zengrowth.csv").mapNotNull {
            val type = when(it.type) {
                "log" -> TileType.WOOD
                "leaf" -> TileType.LEAF
                "blossom" -> TileType.FLOWER
                else -> TileType.FRUIT
            }

            val result = if (playerAmount < it.minPlayerAmount) {
                null
            } else{
                GrowthCard(type, it.id)
            }
            result
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
        return csvLoader.readCsvFile<CSVMasterCardEntry>("/zenmaster.csv").mapNotNull {

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

            val tileTypes = mutableListOf(type1,type2,type3)
            val result = if (playerAmount < it.minPlayerAmount) {
                null
            } else{
                MasterCard(tileTypes, it.id)
            }
            result
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
        return csvLoader.readCsvFile<CSVToolCardEntry>("/zentool.csv").mapNotNull {
            val result = if (playerAmount < it.minPlayerAmount) {
                null
            } else{
                ToolCard(it.id)
            }
            result
        }
    }

    /**
     * this function returns a [List] of [Card] for a given amount of [playerAmount]
     *
     * @param [playerAmount] of how many players are there in the game
     */
    fun readAllZenCards(playerAmount : Int) : List<Card> {
        return readAllGrowthCards(playerAmount) +
                readAllHelperCards() +
                readAllMasterCards(playerAmount)+
                readAllParchmentCards() +
                readAllToolCards(playerAmount)
        /**
        val mutableZenDeck = fullZenDeck.toMutableList()
        mutableZenDeck.removeAll {
            it.id == -1
        }
        return mutableZenDeck
        */
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
