package service

import entity.TileType
import util.CULTIVATE
import util.MEDITATE


/**
 * The [EasyBotService] class is used to implement all the actions of the simple bot
 *
 */
class EasyBotService(val rootService: RootService) {

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

    // 0 is cultivate and 1 is meditate
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

    private fun getTilePlacing(){}
}
