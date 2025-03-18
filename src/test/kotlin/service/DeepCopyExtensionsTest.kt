package service

import entity.*
import kotlin.test.*

class DeepCopyExtensionsTest {

    @Test
    fun testBonsaiGameStateDeepCopy() {

        val originalGameState = BonsaiGameState(
            currentPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED),
            players = mutableListOf(Player("Bob", PlayerType.HUMAN, isLocal = true, ColorType.BLUE)),
            botSpeed = 1,
            currentState = States.START_TURN
        ).apply {
            zenDeck = mutableListOf(GrowthCard(TileType.WOOD, 1))
            faceUpCards = mutableListOf(HelperCard(TileType.WOOD, 2))
            goalTiles = mutableListOf(GoalTile(GoalTileType.GREEN, 1, 10))
        }

        val copiedGameState = originalGameState.deepCopy()

        assertNotSame(originalGameState, copiedGameState)

        assertEquals(originalGameState, copiedGameState)

        originalGameState.currentPlayer.score = 100
        assertNotEquals(originalGameState.currentPlayer.score, copiedGameState.currentPlayer.score)
    }

    @Test
    fun testPlayerDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            bonsaiTree = mutableMapOf((0 to 0) to Tile(0, 0, TileType.WOOD))
            personalSupply = mutableListOf(Tile(1, 1, TileType.LEAF))
            collectedCards = mutableListOf(GrowthCard(TileType.WOOD, 1))
            claimedGoals = mutableListOf(GoalTile(GoalTileType.GREEN, 1, 10))
            score = 50
        }

        val copiedPlayer = originalPlayer.deepCopy()

        assertNotSame(originalPlayer, copiedPlayer)

        assertEquals(originalPlayer, copiedPlayer)

        originalPlayer.score = 100
        assertNotEquals(originalPlayer.score, copiedPlayer.score)
    }

}







