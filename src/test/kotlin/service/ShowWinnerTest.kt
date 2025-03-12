package service

import kotlin.test.*
import entity.*
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Tests if showWinner works correctly:
 * - if one player has the only highest score
 * - in a two player tie
 * - in a three player tie
 * - in a four player tie
 * - with no running game
 */

class ShowWinnerTest {
    private fun setUpGame(): RootService {
        val rootService = RootService()
        val gameService = GameService(rootService)
        val player1 = Player("Tom", PlayerType.HUMAN, true, ColorType.BLACK)
        val player2 = Player("Tom2", PlayerType.HUMAN, true, ColorType.RED)
        val player3 = Player("Tom3", PlayerType.HUMAN, true, ColorType.BLUE)
        val player4 = Player("Tom4", PlayerType.HUMAN, true, ColorType.PURPLE)
        val playerOrder = mutableListOf(player1, player2, player3, player4)
        val goalTiles = mutableListOf(GoalTileType.BLUE, GoalTileType.PINK, GoalTileType.GREEN)
        gameService.startNewGame(playerOrder, false, goalTiles)
        return rootService
    }


    /**
     * Tests if player with the highest score wins
     */

    @Test
    fun testSingleWinner() {
        val rootService = setUpGame()
        val gameService = rootService.gameService
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is null." }

        // Set scores
        gameState.players[0].score = 10
        gameState.players[1].score = 8
        gameState.players[2].score = 70
        gameState.players[3].score = 5

        // Verify the winner
        assertEquals("Tom3", gameService.showWinner())
    }
     /**
     * Tests if the player furthest from the starting player wins in case of a two player tie
     */

    @Test
    fun testTieWithTwoPlayers() {
        val rootService = setUpGame()
        val gameService = rootService.gameService
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is null." }

        // Set scores (two players tied)
        gameState.players[0].score = 7
        gameState.players[1].score = 10
        gameState.players[2].score = 10
        gameState.players[3].score = 5

        // Verify the winner (player farthest from the starting player)
        assertEquals("Tom3", gameService.showWinner())
    }

    /**
     * Tests if the player furthest from the starting player wins in case of a three player tie
     */

    @Test
    fun testTieWithThreePlayers() {
        val rootService = setUpGame()
        val gameService = rootService.gameService
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is null." }

        // Set scores (three players tied)
        gameState.players[0].score = 70
        gameState.players[1].score = 100
        gameState.players[2].score = 100
        gameState.players[3].score = 100

        // Verify the winner (player farthest from the starting player)
        assertEquals("Tom4", gameService.showWinner())
    }

    /**
     * Tests if the player furthest from the starting player wins in case of a four player tie
     */

    @Test
    fun testTieWithAllPlayers() {
        val rootService = setUpGame()
        val gameService = rootService.gameService
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is null." }

        // Set scores (all players tied)
        gameState.players[0].score = 10
        gameState.players[1].score = 10
        gameState.players[2].score = 10
        gameState.players[3].score = 10

        // Verify the winner (player farthest from the starting player)
        assertEquals("Tom4", gameService.showWinner())
    }

     /**
     * Tests if show winner fails if no game was started
     */

    @Test
    fun testEmptyGameState() {
        val rootService = RootService()
        val gameService = GameService(rootService)

        // No game state is set
        val exception = assertFailsWith<IllegalStateException> {
            gameService.showWinner()
        }
        assertEquals("No game was started.", exception.message)
    }


}
