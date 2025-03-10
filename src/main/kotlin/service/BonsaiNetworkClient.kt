package service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponse


class BonsaiNetworkClient(private val networkService: NetworkService) :
    BoardGameClient("playerName", "host", "secret") {
    //TODO(the parameters from BoardGameClient's constructor are not final)

    override fun onCreateGameResponse(response: CreateGameResponse) {

    }

    override fun onJoinGameResponse(response: JoinGameResponse) {

    }

    override fun onPlayerJoined(notification: PlayerJoinedNotification) {

    }

    /**
    fun onStartGameMessageReceived(message : StartGameMessage) {

    }

    fun onCultivateMessage(message : CultivateMessage) {

    }

    fun onMeditateMessage(message : MeditateMessage) {

    }
    */
}
