package service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponse
import edu.udo.cs.sopra.ntf.*
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus


/**
 * [BoardGameClient] implementation for network communication.
 *
 * @param networkService the [NetworkService] to potentially forward received messages to.
 */
class BonsaiNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    var networkService: NetworkService
) :
    BoardGameClient(playerName, host, secret, NetworkLogging.VERBOSE) {


    var sessionID: String? = null
    var otherPlayerNames: MutableList<String> = mutableListOf()

    override fun onCreateGameResponse(response: CreateGameResponse) {
        BoardGameApplication.run {
            check(networkService.connectionState == ConnectionState.WAITING_FOR_HOST_CONFIRMATION)
            {"unexpected CreateGameResponse"}

            when(response.status) {
                CreateGameResponseStatus.SUCCESS -> {
                    networkService.updateConnectionState(ConnectionState.WAITING_FOR_GUEST)
                    sessionID = response.sessionID
                }
                else -> disconnectAndError(response.status)
            }
        }
    }

    override fun onJoinGameResponse(response: JoinGameResponse) {
        BoardGameApplication.run {
            check(networkService.connectionState == ConnectionState.WAITING_FOR_JOIN_CONFIRMATION)
            {"unexpected JoinGameResponse"}

            when(response.status) {
                JoinGameResponseStatus.SUCCESS -> {
                    sessionID = response.sessionID
                    networkService.updateConnectionState((ConnectionState.WAITING_FOR_INIT))
                }
                else -> disconnectAndError(response.status)
            }
        }
    }

    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        BoardGameApplication.run{
            check(networkService.connectionState == ConnectionState.WAITING_FOR_GUEST)
            {"not awaiting any guests."}

            check(otherPlayerNames.size <= 3)
            {"more than 4 players."}

            otherPlayerNames.add(notification.sender)
        }
    }


    @GameActionReceiver
    fun onStartGameMessageReceived(message: StartGameMessage) {
        BoardGameApplication.run{
            networkService.receiveStartGameMessage(
                message = message
            )
        }
    }

    fun onCultivateMessage(message: CultivateMessage) {
        //TODO()
    }

    fun onMeditateMessage(message: MeditateMessage) {
        //TODO()
    }

    private fun disconnectAndError(message: Any) {
        networkService.disconnect()
        error(message)
    }

}
