package service

import entity.*

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
    fun continueGame(){}

    /**
     * Switches the turn to the next player.
     *
     * preconditions:
     * - Current player has ended his turn.
     *
     * post conditions:
     * - The current player switched to the next player.
     */
    fun switchPlayerTurn(){}

    /**
     * Shows the winner of the game.
     *
     * preconditions:
     * - Zen deck is empty and all players played their last action.
     *
     * post conditions:
     * - The winner is shown.
     *
     * @throws IllegalStateException if game isn't over yet.
     */
    fun showWinner(){}

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
    fun refillBoard(){}

}
