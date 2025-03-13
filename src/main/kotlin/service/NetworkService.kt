package service


import edu.udo.cs.sopra.ntf.*
import gui.Refreshable


/**
 * Service layer class that realizes the necessary logic for sending and receiving messages
 * in multiplayer network games. Bridges between the [BonsaiNetworkClient] and the other services.
 */
class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {

    companion object {
        const val SERVER_ADDRESS = "not real"
        const val GAME_ID = "idk yet"
    }

    var client : BonsaiNetworkClient? = null
    private set
    var connectionState : ConnectionState = ConnectionState.DISCONNECTED
    private set
    val myName = client?.playerName

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


    fun sendStartGameMessage(message: StartGameMessage) {
        check(connectionState == ConnectionState.WAITING_FOR_GUEST)
        {"currently not prepared to start a new hosted game."}

        val players = client?.otherPlayerNames
        checkNotNull(players)

        if(players.size < 2 || players.size > 4) {
            throw IllegalStateException("there should be 2 to 4 players")
        }

        //rootService.gameService.startNewGame()

        //it depends on who's the first player
        if(true) {
            //do something
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        } else {
            //maybe do something
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENT)
        }
    }

    fun sendMeditateMessage(message : MeditateMessage) {
        //TODO
    }

    fun sendCultivateMessage(message : CultivateMessage) {
        //TODO
    }

    fun receiveStartGameMessage(message : StartGameMessage) {
        check(connectionState == ConnectionState.WAITING_FOR_INIT)
        {"not waiting for game init message"}

        val orderedPair = message.orderedPlayerNames
        //decode message

        //construct game

        //initialise game


        if(myName == orderedPair.first().first) {
            //do something
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        } else {
            //maybe do something
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENT)
        }
    }

    fun receiveMeditateMessage(message : MeditateMessage) {
        //TODO
    }

    fun receiveCultivateMessage(message : CultivateMessage) {
        //TODO
    }


    fun updateConnectionState(newState : ConnectionState) {
        this.connectionState = newState
        onAllRefreshables {
            //refreshConnectionState(newState)
        }
    }

    private fun connect(secret: String, name : String) : Boolean {
        require(connectionState == ConnectionState.DISCONNECTED && client == null)
        { "already connected to another game" }

        require(secret.isNotBlank()) {"server secret must be given"}
        require(name.isNotBlank()) {"player name must be given"}

        val newClient =
            BonsaiNetworkClient(
                playerName = name,
                host = SERVER_ADDRESS,
                secret = secret,
                networkService = this
            )

        return if (newClient.connect()) {
            this.client = newClient
            true
        } else {
            false
        }
    }

    fun disconnect() {
        client?.apply {
            if (sessionID != null) leaveGame("Goodbye!")
            if (isOpen) disconnect()
        }
        client = null
        updateConnectionState(ConnectionState.DISCONNECTED)
    }
}
