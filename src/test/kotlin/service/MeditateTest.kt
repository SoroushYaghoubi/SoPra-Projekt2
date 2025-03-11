package service

import entity.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertFails

/**
 * Test class for [PlayerActionService.meditate]
 */
class MeditateTest {

    //The game state before meditate action
    private fun setUpGame(): RootService {
        val rootService = RootService()

        //SETUP for game
        val players = mutableListOf(
            Player("Alice", PlayerType.HUMAN, true),
            Player("Bob", PlayerType.HUMAN, true),
            Player("Tomi", PlayerType.HUMAN, true)
        )

        val zenDeck = mutableListOf(
            MasterCard(mutableListOf(TileType.WOOD, TileType.WOOD), 21),
            ParchmentCard(null, CardType.MASTERCARD, 2, 34),
            GrowthCard(TileType.WOOD, 0),
            GrowthCard(TileType.LEAF, 3)
        )

        val faceUpCards = mutableListOf(
            ToolCard(44), //Position 0
            ParchmentCard(null, CardType.GROWTHCARD, 2, 35), //Position 1
            MasterCard(mutableListOf(TileType.LEAF, TileType.LEAF), 26), //Position 2
            GrowthCard(TileType.FLOWER, 9) //Position 3
        )

        val gameState = BonsaiGameState(
            currentPlayer = players.first(),
            players = players,
            botSpeed = 1,
            currentState = States.MEDITATE
        )

        gameState.zenDeck.addAll(zenDeck)
        gameState.faceUpCards.addAll(faceUpCards)

        //SETUP for player

        gameState.currentPlayer.tileCapacity = 5
        gameState.currentPlayer.playableTiles //As default
        gameState.currentPlayer.personalSupply = mutableListOf(Tile(null, null, TileType.WOOD))

        gameState.currentPlayer.collectedCards = mutableListOf(
            MasterCard(mutableListOf(TileType.LEAF, TileType.FRUIT), 27)
        )


        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState

        rootService.currentGame = game

        return rootService
    }

    // in this case is tool card, player receives no extra tile,
    // but the personal supply capacity is expanded to 7
    // and the drawn card is moved to the personal collected card stack
    @Test
    fun `test draw the card in position 0`() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        checkNotNull(game)
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        assertEquals(5, gameState.currentPlayer.tileCapacity)
        assertEquals(3, gameState.currentPlayer.playableTiles.size)
        assertEquals(1, gameState.currentPlayer.collectedCards.size)

        rootService.playerActionService.meditate(0, null)
        assertEquals(7, gameState.currentPlayer.tileCapacity)
        assertEquals(3, gameState.currentPlayer.playableTiles.size)
        assertEquals(2, gameState.currentPlayer.collectedCards.size)
    }

    // in this case: position 2 -> player receives a wood tile and flower tile in personal supply
    // drawn card is master card, player receives the tile(s) whose type is shown on master.
    // in this situation MasterCard(mutableListOf(TileType.LEAF, TileType.LEAF) -> 2 leaf tiles
    // and the drawn card is moved to the personal collected card stack
    @Test
    fun `test draw the card in position 2`() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        checkNotNull(game)
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        assertEquals(5, gameState.currentPlayer.tileCapacity)
        assertEquals(3, gameState.currentPlayer.playableTiles.size)
        assertEquals(1, gameState.currentPlayer.personalSupply.size)
        assertEquals(1, gameState.currentPlayer.collectedCards.size)

        rootService.playerActionService.meditate(2, null)
        assertEquals(5, gameState.currentPlayer.tileCapacity)
        assertEquals(3, gameState.currentPlayer.playableTiles.size)
        assertEquals(5, gameState.currentPlayer.personalSupply.size)
        assertEquals(2, gameState.currentPlayer.collectedCards.size)
    }
}
