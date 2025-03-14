package service

import edu.udo.cs.sopra.ntf.MeditateMessage
import entity.*
import kotlin.math.max

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
     * @param chosenTile The wood or leaf tile if the card is drawn from position 1
     * @throws IllegalStateException if player has already done an action during his current turn.
     */
    fun meditate(cardPosition: Int, chosenTile: TileType?) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        gameState.currentState = States.MEDITATE
        val actPlayer = gameState.currentPlayer

        // Player draw 1 card among face up cards in the board
        val drawnCard = gameState.faceUpCards.removeAt(cardPosition)
        actPlayer.collectedCards.add(drawnCard)

        // Refill the board
        rootService.gameService.refillBoard()

        onAllRefreshables { refreshAfterChooseCard() }

        when (cardPosition) {

            1 -> {
                checkNotNull(chosenTile)
                require(chosenTile == TileType.WOOD || chosenTile == TileType.LEAF) { "Please choose WOOD or LEAF" }
                actPlayer.personalSupply.add(Tile(null, null, chosenTile))
            }

            2 -> {
                actPlayer.personalSupply.add(Tile(null, null, TileType.WOOD))
                actPlayer.personalSupply.add(Tile(null, null, TileType.FLOWER))
            }

            3 -> {
                actPlayer.personalSupply.add(Tile(null, null, TileType.LEAF))
                actPlayer.personalSupply.add(Tile(null, null, TileType.FRUIT))
            }
        }

        when (drawnCard) {

            is ToolCard -> {
                actPlayer.tileCapacity += 2
            }

            is GrowthCard -> {
                actPlayer.playableTiles.add(drawnCard.tileType)
            }

            is MasterCard -> {
                gameState.currentState = States.USING_MASTER
                playMasterCard(drawnCard)
                gameState.currentPlayer.hasPlayed = true
                return
            }

            is HelperCard -> {
                gameState.currentState = States.USING_HELPER
                playHelperCard(drawnCard)
                gameState.currentPlayer.hasPlayed = true
                return
            }
        }

        onAllRefreshables { refreshAfterApplyCardEffects() }
        // Check personal supply limit
        if (actPlayer.personalSupply.size > actPlayer.tileCapacity) {
            gameState.currentState = States.DISCARDING
            onAllRefreshables { refreshAfterReceivedTile(true) }
            return
        }

        onAllRefreshables { refreshAfterReceivedTile(true) }
        actPlayer.hasPlayed = true
    }

    /**
     * apply MasterCard effects
     * @param drawnCard : the drawn MasterCard
     */
    private fun playMasterCard(drawnCard: MasterCard) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }
        require(gameState.currentState == States.USING_MASTER)
        val actPlayer = gameState.currentPlayer

        when (drawnCard.tileTypes.size) {
            1 -> {
                //actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[0]))
                onAllRefreshables { refreshAfterDrawingMasterCardAny() }
                return
            }

            2 -> {
                actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[0]))
                actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[1]))
            }

            3 -> {
                actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[0]))
                actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[1]))
                actPlayer.personalSupply.add(Tile(null, null, drawnCard.tileTypes[2]))
            }
        }
        // Check personal supply limit
        if (actPlayer.personalSupply.size > actPlayer.tileCapacity) {
            gameState.currentState = States.DISCARDING
            onAllRefreshables { refreshAfterReceivedTile(true) }
            return
        }
        actPlayer.hasPlayed = true
        onAllRefreshables { refreshAfterReceivedTile(false) }

    }

    /**
     * chose ANY Type
     * @param tileType : the TileType that the player has chosen
     */
    fun choseTile(tileType: TileType) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val actPlayer = gameState.currentPlayer

        require(
            gameState.currentState == States.USING_MASTER
        ) { "CurrentState should be Using_Master" }

        actPlayer.personalSupply.add(Tile(null, null, tileType))

        // Check personal supply limit
        if (actPlayer.personalSupply.size > actPlayer.tileCapacity) {
            gameState.currentState = States.DISCARDING
            onAllRefreshables { refreshAfterReceivedTile(true) }
            return
        }

        actPlayer.hasPlayed = true
        onAllRefreshables { refreshAfterReceivedTile(false) }

    }

    /**
     * apply HelperCard effects
     * @param drawnCard : the drawn MasterCard
     */
    private fun playHelperCard(drawnCard: HelperCard) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val tileTypeToPlay2 = drawnCard.tileTypes[1]
        gameState.currentPlayer.playableTilesCopy = gameState.currentPlayer.playableTiles.toMutableList()
        onAllRefreshables { refreshAfterDrawingHelperCard(tileTypeToPlay2) }
        gameState.currentPlayer.hasPlayed = true

    }

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
    fun cultivate() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val currentPlayer = gameState.currentPlayer

        if (currentPlayer.hasPlayed) {
            throw IllegalStateException("Player has already meditated or cultivated.")
        }

        // create mutable list of the tiles that are playable this turn
        currentPlayer.playableTilesCopy = currentPlayer.playableTiles.toMutableList()

        // enable the player to click the end turn button at any time from now
        currentPlayer.hasPlayed = true

        // change game state to CULTIVATE
        gameState.currentState = States.CULTIVATE

        onAllRefreshables { refreshAfterCultivateStart() }

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

        val gameState = game.currentBonsaiGameState
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

        gameState.currentState = States.START_TURN
        onAllRefreshables { refreshAfterEndTurn() }

        // TODO: Update history -> later

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
     * TODO: this class claims or renounces two goals at the same time if possible. this might be bad
     *
     */
    fun claimOrRenounceGoal(claim: Boolean) {
        val game = checkNotNull(rootService.currentGame) { "No game was started." }

        val gameState = checkNotNull(game.currentBonsaiGameState)

        val player = getCurrentPlayer()
        val playersBonsaiTree = player.bonsaiTree

        // checks if a player has already claimed a goal tile from a specific tile type
        for (goalTile in gameState.goalTiles.flatten()) {
            if (player.claimedGoals.any { it.goalTileType == goalTile.goalTileType }) {
                continue
            }
            // checks if player has renounced the current goal tile
            if (goalTile in player.renouncedGoals) {
                continue
            }

            // checks if one of the goal tiles is reached
            val conditionValid = when (goalTile.goalTileType) {
                GoalTileType.BROWN -> hasReachedBrownGoal(playersBonsaiTree, goalTile.tier)
                GoalTileType.GREEN -> hasReachedGreenGoal(playersBonsaiTree, goalTile.tier)
                GoalTileType.PINK -> hasReachedPinkGoal(playersBonsaiTree, goalTile.tier)
                GoalTileType.ORANGE -> hasReachedOrangeGoal(playersBonsaiTree, goalTile.tier)
                GoalTileType.BLUE -> hasReachedBlueGoal(playersBonsaiTree, goalTile.tier)
            }

            // if goal tile requirement is reached then take actions based on claim
            if (conditionValid) {
                if (claim) {
                    player.claimedGoals.add(goalTile)
                    for (goalTileList in gameState.goalTiles) {
                        if (goalTileList.contains(goalTile)) {
                            goalTileList.remove(goalTile)
                            break
                        }
                    }
                } else {
                    player.renouncedGoals.add(goalTile)
                    break
                }
            }
        }
    }

    /**
     * count the amount of wood tiles in the players tree and compare it do the necessary amount for the tier.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the brown goal tile
     *
     * @return whether the goal tile can be claimed or not
     */
    private fun hasReachedBrownGoal(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, tier: Int): Boolean {
        val brownTiles = bonsaiTree.keys.count { bonsaiTree[it]?.tileType == TileType.WOOD }
        return when (tier) {
            0 -> brownTiles >= 8
            1 -> brownTiles >= 10
            2 -> brownTiles >= 12
            else -> false
        }
    }

    /**
     * count the amount of adjacent leaf tiles in the players tree and compare it do the necessary amount for the tier.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the green goal tile
     *
     * @return whether the goal tile can be claimed or not
     */
    private fun hasReachedGreenGoal(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, tier: Int): Boolean {
        // Directions for hexagonal grid adjacency
        val directions = listOf(
            Pair(1, 0), Pair(0, 1), Pair(-1, 1),
            Pair(-1, 0), Pair(0, -1), Pair(1, -1)
        )

        // Set to keep track of visited tiles so that there are no redundant calculations
        val visited = mutableSetOf<Pair<Int, Int>>()

        // Function to perform depth-first search and count the size of a cluster
        fun dfs(tilePos: Pair<Int, Int>): Int {
            //stack to keep track of all the tiles
            val stack = mutableListOf(tilePos)
            var count = 0

            while (stack.isNotEmpty()) {
                val (q, r) = stack.removeLast()
                // Skip already visited tiles
                if (!visited.add(Pair(q, r))) continue

                count++
                // find adjacent tiles
                for ((dq, dr) in directions) {
                    val neighbor = Pair(q + dq, r + dr)
                    if (neighbor in bonsaiTree && bonsaiTree[neighbor]?.tileType == TileType.LEAF && neighbor !in visited) {
                        stack.add(neighbor)
                    }
                }
            }
            return count
        }

        // Find the size of the largest cluster of LEAF tiles
        var maxLeafCluster = 0
        for ((pos, tile) in bonsaiTree) {
            if (tile.tileType == TileType.LEAF && pos !in visited) {
                maxLeafCluster = maxOf(maxLeafCluster, dfs(pos))
            }
        }

        // Check if the largest cluster meets the tier requirement
        return when (tier) {
            0 -> maxLeafCluster >= 5
            1 -> maxLeafCluster >= 7
            2 -> maxLeafCluster >= 9
            else -> false
        }
    }

    /**
     * count the amount of flower tiles in the players tree and compare it do the necessary amount for the tier.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the pink goal tile
     *
     * @return whether the goal tile can be claimed or not
     */
    private fun hasReachedPinkGoal(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, tier: Int): Boolean {
        val leftProtrude = bonsaiTree.keys.count { it.first <= -2 && bonsaiTree[it]?.tileType == TileType.FLOWER }
        val rightProtrude =
            bonsaiTree.keys.count {
                (it.first >= 4 || it.first == 3 && (it.second) % 2 == 0)
                        && bonsaiTree[it]?.tileType == TileType.FLOWER
            }

        val pinkProtruding = max(leftProtrude, rightProtrude)

        return when (tier) {
            0 -> pinkProtruding >= 3
            1 -> pinkProtruding >= 4
            2 -> pinkProtruding >= 5
            else -> false
        }
    }

    /**
     * count the amount of fruit tiles in the players tree and compare it do the necessary amount for the tier.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the orange goal tile
     *
     * @return whether the goal tile can be claimed or not
     */
    private fun hasReachedOrangeGoal(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, tier: Int): Boolean {
        val orangeTiles = bonsaiTree.keys.count { bonsaiTree[it]?.tileType == TileType.FRUIT }
        return when (tier) {
            0 -> orangeTiles >= 3
            1 -> orangeTiles >= 4
            2 -> orangeTiles >= 5
            else -> false
        }
    }

    /**
     * checks if a player has reached one of the blue goal tiles.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the blue goal tile
     *
     * return true if the [tier] of the blue goal tile is reached, otherwise false
     */
    private fun hasReachedBlueGoal(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, tier: Int): Boolean {

        val leftProtrude = bonsaiTree.keys.any { it.second <= -2 * (it.first + 2) }
        val rightProtrude = bonsaiTree.keys.any { it.second >= -2 * (it.first - 3) }

        val bellowLeftProtrude = bonsaiTree.keys.any { leftProtrude && it.second >= 2 }
        val bellowRightProtrude = bonsaiTree.keys.any { rightProtrude && it.second >= 2 }

        return when (tier) {
            0 -> rightProtrude
            1 -> leftProtrude && rightProtrude
            2 -> (leftProtrude && bellowRightProtrude) || (rightProtrude && bellowLeftProtrude)
            else -> false
        }
    }

    /**
     * Checks if player has played an action before ending his turn.
     *
     * preconditions:
     * - Player must play an action (cultivate or meditate).
     *
     *  @return true if player can end his turn.
     */
    fun canEndTurn(): Boolean {
        val player = getCurrentPlayer()
        return player.hasPlayed
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
    fun discardSupplyTile(tilesToDiscard: MutableList<Tile>) {
        val player = getCurrentPlayer()
        check(player.personalSupply.size > player.tileCapacity)
        { "The personal supply tiles hasn't reached the capacity." }
        player.personalSupply.removeAll(tilesToDiscard)
    }

    // returns the current player
    private fun getCurrentPlayer(): Player {
        return checkNotNull(rootService.currentGame?.currentBonsaiGameState?.currentPlayer)
    }
}
