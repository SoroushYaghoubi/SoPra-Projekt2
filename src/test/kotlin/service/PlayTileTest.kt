package service

import entity.*
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Test class for [TreeService.playTile], [TreeService.canPlayTile]
 */
class PlayTileTest {
    private fun setUpGame(): RootService {
        val rootService = RootService()

        //SETUP for game
        val players = mutableListOf(
            Player("Alice", PlayerType.HUMAN, true, ColorType.RED),
            Player("Bob", PlayerType.HUMAN, true, ColorType.BLUE),
            Player("Tomi", PlayerType.HUMAN, true, ColorType.BLACK)
        )

        ///val zenDeck = mutableListOf()

        val faceUpCards = mutableListOf(
            MasterCard(mutableListOf(TileType.LEAF, TileType.LEAF), 26),
            MasterCard(mutableListOf(TileType.ANY), 26)
        )

        val gameState = BonsaiGameState(
            currentPlayer = players.first(),
            players = players,
            botSpeed = 1,
            currentState = States.CULTIVATE
        )

        gameState.zenDeck.isEmpty()
        gameState.faceUpCards.addAll(faceUpCards)

        //SETUP for player


        val playerBonsaiTree = mutableMapOf(
            (0 to 0) to Tile(null, null, TileType.WOOD), //ROOT
            (0 to -1) to Tile(null, null, TileType.WOOD),
            //(-1 to -1) to Tile(null, null, TileType.LEAF),

            //(-1 to -2) to Tile(null, null, TileType.FLOWER),
            (0 to -2) to Tile(null, null, TileType.WOOD),
            (1 to -2) to Tile(null, null, TileType.WOOD),
            //(2 to -2) to Tile(null, null, TileType.LEAF),
            //(3 to -2) to Tile(null, null, TileType.FLOWER),

            //(0 to -3) to Tile(null, null, TileType.FLOWER),
            (1 to -3) to Tile(null, null, TileType.LEAF),
            (2 to -3) to Tile(null, null, TileType.LEAF),

//            (2 to -4) to Tile(null, null, TileType.FRUIT)
        )

        val playerCollectedCard = mutableListOf(
            ToolCard(41),
            MasterCard(mutableListOf(TileType.LEAF, TileType.FRUIT), 27),
            HelperCard(TileType.LEAF, 35),
            GrowthCard(TileType.LEAF, 3),
            MasterCard(mutableListOf(TileType.ANY), 24),
//            ParchmentCard(TileType.FRUIT, null, 2, 38),
//            ParchmentCard(null, CardType.MASTERCARD, 2, 38)
        )

        val playerPersonalSupply = mutableListOf(
            Tile(null, null, TileType.FLOWER),
            Tile(null, null, TileType.WOOD),
            Tile(null, null, TileType.LEAF),
//            Tile(null, null, TileType.FLOWER),
//            Tile(null, null, TileType.FRUIT),
//            Tile(null, null, TileType.WOOD),
        )

//        val playerClaimedGoal = mutableListOf(
//            GoalTile(GoalTileType.GREEN, 5, 6),
//            GoalTile(GoalTileType.PINK, 4, 12),
//        )

        gameState.currentPlayer.collectedCards = playerCollectedCard
        gameState.currentPlayer.bonsaiTree = playerBonsaiTree
        //   gameState.currentPlayer.claimedGoals = playerClaimedGoal

        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState

        rootService.currentGame = game

        return rootService
    }

    /**
     * test canPlayTile returns true for valid placement of leaf, wood, flower, fruit
     */
    @Test
    fun `test canPlayTile returns true for valid play`() {
        val rootService = setUpGame()
        val game = rootService.currentGame
        checkNotNull(game)
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState)

        val tileWood = Tile(null, null, TileType.WOOD)
        val tileLeaf = Tile(null, null, TileType.LEAF)
        val tileFlower = Tile(null, null, TileType.FLOWER)
        val tileFruit = Tile(null, null, TileType.FRUIT)


        gameState.currentPlayer.personalSupply.add(tileWood)
        gameState.currentPlayer.personalSupply.add(tileLeaf)
        gameState.currentPlayer.personalSupply.add(tileFlower)
        gameState.currentPlayer.personalSupply.add(tileFruit)

        gameState.currentPlayer.playableTilesCopy.add(TileType.LEAF)
        gameState.currentPlayer.playableTilesCopy.add(TileType.WOOD)
        gameState.currentPlayer.playableTilesCopy.add(TileType.FLOWER)
        gameState.currentPlayer.playableTilesCopy.add(TileType.FRUIT)


        assertTrue(rootService.treeService.canPlayTile(tileWood, Pair(1, -1)))
        assertTrue(rootService.treeService.canPlayTile(tileLeaf, Pair(2, -2)))

        assertTrue(rootService.treeService.canPlayTile(tileFlower, Pair(0, -3)))
        assertTrue(rootService.treeService.canPlayTile(tileFruit, Pair(2, -4)))
    }


}



