package service

import entity.*
import kotlin.test.*

// EITHER TEST IS WRONG OR METHOD IS IMPLEMENTED WRONG

/**
 * Tests if method claimOrRenounceGoal works correctly
 */
class ClaimOrRenounceGoalTest {

    /**
     * Initialises a new game for to do some tests with
     *
     * @return A [RootService] with initialised game
     */
    private fun setUpGame(): RootService {
        val rootService = RootService()
        val game = BonsaiGame()
        val player1 = Player("Tom", PlayerType.HUMAN, true, ColorType.BLUE)
        val player2 = Player("Tomy", PlayerType.HUMAN, true, ColorType.RED)
        val goalTileBrown = GoalTile(GoalTileType.BROWN, 1, 5)
        val goalTileGreen = GoalTile(GoalTileType.GREEN, 1, 6)
        val goalTilePink = GoalTile(GoalTileType.PINK, 1, 8)
        val goalTileOrange = GoalTile(GoalTileType.ORANGE, 1, 9)
        val goalTileBlue = GoalTile(GoalTileType.BLUE, 1, 7)
        val gameState1 = BonsaiGameState(player1, mutableListOf(player1, player2), 2, States.MEDITATE)
        gameState1.goalTiles.add(mutableListOf(goalTileBrown))
        gameState1.goalTiles.add(mutableListOf(goalTileOrange))
        gameState1.goalTiles.add(mutableListOf(goalTileGreen))
        gameState1.goalTiles.add(mutableListOf(goalTilePink))
        gameState1.goalTiles.add(mutableListOf(goalTileBlue))
        val history = History()
        history.currentPosition = 1
        history.gameStates.add(gameState1)
        game.currentBonsaiGameState = gameState1
        game.history = history
        rootService.currentGame = game

        return rootService
    }

    // help function to get current player
    private fun getCurrentPlayer(rootService: RootService): Player {
        val currentGameState = checkNotNull(rootService.currentGame?.currentBonsaiGameState)
        return currentGameState.currentPlayer
    }


    /**
     * Tests if player can claim brown goal tile
     */
    @Test
    fun testGoalTileBrown() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        for (i in 0..7) {
            player.bonsaiTree[Pair(i, i)] = Tile(i, i, TileType.WOOD)
        }
        playerActionService.claimOrRenounceGoal(true)
        assertTrue(player.claimedGoals.isNotEmpty())
        assertEquals(GoalTileType.BROWN, player.claimedGoals.first().goalTileType)
    }

    /**
     * Tests if player can claim brown orange tile
     */
    @Test
    fun testGoalTileOrange() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        for (i in 0..2) {
            player.bonsaiTree[Pair(i, i)] = Tile(i, i, TileType.FRUIT)
        }
        playerActionService.claimOrRenounceGoal(true)
        assertTrue(player.claimedGoals.isNotEmpty())
        assertEquals(GoalTileType.ORANGE, player.claimedGoals.first().goalTileType)
    }

    /**
     * Tests if player can claim green goal tile
     */
    @Test
    fun testGoalTileGreen() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        for (i in 0..4) {
            player.bonsaiTree[Pair(i, 3)] = Tile(i, 3, TileType.LEAF)
        }
        playerActionService.claimOrRenounceGoal(true)
        assertTrue(player.claimedGoals.isNotEmpty())
        assertEquals(GoalTileType.GREEN, player.claimedGoals.first().goalTileType)
    }

    /**
     * Tests if player can claim pink goal tile
     */
    @Test
    fun testGoalTilePink() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        for (i in 4..6) {
            player.bonsaiTree[Pair(i, 0)] = Tile(i, 0, TileType.FLOWER)
        }
        playerActionService.claimOrRenounceGoal(true)
        assertTrue(player.claimedGoals.isNotEmpty())
        assertEquals(GoalTileType.PINK, player.claimedGoals.first().goalTileType)
    }

    /**
     * Tests if player can claim blue goal tile
     */
    @Test
    fun testGoalTileBlue() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        player.bonsaiTree[Pair(-3, 0)] = Tile(-3, 0, TileType.FLOWER)
        playerActionService.claimOrRenounceGoal(true)
        assertTrue(player.claimedGoals.isNotEmpty())
        assertEquals(GoalTileType.BLUE, player.claimedGoals.first().goalTileType)
    }

    /**
     * Tests if player renounce goals get added in list
     */
    @Test
    fun testGoalTileRenounce() {
        val rootService = setUpGame()
        val playerActionService = PlayerActionService(rootService)
        val player = getCurrentPlayer(rootService)
        player.bonsaiTree[Pair(-3, 0)] = Tile(-3, 0, TileType.FLOWER)
        playerActionService.claimOrRenounceGoal(false)
        assertTrue(player.renouncedGoals.isNotEmpty())
        assertTrue(player.claimedGoals.isEmpty())
    }


}
