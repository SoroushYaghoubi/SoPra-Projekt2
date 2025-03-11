package service

import entity.*
import kotlin.test.*

/**
 * Tests if canRedo works correctly:
 * - should be true, if there has been undone before
 */
class CanRedoTest {

    /**
     * Initialises a new game for to do some tests with
     *
     * @return A [RootService] with initialised game
     */
    private fun setUpGame(currentPos: Int): RootService {
        val rootService = RootService()
        val game = BonsaiGame()
        val player1 = Player("Tom", PlayerType.HUMAN, true)
        val player2 = Player("Tomy", PlayerType.HUMAN, true)
        val gameState1 = BonsaiGameState(player1, mutableListOf(player1, player2), 2, States.MEDITATE)
        val gameState2 = BonsaiGameState(player2, mutableListOf(player1, player2), 2, States.CULTIVATE)
        val history = History()
        history.currentPosition = currentPos
        history.gameStates.add(gameState1)
        history.gameStates.add(gameState2)
        game.currentBonsaiGameState = gameState2
        game.history = history
        rootService.currentGame = game
        return rootService

    }

    /**
     * Tests if player can redo if currentPos smaller than history size - 1
     */
    @Test
    fun testCanRedoIfHistoryPosSmallerSize() {
        val rootService = setUpGame(0)
        val historyService = HistoryService(rootService)
        assertTrue(historyService.canRedo())
    }

    /**
     * Tests if player can not redo if currentPos bigger than history size - 1
     */
    @Test
    fun testCanNotRedoIfHistoryPosGreaterSize() {
        val rootService = setUpGame(2)
        val historyService = HistoryService(rootService)
        assertFalse(historyService.canRedo())
    }


    /**
     * Tests if player can not redo if there are no game states yet
     */
    @Test
    fun testCanNotRedoIfGameStateEmpty() {
        val rootService = RootService()
        val game = BonsaiGame()
        val historyService = HistoryService(rootService)
        val history = History()
        history.currentPosition = 0
        game.history = history
        rootService.currentGame = game
        assertFalse(historyService.canRedo())
    }
}
