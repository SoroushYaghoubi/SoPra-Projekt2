package service

import entity.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Tests the [TreeService.removeFromTree] function
 */
class RemoveFromTreeTest {

    /**
     * test remove tile from tree
     */
    @Test
    fun `Remove only the chosen Tile test`() {
        val rootService = RootService()

        //SETUP for game
        val players = mutableListOf(
            Player("Alice", PlayerType.HUMAN, true, ColorType.RED),
            Player("Bob", PlayerType.HUMAN, true, ColorType.BLUE),
        )

        val gameState = BonsaiGameState(
            currentPlayer = players.first(),
            players = players,
            botSpeed = 1,
            currentState = States.DISCARDING
        )

        //SETUP for player
        val playerBonsaiTree = mutableMapOf(
            (0 to 0) to Tile(null, null, TileType.WOOD),
            (1 to -2) to Tile(null, null, TileType.LEAF),
            (0 to -2) to Tile(null, null, TileType.LEAF),
            (-1 to -1) to Tile(null, null, TileType.LEAF),
            (0 to -1) to Tile(null, null, TileType.WOOD),
            (1 to -1) to Tile(null, null, TileType.LEAF),
        )

        val playerCollectedCard = mutableListOf(
            ToolCard(41),
            MasterCard(mutableListOf(TileType.LEAF, TileType.FRUIT), 27),
            HelperCard(TileType.LEAF, 35),
            GrowthCard(TileType.LEAF, 3),
            MasterCard(mutableListOf(TileType.ANY), 24)
        )

        gameState.currentPlayer.collectedCards = playerCollectedCard
        gameState.currentPlayer.bonsaiTree = playerBonsaiTree

        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState
        rootService.currentGame = game
        assert(rootService.treeService.isMinimalAndCorrect(Pair(1, -1)))
        println(gameState.currentPlayer.bonsaiTree)
        rootService.treeService.removeFromTree(Pair(1, -1))
        println(gameState.currentPlayer.bonsaiTree)
    }

    /**
     * test remove tiles from tree
     */
    @Test
    fun `Remove the chosen tile and the connected one`() {
        val rootService = RootService()

        //SETUP for game
        val players = mutableListOf(
            Player("Alice", PlayerType.HUMAN, true, ColorType.RED),
            Player("Bob", PlayerType.HUMAN, true, ColorType.BLUE),
        )

        val gameState = BonsaiGameState(
            currentPlayer = players.first(),
            players = players,
            botSpeed = 1,
            currentState = States.DISCARDING
        )

        //SETUP for player
        val playerBonsaiTree = mutableMapOf(
            (0 to 0) to Tile(null, null, TileType.WOOD),
            (1 to -2) to Tile(null, null, TileType.LEAF),
            (0 to -2) to Tile(null, null, TileType.LEAF),
            (-1 to -1) to Tile(null, null, TileType.LEAF),
            (0 to -1) to Tile(null, null, TileType.WOOD),
            (1 to -1) to Tile(null, null, TileType.LEAF),
            (-1 to -2) to Tile(null, null, TileType.FRUIT),
            (1 to -3) to Tile(null, null, TileType.FRUIT),
            (2 to -2) to Tile(null, null, TileType.FRUIT),
        )

        val playerCollectedCard = mutableListOf(
            ToolCard(41),
            MasterCard(mutableListOf(TileType.LEAF, TileType.FRUIT), 27),
            HelperCard(TileType.LEAF, 35),
            GrowthCard(TileType.LEAF, 3),
            MasterCard(mutableListOf(TileType.ANY), 24)
        )

        gameState.currentPlayer.collectedCards = playerCollectedCard
        gameState.currentPlayer.bonsaiTree = playerBonsaiTree

        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState
        rootService.currentGame = game
        rootService.treeService.isMinimalAndCorrect(Pair(1, -1))
        assertEquals(9, gameState.currentPlayer.bonsaiTree.size)
        println(gameState.currentPlayer.bonsaiTree)
        rootService.treeService.removeFromTree(Pair(1, -1))
        println(gameState.currentPlayer.bonsaiTree)
        assertEquals(7, gameState.currentPlayer.bonsaiTree.size)
        gameState.currentPlayer.bonsaiTree[Pair(1, -1)] = Tile(null, null, TileType.LEAF)
        gameState.currentPlayer.bonsaiTree[Pair(2, -2)] = Tile(null, null, TileType.FLOWER)
        rootService.treeService.isMinimalAndCorrect(Pair(1, -1))
        rootService.treeService.removeFromTree(Pair(1, -1))
        println(gameState.currentPlayer.bonsaiTree)
        assertEquals(8, gameState.currentPlayer.bonsaiTree.size)
    }

}
