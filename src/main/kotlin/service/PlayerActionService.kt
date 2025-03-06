package service

class PlayerActionService {
    /**
     * Restores the last action
     *
     * preconditions:
     * - The game has started and is currently running
     * - Last action(s) must be undone before
     *
     * post conditions:
     * - Last action(s) is(are) restored
     *
     * @throws IllegalStateException if no action was undone before
     */
    fun redo(){}

    /**
     * Reverses the last action(s)
     *
     * preconditions:
     * - A previous action must exist
     *
     * post conditions:
     * - The last action(s) is(are) reversed
     *
     * @throws IllegalStateException if no previous action exist (game has just started)
     */
    fun undo(){}

    /**
     * Meditate to receive a zen card:
     * - tool card
     * - growth card
     * - helper card
     * - master card
     * - parchment card
     * and also receive play tiles based on the position of the card chosen
     *
     * preconditions:
     * - The game has started and is currently running
     *
     * post conditions:
     * - Player has received a zen card
     * - Empty spot in board is filled by pushing every card to the right
     * and filling the left empty spot with a new card from the zen deck
     *
     * @param cardPosition The position of the selected zen card
     * @throws IllegalStateException if player has done an action before
     *
     */
    fun meditate(cardPosition: Int){}

    fun cultivate(){}

    /**
     * Place a bonsai tile to players bonsai tree
     *
     * preconditions:
     * - Player has chosen action cultivate, or has a helper card
     * - Player can place a wood tile to his bonsai tree
     *
     * post conditions:
     * - bonsai tile(s) are added to bonsai tree
     *
     * @param tile The bonsai tile to be placed
     * @param tilePosition The position of the bonsai tile to be placed
     * @throws IllegalArgumentException if the bonsai tile is not playable
     * @throws IllegalArgumentException if the tile position is invalid
     */
    fun playTile(tile: Tile, tilePosition: Pair<Int,Int>){}

    /**
     * Ends the turn of the current player
     *
     * preconditions:
     * - player has played an action (meditate or cultivate)
     *
     * post conditions:
     * - The current player switches to the next player
     * @throws IllegalStateException if player has not played an action
     */
    fun endTurn(){}

    /**
     * Removes bonsai tile(s) from players bonsai tree
     *
     * preconditions:
     * - The game has started and is currently running
     * - Bonsai tree has tiles to be removed from (bonsai tree is not empty)
     *
     * post conditions:
     * - The bonsai tile(s) is(are) removed
     *
     * @param tile The bonsai tile(s) to be removed
     * @param tilePosition The position of the bonsai tile to be removed from
     * @throws IllegalStateException if there is no tile (bonsai tree is empty)
     */
    fun removeFromTree(tile: Tile, tilePosition: Pair<Int,Int>){}

    /**
     * Saves the current game state
     *
     * preconditions:
     * - The game must exist
     *
     * post conditions:
     * - The game state is saved
     *
     * @throws IllegalStateException if there is no existing game
     */
    fun saveGame(){}

    /**
     * Checks if a bonsai tile can be played based on the symbols shown on
     * Seishi tile or growth cards
     *
     * preconditions:
     * - player must have bonsai tiles to place with
     * - player must have bonsai tile shown on Seishi tile or growth cards
     *
     * @param tile The bonsai tile to be placed
     * @return true if bonsai tile can be placed
     * @throws IllegalArgumentException if player has no bonsai tiles to place
     */
    fun canPlayTile(tile: Tile): Boolean{}

    /**
     * Checks if a bonsai tile can be played based on the game rules
     *
     * Rules:
     * - A wood tile must be placed on another wood tile
     * - A leaf tile must be placed on a wood tile
     * - A flower tile must be placed on a leaf tile
     * - A fruit tile must be placed in between two leave tiles
     *
     * @param tile The bonsai tile to be placed
     * @param tilePosition The position of the bonsai tile to be placed
     * @return true if bonsai tile can be placed
     * @throws IllegalArgumentException if player has no bonsai tiles to place
     * @throws IllegalArgumentException if bonsai tile is played in invalid position
     */
    fun canPlayTile(tile: Tile, tilePosition: Pair<Int,Int>): Boolean{}

    /**
     * Checks if player can claim a goal tile
     *
     * @return true if player reached conditions to claim goal tile
     */
    fun canClaimGoalTile(): Boolean{}

    /**
     * Player claims or renounces goal tile
     *
     * preconditions:
     * - player has reached conditions to claim a goal tile
     *
     * post conditions:
     * - player has accepted the goal tile or
     * - goal tile gets locked from player
     *
     * @param claim true if player accepts goal tile, otherwise false
     *
     */
    fun claimOrRenounceGoal(claim: Boolean){}

    /**
     * Checks if player can redo his turn
     *
     * preconditions:
     * - Player has undone action(s)
     *
     * @return true if redo is available, otherwise false
     */
    fun canRedo(): Boolean{}

    /**
     * Checks if player can undo his turn
     *
     * preconditions:
     * - A previous player action exists
     *
     * @return true if undo is available, otherwise false
     */
    fun canUndo(): Boolean{}

    /**
     * Checks if player has played an action before ending his turn
     *
     * preconditions:
     * - Player must play an action (cultivate or meditate)
     *
     *  @return true if player can end turn
     */
    fun canEndTurn(): Boolean{}

    /**
     * Discards tile(s) from supply if over personal capacity limit
     *
     * preconditions:
     * - There are more tiles in personal supply than allowed
     *
     * post conditions:
     * - bonsai tile(s) are removed from players personal supply
     *
     * @throws IllegalStateException if bonsai tiles not over capacity limit
     */
    fun discardSupplyTile(){}
}
