package service

import kotlin.test.*

/**
 * Test class for [NetworkService]
 */
class NetworkScenarios {

    /**
     * Tests a whole network session with the server, including handshakes, game init and game messages. But only
     * things regarding network. For example if the messages are being interpreted correctly, is not being tested here.
     */
    @Test
    fun `scenario total`(){
        val hostNet = RootService().networkService
        val joinNet = RootService().networkService

        // ---------------------------------------------------------
        // --------------- operational bracket [[[[[ ---------------
        // ---------------------------------------------------------
        assertEquals(hostNet.connectionState, ConnectionState.DISCONNECTED)

        // --------------- create/join game ---------------
        hostNet.createGame("baum25", "host_game", "my6")
        assertEquals(hostNet.connectionState, ConnectionState.WAITING_FOR_HOST_CONFIRMATION)



        joinNet.joinGame("baum25", "join_game", "my7")
        assertEquals(joinNet.connectionState, ConnectionState.WAITING_FOR_JOIN_CONFIRMATION)


        // --------------- send messages ---------------
//        hostNet.sendStartGameMessage(mutableListOf(), mutableListOf())

        // --------------- receive messages ---------------



        // ---------------------------------------------------------
        // --------------- operational bracket ]]]]] ---------------
        // ---------------------------------------------------------
        joinNet.disconnect()
        hostNet.disconnect()
        assertEquals(hostNet.connectionState, ConnectionState.DISCONNECTED)
    }
}