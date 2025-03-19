package service

import edu.udo.cs.sopra.ntf.*
import entity.*
import util.ZenCardLoader
import kotlin.test.*

class NetworkServiceTest {

    private fun setUpGame(): RootService {
        val rootService = RootService()
        val players = mutableListOf(
            Player("Tom", PlayerType.HUMAN, true, ColorType.RED),
            Player("Tomy", PlayerType.HUMAN, true, ColorType.BLUE),
            Player("Tomi", PlayerType.HUMAN, true, ColorType.BLACK)
        )
        val gameState = BonsaiGameState(
            currentPlayer = players.first(),
            players = players,
            botSpeed = 1,
            currentState = States.CULTIVATE
        )
        gameState.zenDeck = mutableListOf(
            HelperCard(TileType.LEAF, 1),
            HelperCard(TileType.WOOD, 2),
            HelperCard(TileType.FRUIT, 3),
            HelperCard(TileType.FRUIT, 4),
            HelperCard(TileType.FRUIT, 5)
        )
        val game = BonsaiGame()
        game.currentBonsaiGameState = gameState
        rootService.currentGame = game
        return rootService
    }

    @Test
    fun testCreateGame() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.createGame("baum25", "Tom", "1234")
        assertEquals(ConnectionState.WAITING_FOR_HOST_CONFIRMATION, networkService.connectionState)
    }

    @Test
    fun testJoinGame() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.joinGame("baum25", "Tom", "1234")
        assertEquals(ConnectionState.WAITING_FOR_JOIN_CONFIRMATION, networkService.connectionState)
    }

    @Test
    fun testStartGameMessage(){
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        val players = rootService.currentGame?.currentBonsaiGameState?.players
        checkNotNull(players)
        val goalTiles = mutableListOf(GoalTileType.BROWN, GoalTileType.PINK, GoalTileType.GREEN)
        networkService.myName = "Tom"
        networkService.setConnectionStateTest(ConnectionState.WAITING_FOR_GUEST)
        networkService.sendStartGameMessage(players, goalTiles)
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState)
        val checkStateForHost = if (networkService.myName == gameState.players.first().name) {
            ConnectionState.PLAYING_MY_TURN
        } else {
            ConnectionState.WAITING_FOR_OPPONENT
        }
        assertEquals(checkStateForHost, networkService.connectionState)
    }

    @Test
    fun testSendMeditateMessage() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.setConnectionStateTest(ConnectionState.PLAYING_MY_TURN)
        networkService.toBeSentMeditateMessage.playedTiles.add(TileType.LEAF to Pair(1, -1))
        networkService.sendMeditateMessage()
        assertEquals(ConnectionState.WAITING_FOR_OPPONENT, networkService.connectionState)
        assertTrue(networkService.toBeSentMeditateMessage.playedTiles.isEmpty())
    }

    @Test
    fun testSendCultivateMessage() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.setConnectionStateTest(ConnectionState.PLAYING_MY_TURN)
        networkService.toBeSentCultivateMessage.playedTiles.add(TileType.LEAF to Pair(1, -1))
        networkService.sendCultivateMessage()
        assertEquals(ConnectionState.WAITING_FOR_OPPONENT, networkService.connectionState)
        assertTrue(networkService.toBeSentCultivateMessage.playedTiles.isEmpty())
    }

    @Test
    fun testReceiveStartGameMessage() {
        val rootService = RootService()
        val networkService = NetworkService(rootService)

        rootService.currentGame = null
        rootService.gameService = GameService(rootService)
        networkService.myName = "Tom"
        networkService.setConnectionStateTest(ConnectionState.WAITING_FOR_INIT)
        val zenDeck = ZenCardLoader().readAllZenCards(3)
        val orderedCards = zenDeck.map { Pair(it.cardType.toCardTypeMessage(), it.id) }
        val message = StartGameMessage(
            orderedPlayerNames = listOf(
                Pair("Tom", ColorTypeMessage.RED),
                Pair("Tomy", ColorTypeMessage.BLUE),
                Pair("Tomi", ColorTypeMessage.BLACK)
            ),
            chosenGoalTiles = listOf(
                GoalTileTypeMessage.BROWN,
                GoalTileTypeMessage.PINK,
                GoalTileTypeMessage.GREEN
            ),
            orderedCards = orderedCards
        )
        networkService.receiveStartGameMessage(message)
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState)
        assertEquals(3, gameState.players.size)
        assertEquals("Tom", gameState.players.first().name)
        assertEquals("Tomi", gameState.players.last().name)
        assertEquals(9, gameState.goalTiles.size)
        val checkStateForHost = if (networkService.myName == gameState.players.first().name) {
            ConnectionState.PLAYING_MY_TURN
        } else {
            ConnectionState.WAITING_FOR_OPPONENT
        }
        assertEquals(checkStateForHost, networkService.connectionState)
    }

    @Test
    fun testReceiveMeditateMessage() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.setConnectionStateTest(ConnectionState.WAITING_FOR_OPPONENT)
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState)
        println("Zen deck: ${gameState.zenDeck.map { it.id }}")
        val tile = Tile(null, null, TileType.LEAF)
        gameState.currentPlayer.personalSupply.add(tile)
        gameState.currentPlayer.playableTilesCopy.add(TileType.LEAF)
        gameState.currentPlayer.bonsaiTree[Pair(0, -1)] = Tile(null, null, TileType.WOOD)
        val message = MeditateMessage(
            removedTilesAxialCoordinates = listOf(),
            chosenCardPosition = 2,
            playedTiles = listOf(TileTypeMessage.LEAF to Pair(1, -1)),
            drawnTiles = listOf(TileTypeMessage.WOOD),
            claimedGoals = listOf(),
            renouncedGoals = listOf(),
            discardedTiles = listOf()
        )
        println("Zen Deck Size: ${gameState.zenDeck.size}")
        println("Chosen Card Position: ${message.chosenCardPosition}")
        networkService.receiveMeditateMessage(message, "Tom")
        assertEquals(ConnectionState.WAITING_FOR_OPPONENT, networkService.connectionState)
    }

    @Test
    fun testReceiveCultivateMessage() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.setConnectionStateTest(ConnectionState.WAITING_FOR_OPPONENT)
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState)
        val tile = Tile(null, null, TileType.LEAF)
        gameState.currentPlayer.personalSupply.add(tile)
        gameState.currentPlayer.playableTilesCopy.add(TileType.LEAF)
        val message = CultivateMessage(
            removedTilesAxialCoordinates = listOf(),
            playedTiles = listOf(TileTypeMessage.LEAF to Pair(1, -1)),
            claimedGoals = listOf(),
            renouncedGoals = listOf()
        )
        networkService.receiveCultivateMessage(message, "Tom")
        assertEquals(ConnectionState.WAITING_FOR_OPPONENT, networkService.connectionState)
    }

    @Test
    fun testUpdateConnectionState() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.updateConnectionState(ConnectionState.WAITING_FOR_GUEST)
        assertEquals(ConnectionState.WAITING_FOR_GUEST, networkService.connectionState)
    }

    @Test
    fun testDisconnect() {
        val rootService = setUpGame()
        val networkService = NetworkService(rootService)
        networkService.setConnectionStateTest(ConnectionState.CONNECTED)
        networkService.disconnect()
        assertEquals(ConnectionState.DISCONNECTED, networkService.connectionState)
    }


}
