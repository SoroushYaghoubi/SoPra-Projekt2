package service

import entity.*
import gui.*
import java.io.File
import kotlinx.serialization.json.Json

/**
 * Service layer class that provides the logic for actions taken by the System during the game.
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Starts a new game with:
     * - players
     * - specified player order and
     * - decide if network game or local game
     *
     * preconditions:
     * - There is no existing game.
     * - player must set their names and player order.
     * - must decide if played online or local.
     *
     * post conditions:
     * - Game started.
     *
     * @param players List of player names that are playing this game.
     * @param playerOrder A list of Players deciding the player order.
     * @param networkGame true if game is played online otherwise false.
     */
    fun startNewGame(players: MutableList<String>,
                     playerOrder: MutableList<Player>,
                     networkGame: Boolean){}

    /**
     * Continues a previously saved game state.
     *
     * preconditions:
     * - There is an existing game which was saved before.
     *
     * post conditions:
     * - Game has continued with old game state.
     *
     * @throws IllegalStateException if there is no previously saved game.
     */
    fun continueGame(){
        val savedGameState = File("./savedGameState.json")
        if (!savedGameState.exists()){
            throw IllegalStateException ("There is no saved gameState")
        }
        val jsonString = savedGameState.readText()
        val game = Json.decodeFromString<BonsaiGame>(jsonString)
        rootService.currentGame = game
        onAllRefreshables { refreshAfterGameStart() }
    }

    /**
     * Switches the turn to the next player. Assuming player list is in correct turn order
     *
     * preconditions:
     * - Current player has ended his turn.
     *
     * post conditions:
     * - The current player switched to the next player.
     */
    fun switchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        val currentIndex = gameState.players.indexOf(gameState.currentPlayer)

        // get next player in the list, looping back to the first player at end of the list
        val nextIndex = (currentIndex + 1) % gameState.players.size
        gameState.currentPlayer = gameState.players[nextIndex]

        // refreshes here and not in endTurn
        onAllRefreshables { refreshAfterEndTurn() }
    }

    /**
     * Shows the winner of the game.
     *
     * preconditions:
     * - Zen deck is empty and all players played their last action.
     * NOTE by Giang: this is already checked in endTurn of PlayerActionService
     * post conditions:
     * - The winner is shown.
     *
     * @throws IllegalStateException if game isn't over yet.
     */
    fun showWinner(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        // Get the winner's name using the index
        gameState.players[getWinnerIndex()].name
    }

    // Help function to get the index of the winner
    private fun getWinnerIndex(): Int {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }
        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        val maxScore = gameState.players.maxOf { it.score }
        val playerOrder: MutableList<Player> = mutableListOf()

        // List of players as candidate for winner: those with highest score
        val candidates = gameState.players.filter { it.score == maxScore }

        // If only one has the highest score
        if (candidates.size == 1) {
            return gameState.players.indexOf(candidates[0])
        }

        // In case of a tie, find the candidate who is farthest from the starting player in the playOrder list
        var farthestIndex = 0
        var maxDistance = 0

        // Checking the position of candidates in playerOrder, the player with the highest index is then the farthest
        for (candidate in candidates) {
            val candidateIndexInOrder = playerOrder.indexOf(candidate)
            if (candidateIndexInOrder > maxDistance) {
                maxDistance = candidateIndexInOrder
                farthestIndex = gameState.players.indexOf(candidate)
            }
        }
        return farthestIndex
    }

    /**
     * Calculates the score of the current player.
     */
    fun calculateScore(){}

    /**
     * Refills the board after a player has meditated.
     *
     * preconditions:
     * - player has meditated.
     * - zen deck is not empty.
     *
     * post conditions:
     * - all zen decks got shifted to the right side and
     * empty spot gets filled by zen deck.
     *
     * @throws IllegalStateException if zen deck is empty.
     */
    fun refillBoard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        if (gameState.zenDeck.isEmpty()) {
            throw IllegalStateException("Zen deck is empty.")
        }

        if (gameState.faceUpCards.size < 4) {
            val newCard = gameState.zenDeck.removeAt(0)
            // new card is added at index 3
            gameState.faceUpCards.add(newCard)
        }

        onAllRefreshables { refreshAfterChooseCard() }
    }

}
