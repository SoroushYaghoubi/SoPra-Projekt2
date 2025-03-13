package service


import edu.udo.cs.sopra.ntf.*
import gui.Refreshable


/**
 * Service layer class that realizes the necessary logic for sending and receiving messages
 * in multiplayer network games. Bridges between the [BonsaiNetworkClient] and the other services.
 */
class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {

    companion object {
        /** URL of the BGW net server hosted for SoPra participants */
        const val SERVER_ADDRESS = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"
        /** Name of the game as registered with the server */
        const val GAME_ID = "Bonsai"
    }

    /** Network client. Nullable for offline games. */
    var client : BonsaiNetworkClient? = null
    private set

    /** current state of the connection in a network game. */
    var connectionState : ConnectionState = ConnectionState.DISCONNECTED
    private set

    // our own name
    val myName = client?.playerName


    /**
     * Connects to server and creates a new game session.
     *
     * @param secret Server secret.
     * @param name Player name.
     * @param sessionID identifier of the hosted session (to be used by guest on join)
     *
     * @throws IllegalStateException if already connected to another game or connection attempt fails
     */
    fun createGame(secret : String, name : String, sessionID : String?) {
        if (!connect(secret, name)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        if (sessionID.isNullOrBlank()) {
            client?.createGame(GAME_ID, "Welcome!^_^")
        } else {
            client?.createGame(GAME_ID, sessionID, "Welcome!^_^")
        }
        updateConnectionState(ConnectionState.WAITING_FOR_HOST_CONFIRMATION)
    }

    /**
     * Connects to server and joins a game session as guest player.
     *
     * @param secret Server secret.
     * @param name Player name.
     * @param sessionID identifier of the joined session (as defined by host on create)
     *
     * @throws IllegalStateException if already connected to another game or connection attempt fails
     */
    fun joinGame(secret : String, name : String, sessionID : String) {
        if (!connect(secret, name)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        client?.joinGame(sessionID, "Welcome!^o^")

        updateConnectionState(ConnectionState.WAITING_FOR_JOIN_CONFIRMATION)
    }

    /**
     * set up the game using [GameService.startNewGame] and send the game init message
     * to the guest players. [connectionState] needs to be [ConnectionState.WAITING_FOR_GUEST].
     * This method should be called when the host(we) decide to start the game and
     * when there are 2 to 4 players in the lobby
     *
     * @throws IllegalStateException if [connectionState] != [ConnectionState.WAITING_FOR_GUEST]
     * @throws IllegalStateException if player size < 2 or player size > 4
     */
    fun sendStartGameMessage() {
        check(connectionState == ConnectionState.WAITING_FOR_GUEST)
        {"currently not prepared to start a new hosted game."}

        val players = client?.otherPlayerNames
        checkNotNull(players)

        //val colorMessage = message.orderedPlayerNames.first().second
        //val color = colorMessage.toColor()

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

    /**
     * Initializes the entity structure with the data given by the [StartGameMessage] sent by the host.
     * [connectionState] needs to be [ConnectionState.WAITING_FOR_INIT].
     * This method should be called from the [BonsaiNetworkClient] when the host sends the init message.
     * See [BonsaiNetworkClient.onStartGameMessageReceived].
     *
     * @throws IllegalStateException if not currently waiting for an init message
     */
    fun receiveStartGameMessage(message : StartGameMessage) {
        check(connectionState == ConnectionState.WAITING_FOR_INIT)
        {"not waiting for game init message"}

        val orderedPair = message.orderedPlayerNames
        //decode message

        //construct game

        //initialise game
        val colorType = message.orderedPlayerNames.first().second.toColor()


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

    /**
     * Updates the [connectionState] to [newState] and notifies
     * all refreshables via [Refreshable.refreshConnectionState]
     */
    fun updateConnectionState(newState : ConnectionState) {
        this.connectionState = newState
        onAllRefreshables {
            refreshConnectionState(newState)
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

    /**
     * Disconnects the [client] from the server, nulls it and updates the
     * [connectionState] to [ConnectionState.DISCONNECTED]. Can safely be called
     * even if no connection is currently active.
     */
    fun disconnect() {
        client?.apply {
            if (sessionID != null) leaveGame("Goodbye!")
            if (isOpen) disconnect()
        }
        client = null
        updateConnectionState(ConnectionState.DISCONNECTED)
    }
}
