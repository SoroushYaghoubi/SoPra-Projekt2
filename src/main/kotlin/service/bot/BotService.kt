package service.bot

import entity.BonsaiGameState
import entity.Tile
import entity.TileType
import service.RootService
import util.*


/**
 * The [BotService] class is used to implement all the actions of the bots
 *
 */
class BotService(val rootService: RootService) {

    /**
     * Function that makes a random move for the bot
     */
    fun makeMove() {

        // random chosen variablen for function calls
        val action = chooseAction()
        val chosenCard = chooseCard()
        val chosenTile = randomTile()

        //
        when  {
            (action == MEDITATE) && (chosenCard == 0) ->
                rootService.playerActionService.meditate(chooseCard(),null)
            (action == MEDITATE) && (chosenCard == 1) ->
                rootService.playerActionService.meditate(chooseCard(), chosenTile)
            (action == MEDITATE) && (chosenCard == 2) ->
                rootService.playerActionService.meditate(chooseCard(),null)
            (action == MEDITATE) && (chosenCard == 3) ->
                rootService.playerActionService.meditate(chooseCard(),null)
            action == CULTIVATE -> {
                rootService.playerActionService.cultivate()

            }
        }
    }

    // 0 is cultivating and 1 is meditating
    private fun chooseAction() : Int = (0..1).random()

    // gives back the card position
    private fun chooseCard() : Int {
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState)

        val openCards = gameState.faceUpCards.size

        return (0 until openCards).random()
    }

    private fun randomTile(): TileType {
       return when ((0..1).random()){
            0 -> TileType.WOOD
            1 -> TileType.LEAF
           else -> TileType.WOOD
        }
    }

    private fun allPossiblePlayedTiles(
        tree : MutableMap<Pair<Int, Int>, Tile>,
        alreadyPlayedTiles : MutableList<Pair<TileType,Pair<Int,Int>>>,
        playableTiles : MutableList<TileType>,
        hasTiles : MutableList<TileType>,
        returnList : MutableList<MutableList<Pair<TileType,Pair<Int,Int>>>>
        ) : MutableList<MutableList<Pair<TileType,Pair<Int,Int>>>>{

        val playedTiles = alreadyPlayedTiles
        val newReturnList : MutableList<MutableList<Pair<TileType,Pair<Int,Int>>>> = mutableListOf()
        returnList.add(playedTiles)

        if (playableTiles.isEmpty() || hasTiles.isEmpty() ) return returnList

        for (emptyPosition in tree.getEmptyTiles() ){
            val neighbors = arrayOf(0,0,0,0,0)
            for (tilePosition in circleAround(emptyPosition)){
                val currentTile = tree[tilePosition]
                if (currentTile != null){
                    when (currentTile.tileType) {
                        TileType.WOOD ->
                            neighbors[0] += 1
                        TileType.LEAF ->
                            neighbors[1] += 1
                        TileType.FLOWER ->
                            neighbors[2] += 1
                        TileType.FRUIT ->
                            neighbors[3] += 1
                        else ->
                            neighbors[4] += 1
                    }
                }
            }

            when {
                neighbors[0] >= 1 && playableTiles.contains(TileType.WOOD) && hasTiles.contains(TileType.WOOD)->
                {
                    val newTreeWood1 = tree
                    newTreeWood1[emptyPosition] = Tile(emptyPosition.first,emptyPosition.second,TileType.WOOD)
                    val newPlayedTiles1 = playedTiles
                    newPlayedTiles1.add(Pair(TileType.WOOD,emptyPosition))
                    val newPlayableTiles1 = playableTiles
                    newPlayableTiles1.remove(TileType.WOOD)
                    val newHasTile1 = hasTiles
                    newHasTile1.remove(TileType.WOOD)

                    returnList += allPossiblePlayedTiles(
                        newTreeWood1,
                        newPlayedTiles1,
                        newPlayableTiles1,
                        newHasTile1,
                        newReturnList
                    )
                }

                neighbors[0] >= 1 && playableTiles.contains(TileType.LEAF) && hasTiles.contains(TileType.LEAF)->
                {
                    val newTreeWood2 = tree
                    newTreeWood2[emptyPosition] = Tile(emptyPosition.first,emptyPosition.second,TileType.LEAF)
                    val newPlayedTiles2 = playedTiles
                    newPlayedTiles2.add(Pair(TileType.LEAF,emptyPosition))
                    val newPlayableTiles2 = playableTiles
                    newPlayableTiles2.remove(TileType.LEAF)
                    val newHasTile2 = hasTiles
                    newHasTile2.remove(TileType.LEAF)

                    returnList += allPossiblePlayedTiles(
                        newTreeWood2,
                        newPlayedTiles2,
                        newPlayableTiles2,
                        newHasTile2,
                        newReturnList
                    )
                }

                neighbors[1] >= 1 && playableTiles.contains(TileType.FLOWER) && hasTiles.contains(TileType.FLOWER)->
                {
                    val newTreeWood3 = tree
                    newTreeWood3[emptyPosition] = Tile(emptyPosition.first,emptyPosition.second,TileType.LEAF)
                    val newPlayedTiles3 = playedTiles
                    newPlayedTiles3.add(Pair(TileType.LEAF,emptyPosition))
                    val newPlayableTiles3 = playableTiles
                    newPlayableTiles3.remove(TileType.LEAF)
                    val newHasTile3 = hasTiles
                    newHasTile3.remove(TileType.LEAF)

                    returnList += allPossiblePlayedTiles(
                        newTreeWood3,
                        newPlayedTiles3,
                        newPlayableTiles3,
                        newHasTile3,
                        newReturnList
                    )
                }

                neighbors[1] >= 2 && neighbors[4] == 0 &&
                        playableTiles.contains(TileType.FRUIT) && hasTiles.contains(TileType.FRUIT)->
                {
                    val newTreeWood4 = tree
                    newTreeWood4[emptyPosition] = Tile(emptyPosition.first,emptyPosition.second,TileType.LEAF)
                    val newPlayedTiles4 = playedTiles
                    newPlayedTiles4.add(Pair(TileType.LEAF,emptyPosition))
                    val newPlayableTiles4 = playableTiles
                    newPlayableTiles4.remove(TileType.LEAF)
                    val newHasTile4 = hasTiles
                    newHasTile4.remove(TileType.LEAF)

                    returnList += allPossiblePlayedTiles(
                        newTreeWood4,
                        newPlayedTiles4,
                        newPlayableTiles4,
                        newHasTile4,
                        newReturnList
                    )
                }
            }
        }

        return returnList

    }

    private fun getAllPossibleMoves(state : BonsaiGameState) : MutableList<Move>{
        val returnList = mutableListOf<Move>()
        return returnList
    }
}
