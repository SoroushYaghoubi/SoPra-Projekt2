package service

import entity.*

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
    fun meditate(cardPosition: Int, chosenTile: Tile?) {


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

        // Constructor of Tile needs re-discussion
        when (cardPosition) {

            1 -> {
                checkNotNull(chosenTile)
                actPlayer.personalSupply.add(chosenTile)
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
                gameState.currentState = States.END_TURN
                return
            }

            is HelperCard -> {
                gameState.currentState = States.USING_HELPER
                playHelperCard(drawnCard)
                gameState.currentPlayer.hasPlayed = true
                gameState.currentState = States.END_TURN
                return
            }
        }


        // Check personal supply limit
        if (actPlayer.personalSupply.size > actPlayer.tileCapacity) {
            gameState.currentState = States.DISCARDING
            onAllRefreshables { refreshAfterChoseOrReceivedTile(true) }
            return
        }


        actPlayer.hasPlayed = true
        gameState.currentState = States.END_TURN

    }

    // NOTES: Maybe GUI does instead
//    private fun chooseTile(chosenTile: TileType): TileType {
//       getCurrentPlayer().
//    }

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
            onAllRefreshables { refreshAfterChoseOrReceivedTile(true) }
            return
        }
        actPlayer.hasPlayed = true
        gameState.currentState = States.END_TURN
        onAllRefreshables { refreshAfterChoseOrReceivedTile(false) }

    }

    /**
     * chose ANY Type
     * @param tileType : the TileType that the player has chosen
     */
    fun choseTile(tileType: TileType): Tile {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val actPlayer = gameState.currentPlayer

        require(
            gameState.currentState == States.USING_MASTER ||
                    gameState.currentState == States.USING_HELPER
        ) { "currentState should be Using_Master" }

        if (gameState.currentState == States.USING_MASTER) {
            actPlayer.personalSupply.add(Tile(null, null, tileType))

            // Check personal supply limit
            if (actPlayer.personalSupply.size > actPlayer.tileCapacity) {
                gameState.currentState = States.DISCARDING
                onAllRefreshables { refreshAfterChoseOrReceivedTile(true) }
                return Tile(null, null, tileType)
            }

            actPlayer.hasPlayed = true
            gameState.currentState = States.END_TURN
            onAllRefreshables { refreshAfterChoseOrReceivedTile(false) }
            return Tile(null, null, tileType)
        } else {
            return Tile(null, null, tileType)
        }


    }

    /**
     *
     */
    private fun playHelperCard(drawnCard: HelperCard) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val tileTypeToPlay2 = drawnCard.tileTypes[1]

        onAllRefreshables { refreshAfterDrawingHelperCard(TileType.ANY, tileTypeToPlay2) }

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

        // TODO: Update history -> later

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
    fun claimOrRenounceGoal(claim: Boolean) {
        val game = checkNotNull(rootService.currentGame) { "No game was started." }

        val gameState = checkNotNull(game.currentBonsaiGameState)

        val player = gameState.currentPlayer
        val playersBonsaiTree = player.bonsaiTree

        // checks if a player has already claimed a goal tile from a specific tile type
        for (goalTile in gameState.goalTiles.flatten()){
            if (player.claimedGoals.any{it.goalTileType == goalTile.goalTileType}) {
                continue
            }

            val conditionValid = when (goalTile.goalTileType) {
                GoalTileType.BROWN -> playersBonsaiTree.values.count {it.tileType == TileType.WOOD} >= goalTile.tier
                GoalTileType.GREEN -> playersBonsaiTree.values.count{it.tileType == TileType.LEAF} >= goalTile.tier
                GoalTileType.PINK -> playersBonsaiTree.values.count{it.tileType == TileType.FLOWER} >= goalTile.tier
                GoalTileType.ORANGE -> playersBonsaiTree.values.count { it.tileType == TileType.FRUIT } >= goalTile.tier
                GoalTileType.BLUE -> hasReachedBlueGoal(playersBonsaiTree, goalTile.tier)
            }

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
                }
            }
        }

    }

    /**
     * checks if a player has reached on of the blue goal tiles.
     *
     * @param bonsaiTree is the bonsai tree of the active player
     * @param tier the tier of the blue goal tile
     *
     * return true if the [tier] of the blue goal tile is reached, otherwise false
     */
    private fun hasReachedBlueGoal(bonsaiTree : MutableMap<Pair<Int, Int>, Tile>, tier : Int) : Boolean{
        val leftProtrude = bonsaiTree.keys.any { it.first  <= -3}
        val rightProtrude = bonsaiTree.keys.any { it.first  >= 4}
        val bellowProtrude = bonsaiTree.keys.any { it.second >= 3 }

        return when(tier) {
            7 -> leftProtrude || rightProtrude
            10 -> leftProtrude && rightProtrude
            14 -> (leftProtrude || rightProtrude) && bellowProtrude
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
    fun discardSupplyTile(tilesToDiscard: MutableList<Tile>) {}

    private fun getCurrentPlayer(): Player {
        return checkNotNull(rootService.currentGame?.currentBonsaiGameState?.currentPlayer)
    }
}

