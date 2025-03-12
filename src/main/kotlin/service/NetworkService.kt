package service

import edu.udo.cs.sopra.ntf.ColorTypeMessage
import util.CSVLoader
import util.ZenCardLoader
import edu.udo.cs.sopra.ntf.*

/**
 * Service layer class that realizes the necessary logic for sending and receiving messages
 * in multiplayer network games. Bridges between the [BonsaiNetworkClient] and the other services.
 */
class NetworkService(private val rootService: RootService,
    private var connectionState : ConnectionState = ConnectionState.DISCONNECTED) {

    var client : BonsaiNetworkClient? = null

    fun createGame(secret : String, name : String, sessionID : String?) {
        //TODO
        /**
        val csvLoader = CSVLoader()
        val zenCardLoader = ZenCardLoader()
        val deck = zenCardLoader.readAllZenCards(3)
        */
    }


    fun joinGame(secret : String, name : String, sessionID : String) {
        //TODO
    }


    fun sendStartGameMessage(message : StartGameMessage) {
        //TODO
    }

    fun sendMeditateMessage(message : MeditateMessage) {
        //TODO
    }

    fun sendCultivateMessage(message : CultivateMessage) {
        //TODO
    }

    fun receiveStartGameMessage(message : StartGameMessage) {
        //TODO
    }

    fun receiveMeditateMessage(message : MeditateMessage) {
        //TODO
    }

    fun receiveCultivateMessage(message : CultivateMessage) {
        //TODO
    }


    fun updateConnectionState(newState : ConnectionState) {
        //TODO
    }

    private fun connect(secret: String, name : String) : Boolean {
        //TODO
        return true
    }

    private fun disconnect() {
        //TODO
    }
}
