package service

import entity.Player
import entity.Tile
import entity.TileType


/**
 * The service layer class which contains all actions related to the bonsai tree
 */
class TreeService(private val rootService: RootService) : AbstractRefreshingService() {

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

        if (!canPlayTile(tile, tilePosition)) {
            throw IllegalArgumentException("Tile can not be played")
        }

        val currentPlayer = getCurrentPlayer()
        currentPlayer.bonsaiTree[tilePosition] = tile
        currentPlayer.personalSupply.remove(tile)

        if (tile.tileType in currentPlayer.playableTilesCopy) {
            currentPlayer.playableTilesCopy.remove(tile.tileType)
        } else if (TileType.ANY in currentPlayer.playableTilesCopy) {
            currentPlayer.playableTilesCopy.remove(TileType.ANY)
        }

        // update message
        val net = rootService.networkService
        if(net.connectionState != ConnectionState.DISCONNECTED &&
            currentPlayer.isLocal){
            net.toBeSentCultivateMessage.playedTiles.add(
                (tile.tileType to (tilePosition))
            )
        }
        // TODO: check if player has achieved a goal tile
        onAllRefreshables { refreshAfterPlayTile() }
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
    fun removeFromTree(tilePosition: Pair<Int, Int>) {
        //TODO(to be completed)

        val currentPlayer = getCurrentPlayer()

        // update message
        val net = rootService.networkService
        // TODO(if it's allowed then...)
        if(net.connectionState != ConnectionState.DISCONNECTED &&
            currentPlayer.isLocal){
            net.toBeSentCultivateMessage.removedTilesAxialCoordinates.add(tilePosition)
        }
    }

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
        if (!canPlayTile(tile)) {
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
        if (neighbourTiles.isEmpty()) {
            throw IllegalArgumentException("There are no adjacent cards")
        }
        if (tile.tileType == TileType.WOOD) {
            return neighbourTiles.contains(TileType.WOOD)
        }
        if (tile.tileType == TileType.LEAF) {
            return neighbourTiles.contains(TileType.WOOD)
        }
        if (tile.tileType == TileType.FLOWER) {
            return neighbourTiles.contains(TileType.LEAF)
        }
        if (tile.tileType == TileType.FRUIT) {
            if (neighbourTiles.first() == TileType.LEAF && neighbourTiles.last() == TileType.LEAF) {
                return true
            }
            for (i in 0..<neighbourTiles.size - 1) {
                val currentTile = neighbourTiles[i]
                val nextTile = neighbourTiles[i + 1]
                if (currentTile == TileType.LEAF && nextTile == TileType.LEAF) {
                    return true
                }
            }
        }
        return false
    }

    private fun getCurrentPlayer(): Player {
        val currentGameState = checkNotNull(rootService.currentGame?.currentBonsaiGameState)
        return currentGameState.currentPlayer
    }

}
