package service

import kotlin.test.*
import entity.*

/**
 * Tests if startNewGame works correctly:
 * - Game is not null
 * - Player size should be the same as players set
 * - Player order should be as selected
 * - Player names should be the same as selected
 * - FaceUp cards should be 4
 */
class StartNewGameTest {

    /**
     * Initialises a new game for to do some tests with
     *
     * @return A [RootService] with initialised game
     */
    private fun setUpGame(): RootService {
        val rootService = RootService()
        val gameService = GameService(rootService)
        val player1 = Player("Tom", PlayerType.HUMAN, true)
        val player2 = Player("Tomy", PlayerType.HUMAN, true)
        val player3 = Player("Tomi", PlayerType.HUMAN, true)
        val playerOrder = mutableListOf(player1, player2, player3)
        val goalTiles = mutableListOf(GoalTileType.BLUE, GoalTileType.PINK, GoalTileType.GREEN)
        gameService.startNewGame(playerOrder, false, goalTiles)
        return rootService
    }

    /**
     * Tests if game was initialised
     */
    @Test
    fun testIfGameIsNotNull() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        assertNotNull(game)
        // startNewGame fails if there are only 2 players
    }

    /**
     * Tests if player size is correct
     */
    @Test
    fun testPlayerSize() {
        val rootService = setUpGame()
        val game = checkNotNull(rootService.currentGame)
        val players = game.bonsaiGameState.last().players
        assertEquals(3, players.size)
    }

    /**
     * Tests if player order is correct as set
     */
    @Test
    fun testPlayerOrder() {
        val rootService = setUpGame()
        val game = checkNotNull(rootService.currentGame)
        val bonsaiGameState = game.bonsaiGameState.last()
        assertEquals("Tom", bonsaiGameState.currentPlayer.name)
        assertNotEquals("Tomy", bonsaiGameState.currentPlayer.name)
    }

    /**
     * Tests if names got initialised correctly
     */
    @Test
    fun testIfNamesCorrect() {
        val rootService = setUpGame()
        val game = checkNotNull(rootService.currentGame)
        val players = game.bonsaiGameState.last().players
        assertEquals("Tom", players.first().name)
        assertEquals("Tomi", players.last().name)
    }

    /**
     * Tests if faceUp cards are size 4
     */
    @Test
    fun testFaceUpCardsSize() {
        val rootService = setUpGame()
        val game = checkNotNull(rootService.currentGame)
        val bonsaiGameState = game.bonsaiGameState.last()
        assertEquals(4, bonsaiGameState.faceUpCards.size)
        // test fails there are 0 cards in faceUpCards
    }

}
