package service

import entity.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class checkSupplyTest {

    private fun setUpGame(): RootService {
        val rootService = RootService()

        //SETUP for game
        val players = mutableListOf(
            Player("Alice", PlayerType.HUMAN, true, ColorType.RED),
            Player("Bob", PlayerType.HUMAN, true, ColorType.BLUE),
        )

        val faceUpCards = mutableListOf(
            MasterCard(mutableListOf(TileType.LEAF, TileType.LEAF), 26),
            MasterCard(mutableListOf(TileType.ANY), 26)
        )

        val gameState = BonsaiGameState(
            currentPlayer = players.first(), players = players,
            botSpeed = 1, currentState = States.END_TURN
        )

        gameState.zenDeck.isEmpty()
        gameState.faceUpCards.addAll(faceUpCards)




        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState

        rootService.currentGame = game

        return rootService
    }
    @Test
    fun `checkSupply should set state to DISCARDING when player has more tiles than capacity`() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        checkNotNull(game)
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }
        gameState.currentPlayer.personalSupply = MutableList(6) { Tile(null, null, TileType.WOOD) } // 6 Tiles, Kapazität ist 5
        rootService.playerActionService.checkSupply()
        assertEquals(States.DISCARDING, gameState.currentState)
    }

    @Test
    fun `checkSupply should mark player as played when player has tiles within capacity`() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        checkNotNull(game)
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }
        gameState.currentPlayer.personalSupply = MutableList(4) { Tile(null, null, TileType.WOOD) } // 6 Tiles, Kapazität ist 5
        rootService.playerActionService.checkSupply()
        assert(gameState.currentPlayer.hasPlayed)
    }
}
