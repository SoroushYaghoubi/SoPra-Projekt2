package service

import entity.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CreateGoalTilesTest {

    /**
     * Initialises a new game for to do some tests with
     *
     * @return A [RootService] with initialised game
     */
    private fun setUpGame(): RootService {
        val rootService = RootService()
        val gameService = GameService(rootService)
        val player1 = Player("Tom", PlayerType.HUMAN, true, ColorType.RED)
        val player2 = Player("Tomy", PlayerType.HUMAN, true, ColorType.BLACK)
        val playerOrder = mutableListOf(player1, player2)
        val goalTiles = mutableListOf(GoalTileType.BROWN, GoalTileType.ORANGE, GoalTileType.GREEN)
        gameService.startNewGame(playerOrder, false, goalTiles)
        return rootService
    }

    @Test
    fun testGoalTilesForTwoPlayers() {
        val rootService = setUpGame()
        val goalTiles = rootService.gameService.createGoalTiles(mutableListOf(GoalTileType.BROWN, GoalTileType.ORANGE, GoalTileType.GREEN), 2)

        assertFalse(goalTiles.flatten().contains(GoalTile(GoalTileType.ORANGE, 4, 11)))
    }
}
