package service

class NetworkService(private val rootService: RootService,
    private var connectionState : ConnectionState = ConnectionState.DISCONNECTED) {

    var client : BonsaiNetworkClient? = null

    fun createGame(secret : String, name : String, sessionID : String?) {
        //TODO
    }

    fun joinGame(secret : String, name : String, sessionID : String) {
        //TODO
    }

    /**
    fun sendStartGameMessage(message : StartGameMessage) {
        //TODO
    }

    fun sendMeditateMessage(message : MeditateMessage) {
        //TODO
    }

    fun sendCultivateMessage(message : CultivateMessage) {
        //TODO
    }
    */

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
