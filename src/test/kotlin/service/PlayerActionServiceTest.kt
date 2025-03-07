package service

import kotlin.test.*
import entity.*
import java.io.File

class PlayerActionServiceTest {
    @Test
    fun testSaveGame() {

        val rootService = RootService()
        val gameService = GameService(rootService)
        val playerActionService = PlayerActionService(rootService)
        val game = BonsaiGame()
        val player1 = Player("Tom", PlayerType.HUMAN, true)
        val player2 = Player("Tomy", PlayerType.HUMAN, true)
        val gameState1 = BonsaiGameState(player1, mutableListOf(player1,player2), 2, States.MEDITATE)
        val gameState2 = BonsaiGameState(player2, mutableListOf(player1,player2), 2, States.CULTIVATE)
        val history = History()
        history.currentPosition = 1
        history.gameStates.add(gameState1)
        history.gameStates.add(gameState2)
        game.bonsaiGameState.add(gameState1)
        game.bonsaiGameState.add(gameState2)
        game.history = history
        rootService.currentGame = game
        playerActionService.saveGame()
        rootService.currentGame = null
        gameService.continueGame()
        val loadedGame = rootService.currentGame
        assertNotNull(loadedGame)
        assertEquals(game.bonsaiGameState.first(), loadedGame.bonsaiGameState.first())
        assertEquals(game.bonsaiGameState.last(), loadedGame.bonsaiGameState.last())
        assertEquals(game.history?.currentPosition, loadedGame.history?.currentPosition)
        assertEquals(game.history?.gameStates, loadedGame.history?.gameStates)

    }
}
