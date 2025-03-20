package service

import entity.ColorType
import entity.GoalTileType
import entity.Player
import entity.PlayerType
import util.SECRET_KEY
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [BonsaiNetworkClient]
 */
class NetWorkClientTest {

    /**
     * Test if correctly received StartGameMessage
     */
    @Test
    fun onStartGameMessageReceived() {
        val rootService1 = RootService()
        val rootService2 = RootService()
        val sessionId = (10001.. 50000).random().toString()
        rootService1.networkService.createGame (
                SECRET_KEY,
                "Gary",
                sessionId
            )

        Thread.sleep(2000)
        rootService2.networkService.joinGame(
            SECRET_KEY,
            "HIHI",
            sessionId
        )
        Thread.sleep(2000)

        val player1 = Player(
            "Gary", PlayerType.HUMAN, true, ColorType.RED)
        val player2 = Player(
            "HIHI", PlayerType.HUMAN, false, ColorType.PURPLE)

        rootService1.networkService.sendStartGameMessage(
            mutableListOf(player1,player2),
            mutableListOf(
                GoalTileType.BROWN, GoalTileType.PINK, GoalTileType.GREEN)
        )
        Thread.sleep(1000)

        val game1 = rootService1.currentGame?.currentBonsaiGameState
        val game2 = rootService2.currentGame?.currentBonsaiGameState
        checkNotNull(game1)
        checkNotNull(game2)
        assertEquals(game1.zenDeck.size, game2.zenDeck.size)
    }
}
