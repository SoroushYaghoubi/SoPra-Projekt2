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

    @Test
    fun testPlayerBonsaiTreeDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            bonsaiTree = mutableMapOf((0 to 0) to Tile(0, 0, TileType.WOOD))
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's bonsaiTree
        originalPlayer.bonsaiTree[0 to 0] = Tile(0, 0, TileType.LEAF)

        // Check that the copied player's bonsaiTree is unaffected
        assertNotEquals(originalPlayer.bonsaiTree[0 to 0], copiedPlayer.bonsaiTree[0 to 0])
    }

    @Test
    fun testPlayerPersonalSupplyDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            personalSupply = mutableListOf(Tile(1, 1, TileType.LEAF))
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's personalSupply
        originalPlayer.personalSupply[0] = Tile(1, 1, TileType.FLOWER)

        // Check that the copied player's personalSupply is unaffected
        assertNotEquals(originalPlayer.personalSupply[0], copiedPlayer.personalSupply[0])
    }

    @Test
    fun testPlayerCollectedCardsDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            collectedCards = mutableListOf(GrowthCard(TileType.WOOD, 1))
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's collectedCards
        originalPlayer.collectedCards[0] = GrowthCard(TileType.LEAF, 1)

        // Check that the copied player's collectedCards is unaffected
        assertNotEquals(originalPlayer.collectedCards[0], copiedPlayer.collectedCards[0])
    }

    @Test
    fun testPlayerClaimedGoalsDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            claimedGoals = mutableListOf(GoalTile(GoalTileType.GREEN, 1, 10))
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's claimedGoals
        originalPlayer.claimedGoals[0] = GoalTile(GoalTileType.PINK, 1, 10)

        // Check that the copied player's claimedGoals is unaffected
        assertNotEquals(originalPlayer.claimedGoals[0], copiedPlayer.claimedGoals[0])
    }

    @Test
    fun testPlayerRenouncedGoalsDeepCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            renouncedGoals = mutableListOf(GoalTile(GoalTileType.GREEN, 1, 10))
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's renouncedGoals
        originalPlayer.renouncedGoals[0] = GoalTile(GoalTileType.PINK, 1, 10)

        // Check that the copied player's renouncedGoals is unaffected
        assertNotEquals(originalPlayer.renouncedGoals[0], copiedPlayer.renouncedGoals[0])
    }

    @Test
    fun testPlayerPlayableTilesShallowCopy() {
        val originalPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED).apply {
            playableTiles = mutableListOf(TileType.WOOD, TileType.LEAF)
            playableTilesCopy = mutableListOf(TileType.FLOWER, TileType.FRUIT)
        }

        val copiedPlayer = originalPlayer.deepCopy()

        // Modify the original player's playableTiles
        originalPlayer.playableTiles[0] = TileType.FLOWER

        // Check that the copied player's playableTiles is unaffected
        assertNotEquals(originalPlayer.playableTiles[0], copiedPlayer.playableTiles[0])

        // Modify the original player's playableTilesCopy
        originalPlayer.playableTilesCopy[0] = TileType.ANY

        // Check that the copied player's playableTilesCopy is unaffected
        assertNotEquals(originalPlayer.playableTilesCopy[0], copiedPlayer.playableTilesCopy[0])
    }

    @Test
    fun testBonsaiGameStateZenDeckDeepCopy() {
        val originalGameState = BonsaiGameState(
            currentPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED),
            players = mutableListOf(Player("Bob", PlayerType.HUMAN, isLocal = true, ColorType.BLUE)),
            botSpeed = 1,
            currentState = States.START_TURN
        ).apply {
            zenDeck = mutableListOf(GrowthCard(TileType.WOOD, 1))
        }

        val copiedGameState = originalGameState.deepCopy()

        // Modify the original zenDeck
        originalGameState.zenDeck.add(GrowthCard(TileType.LEAF, 2))

        // Check that the copied zenDeck is unaffected
        assertNotEquals(originalGameState.zenDeck.size, copiedGameState.zenDeck.size)

    }

    @Test
    fun testBonsaiGameStateFaceUpCardsDeepCopy() {
        val originalGameState = BonsaiGameState(
            currentPlayer = Player("Alice", PlayerType.HUMAN, isLocal = true, ColorType.RED),
            players = mutableListOf(Player("Bob", PlayerType.HUMAN, isLocal = true, ColorType.BLUE)),
            botSpeed = 1,
            currentState = States.START_TURN
        ).apply {
            faceUpCards = mutableListOf(HelperCard(TileType.WOOD, 2))
        }

        val copiedGameState = originalGameState.deepCopy()

        // Modify the original faceUpCards
        originalGameState.faceUpCards.add(HelperCard(TileType.LEAF, 3))

        // Check that the copied faceUpCards is unaffected
        assertNotEquals(originalGameState.faceUpCards.size, copiedGameState.faceUpCards.size)
    }

}









