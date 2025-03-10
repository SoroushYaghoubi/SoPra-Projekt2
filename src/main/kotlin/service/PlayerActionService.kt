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
     * Place a bonsai tile to players bonsai tree.
     *
     * preconditions:
     * - Player has chosen action cultivate, or has a helper card.
     * - Player can place a wood tile to his bonsai tree.
     *
     * post conditions:
     * - bonsai tile is added to bonsai tree.
     *
     * @param tile The bonsai tile to be placed.
     * @param tilePosition The position of the bonsai tile to be placed.
     * @throws IllegalArgumentException if the bonsai tile is not playable.
     * @throws IllegalArgumentException if the tile position is invalid.
     */
    fun playTile(tile: Tile, tilePosition: Pair<Int, Int>) {
        if (!canPlayTile(tile, tilePosition)){
            throw IllegalArgumentException("Tile can not be played")
        }
        val currentPlayer = getCurrentPlayer()
        currentPlayer.bonsaiTree[tilePosition] = tile
        currentPlayer.personalSupply.remove(tile)
        currentPlayer.playableTilesCopy.remove(tile.tileType)
        // TODO: check if player has achieved a goal tile
        onAllRefreshables { refreshAfterPlayTile() }
    }

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
     * Checks if a bonsai tile can be played based on the symbols shown on
     * Seishi tile or growth cards.
     *
     * preconditions:
     * - player must have bonsai tiles to place with.
     * - player must have bonsai tile shown on Seishi tile or growth cards.
     *
     * @param tile The bonsai tile to be placed.
     * @return true if bonsai tile can be placed.
     * @throws IllegalArgumentException if player has no bonsai tiles to place.
     */
    fun canPlayTile(tile: Tile): Boolean {
        val currentPlayer = getCurrentPlayer()
        if (!currentPlayer.personalSupply.contains(tile)) {
            throw IllegalArgumentException("Player does not have this bonsai tile in hand")
        }
        return currentPlayer.playableTilesCopy.contains(tile.tileType)
    }

    /**
     * Checks if a bonsai tile can be played based on the game rules.
     *
     * Rules:
     * - A wood tile must be placed on another wood tile.
     * - A leaf tile must be placed on a wood tile.
     * - A flower tile must be placed on a leaf tile.
     * - A fruit tile must be placed in between two leave tiles.
     *
     * @param tile The bonsai tile to be placed.
     * @param tilePosition The position of the bonsai tile to be placed.
     * @return true if bonsai tile can be placed.
     * @throws IllegalArgumentException if player has no bonsai tiles to place.
     * @throws IllegalArgumentException if bonsai tile is played in invalid position.
     */
    fun canPlayTile(tile: Tile, tilePosition: Pair<Int, Int>): Boolean {
        if (!canPlayTile(tile)){
            return false
        }
        val currentPlayer = getCurrentPlayer()
        if (currentPlayer.bonsaiTree.containsKey(tilePosition)) {
            throw IllegalArgumentException("Position is already occupied")
        }
        val tree = currentPlayer.bonsaiTree
        val q = tilePosition.first
        val r = tilePosition.second
        val neighbourTiles = mutableListOf(
            tree.getOrDefault(Pair(q + 1, r), null)?.tileType,
            tree.getOrDefault(Pair(q, r + 1), null)?.tileType,
            tree.getOrDefault(Pair(q - 1, r + 1), null)?.tileType,
            tree.getOrDefault(Pair(q - 1, r), null)?.tileType,
            tree.getOrDefault(Pair(q, r - 1), null)?.tileType,
            tree.getOrDefault(Pair(q + 1, r - 1), null)?.tileType,
        ).filterNotNull()
        if (neighbourTiles.isEmpty()){
            throw IllegalArgumentException("There are no adjacent cards")
        }
        if (tile.tileType == TileType.WOOD){
            return neighbourTiles.contains(TileType.WOOD)
        }
        if (tile.tileType == TileType.LEAF){
            return neighbourTiles.contains(TileType.WOOD)
        }
        if (tile.tileType == TileType.FLOWER){
            return neighbourTiles.contains(TileType.LEAF)
        }
        if (tile.tileType == TileType.FRUIT){
            if (neighbourTiles.first() == TileType.LEAF && neighbourTiles.last() == TileType.LEAF){
                return true
            }
            for (i in 0..<neighbourTiles.size - 1){
                val currentTile = neighbourTiles[i]
                val nextTile = neighbourTiles[i + 1]
                if (currentTile == TileType.LEAF && nextTile == TileType.LEAF){
                    return true
                }
            }
        }
        return false
    }

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

    private fun getCurrentPlayer(): Player {
        return checkNotNull(rootService.currentGame?.currentBonsaiGameState?.currentPlayer)
    }
}

