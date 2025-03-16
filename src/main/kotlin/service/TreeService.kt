package service

import entity.Player
import entity.States
import entity.Tile
import entity.TileType
import util.POT


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

        require(canPlayTile(tile, tilePosition)) { "Tile can not be played" }

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
        if (net.connectionState != ConnectionState.DISCONNECTED &&
            currentPlayer.isLocal
        ) {
            net.toBeSentCultivateMessage.playedTiles.add(
                (tile.tileType to (tilePosition))
            )
        }
        // TODO: check if player has achieved a goal tile

        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        game.goalTiles.forEach {
            if (rootService.playerActionService.canClaimOrRenounceGoal(it.goalTileType, it.tier) &&
                currentPlayer.isLocal &&
                !currentPlayer.renouncedGoals.contains(it)
            ) {
                // call claimOrRenounceGoal() in the gui layer
                onAllRefreshables { refreshAfterPlayTile(it.goalTileType, it.tier) }
            }
        }
        // do other stuff in gui
        onAllRefreshables { refreshAfterPlayTile(null, 0) }
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
     * @param tilePosition The position of the bonsai tile to be removed from.
     * @throws IllegalStateException if there is no tile (bonsai tree is empty).
     */
    fun removeFromTree(tilePosition: Pair<Int, Int>) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        gameState.currentState = States.DISCARDING

        val currentPlayer = getCurrentPlayer()

        if (canPlayWood()) {
            throw IllegalArgumentException("No need to remove")
        }



        require(gameState.currentPlayer.bonsaiTree[tilePosition]?.tileType == TileType.LEAF){"not a valid move"}


        require(isMinimalAndCorrect(tilePosition))

            if(getNeighbourTiles(tilePosition).any{ it.second?.tileType == TileType.FRUIT })
            {
                getNeighbourTiles(tilePosition)
                    .filter { it.second?.tileType == TileType.FRUIT }
                    .forEach { currentPlayer.bonsaiTree.remove(it.first) }
                currentPlayer.bonsaiTree.remove(tilePosition)
                // TODO : check it another time !!!!!!
                  }

            else{
                currentPlayer.bonsaiTree.remove(tilePosition)
            }
                // Refresh GUI to reflect the updated tree
        onAllRefreshables { refreshAfterRemoveFromTree(tilePosition) }

        // update message
        val net = rootService.networkService
        // TODO(if it's allowed then...)
        if (net.connectionState != ConnectionState.DISCONNECTED &&
            currentPlayer.isLocal
        ) {
            net.toBeSentCultivateMessage.removedTilesAxialCoordinates.add(tilePosition)
        }
    }

    /**
     *
     */
    fun isMinimalAndCorrect(tilePosition: Pair<Int, Int>) : Boolean{
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }
        val tree = gameState.currentPlayer.bonsaiTree
        val leafTilesPositions = tree
            .filter { it.value.tileType == TileType.LEAF }.keys.toMutableList()
        println(leafTilesPositions)
        val removableLeafs1: MutableList<Pair<Int, Int>> = mutableListOf()
        val removableLeafs2: MutableList<Pair<Int, Int>> = mutableListOf()
        leafTilesPositions.forEach { position ->
            val neighbourTiles = getNeighbourTiles(position)
            println("neighbours $position : $neighbourTiles ")
            if(neighbourTiles.any { it.second == null }) {
                if (neighbourTiles.all {
                        it.second == null ||
                                it.second?.tileType == TileType.WOOD ||
                                it.second?.tileType == TileType.LEAF ||
                                (it.second?.tileType == TileType.FLOWER &&
                                        getNeighbourTiles(it.first)
                                            .filter { neighbor -> neighbor.first != position }
                                            .any { neighbor -> neighbor.second?.tileType == TileType.LEAF })
                    }) {
                    removableLeafs1.add(position)
                } else {
                    removableLeafs2.add(position)
                }
            }
        }
        println("first $removableLeafs1")
        println("second $removableLeafs2")
        if (removableLeafs1.isNotEmpty()) return removableLeafs1.contains(tilePosition)
        return removableLeafs2.contains(tilePosition)
    }

    /**
     *
     */
    private fun getNeighbourTiles(position : Pair<Int, Int>) : List<Pair<Pair<Int, Int>, Tile?>>{
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }
        val tree = gameState.currentPlayer.bonsaiTree
        val q = position.first
        val r = position.second
        return listOf(
            Pair(Pair(q + 1, r), tree.getOrDefault(Pair(q + 1, r), null)),
            Pair(Pair(q, r + 1), tree.getOrDefault(Pair(q, r + 1), null)),
            Pair(Pair(q - 1, r + 1), tree.getOrDefault(Pair(q - 1, r + 1), null)),
            Pair(Pair(q - 1, r), tree.getOrDefault(Pair(q - 1, r), null)),
            Pair(Pair(q, r - 1), tree.getOrDefault(Pair(q, r - 1), null)),
            Pair(Pair(q + 1, r - 1), tree.getOrDefault(Pair(q + 1, r - 1), null))
        )
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
        require(currentPlayer.personalSupply.contains(tile)) { "Player does not have this bonsai tile in hand" }

        return currentPlayer.playableTilesCopy.contains(tile.tileType)
                || currentPlayer.playableTilesCopy.contains(TileType.ANY)
    }

    /**
     * check if a [WOOD] can be placed in the tree .
     */
    fun canPlayWood(): Boolean {
        val tree = getCurrentPlayer().bonsaiTree
        tree.filter { it.value.tileType == TileType.WOOD }
            .forEach { (position, tile) ->
                val q = position.first
                val r = position.second
                println("Tile at ($q, $r) -> Type: ${tile.tileType}")

                val neighbourTiles = listOf(
                    Pair(Pair(q + 1, r), tree.getOrDefault(Pair(q + 1, r), null)),
                    Pair(Pair(q, r + 1), tree.getOrDefault(Pair(q, r + 1), null)),
                    Pair(Pair(q - 1, r + 1), tree.getOrDefault(Pair(q - 1, r + 1), null)),
                    Pair(Pair(q - 1, r), tree.getOrDefault(Pair(q - 1, r), null)),
                    Pair(Pair(q, r - 1), tree.getOrDefault(Pair(q, r - 1), null)),
                    Pair(Pair(q + 1, r - 1), tree.getOrDefault(Pair(q + 1, r - 1), null))
                ).filter { it.first !in POT }
                val nullPositions = neighbourTiles.filter { it.second == null }.map { it.first }

                println("Null:$nullPositions")
                if (nullPositions.isNotEmpty()) {
                    return true
                }
            }
        return false
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
        if (!canPlayTile(tile)) return false

        val currentPlayer = getCurrentPlayer()

        // Position of pot
        //  TODO: you can import the POT from util
        val forbiddenPositions = setOf(
            Pair(-2, 0), Pair(-1, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0),
            Pair(-2, 1), Pair(-1, 1), Pair(0, 1), Pair(1, 1), Pair(2, 1)
        )

        require(
            !currentPlayer.bonsaiTree.containsKey(tilePosition)
                    && tilePosition !in forbiddenPositions
        ) { "Position is already occupied or is pot" }

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

        require(neighbourTiles.isNotEmpty()) { "There are no adjacent cards" }

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
