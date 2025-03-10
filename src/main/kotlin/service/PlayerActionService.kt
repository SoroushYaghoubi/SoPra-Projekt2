package service

import entity.*
import kotlinx.serialization.json.Json
import java.io.File

/**
 * The service layer class which contains the player's action functions.
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Meditate to receive a zen card:
     * - tool card
     * - growth card
     * - helper card
     * - master card
     * - parchment card
     * and also receive play tiles based on the position of the card chosen.
     *
     * preconditions:
     * - The game has started and is currently running.
     *
     * post conditions:
     * - Player has received a zen card.
     * - Empty spot in board is filled by pushing every card to the right
     * and filling the left empty spot with a new card from the zen deck.
     * - updates that the current player has done an action during his turn.
     * - sets the current state to cultivate
     *
     * @param cardPosition The position of the selected zen card.
     * @throws IllegalStateException if player has already done an action during his current turn.
     */
    fun meditate(cardPosition: Int) {}

    /**
     * Action to place a tile from personal supply on bonsai tree.
     *
     * preconditions:
     * - The game has started and is currently running.
     *
     * post conditions:
     * - updates that the current player has done an action during his turn.
     * - sets the current state to cultivate
     *
     * @throws IllegalStateException if player has already done an action during his current turn.
     */
    fun cultivate() {}


    /**
     * Ends the turn of the current player.
     *
     * preconditions:
     * - player has played an action (meditate or cultivate).
     *
     * post conditions:
     * - The current player switches to the next player.
     *
     * @throws IllegalStateException if the current player has not played an action yet.
     */
    fun endTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        require(canEndTurn())

        // Trigger end game by counting the turn of player
        if (gameState.zenDeck.isEmpty()) {
            gameState.endGameCounter++
        }

        // When the counter = the number of players -> all players finish their last turn
        if (gameState.endGameCounter == gameState.players.size) {
            rootService.gameService.showWinner()
        } else {
            rootService.gameService.calculateScore()
            rootService.gameService.switchPlayerTurn()
        }

        // Update history -> later

    }

    /**
     * Removes bonsai tile from players bonsai tree.
     *
     * preconditions:
     * - The game has started and is currently running.
     * - Bonsai tree has tiles to be removed from (bonsai tree is not empty).
     *
     * post conditions:
     * - The bonsai tile is removed from the bonsai tree.
     *
     * @param tile The bonsai tile(s) to be removed.
     * @param tilePosition The position of the bonsai tile to be removed from.
     * @throws IllegalStateException if there is no tile (bonsai tree is empty).
     */
    fun removeFromTree(tile: Tile, tilePosition: Pair<Int, Int>) {}


    /**
     * Checks if player can claim a goal tile.
     *
     * @return true if player reached conditions to claim a goal tile.
     */
    fun canClaimGoalTile(): Boolean {
        TODO("just remove this todo. this is only for kotlin compiler to stop complaining")
    }

    /**
     * Player claims or renounces goal tile.
     *
     * preconditions:
     * - player has reached conditions to claim a goal tile.
     *
     * post conditions:
     * - player has accepted the goal tile or
     * - goal tile gets locked from player
     *
     * @param claim true if player accepts goal tile, otherwise false.
     *
     */
    fun claimOrRenounceGoal(claim: Boolean) {}

    /**
     * Checks if player has played an action before ending his turn.
     *
     * preconditions:
     * - Player must play an action (cultivate or meditate).
     *
     *  @return true if player can end his turn.
     */
    fun canEndTurn(): Boolean {
        TODO("just remove this todo. this is only for kotlin compiler to stop complaining")
    }

    /**
     * Discards tile(s) from supply if the personal capacity limit has been exceeded.
     *
     * preconditions:
     * - There are more tiles in personal supply than allowed.
     *
     * post conditions:
     * - bonsai tile(s) are removed from players personal supply.
     *
     * @throws IllegalStateException if personal supply is not over capacity limit.
     */
    fun discardSupplyTile() {}

}

