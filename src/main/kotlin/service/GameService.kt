package service

import edu.udo.cs.sopra.ntf.*

import edu.udo.cs.sopra.ntf.StartGameMessage
import entity.*
import util.ZenCardLoader
import kotlin.math.max

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
     * @param playerOrder List of ordered players that are playing this game.
     * @param playerOrder A list of Players deciding the player order.
     * @param networkGame true if game is played online otherwise false.
     */
    fun startNewGame( // playersEntries: MutableMap<String, PlayerType>,
        playerOrder: MutableList<Player>,
        networkGame: Boolean,
        goalTilesEntries: MutableList<GoalTileType>
    ) {
        // check required conditions
        check(rootService.currentGame == null) { "There is already a game running." }
        require(playerOrder.size >= 2) { "at least 2 Players" }

        // create the zenDeck depending on the number of players
        val zenDeck = ZenCardLoader().readAllZenCards(playerOrder.size).shuffled().toMutableList()

        // put the first 4 cards face up
        val faceUpCards = mutableListOf<Card>()
        repeat(4) {
            faceUpCards.add(zenDeck.removeAt(0))
        }

        val gameState = BonsaiGameState(
            currentPlayer = playerOrder.first(),
            players = playerOrder,
            1,
            currentState = States.START_TURN
        )

        // create the list of goalTiles
        gameState.goalTiles = createGoalTiles(goalTilesEntries, playerOrder.size)
        // assign the created zenDeck to the zenDeck of the game
        gameState.zenDeck = zenDeck
        // assign the face-up cards list to the game face-up cards
        gameState.faceUpCards = faceUpCards

        // give each player the post Tiles depending on the order
        gameState.players.forEachIndexed { index, player ->
            val tiles = mutableListOf<TileType>()

            if (index == 0) {
                tiles.add(TileType.WOOD)
            } else {
                tiles.add(TileType.WOOD)
                tiles.add(TileType.LEAF)

                if (index >= 2) tiles.add(TileType.FLOWER)
                if (index >= 3) tiles.add(TileType.FRUIT)
            }
            player.personalSupply.addAll(tiles.map { Tile(null, null, it) })
        }

        // add the new game state to the history
        val setHistory = History().apply { gameStates.add(gameState) }

        val game = BonsaiGame().apply {
            history = setHistory
            currentBonsaiGameState = gameState
        }


        rootService.currentGame = game

        if (networkGame) {

            val message = StartGameMessage(
                orderedPlayerNames = playerOrder.map { player ->
                    Pair(
                        player.name,
                        ColorTypeMessage.valueOf(player.color.name)
                    )
                },
                chosenGoalTiles = goalTilesEntries.map { GoalTileTypeMessage.valueOf(it.name) },
                orderedCards = zenDeck.mapIndexed { index, card ->
                    Pair(CardTypeMessage.valueOf(card.cardType.name), index)
                }
            )
            rootService.networkService.sendStartGameMessage(message)
        }

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

        val gameState = game.currentBonsaiGameState
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
     * - a refresh with the player order was used, where the winning player is at index 0,
     * second place is at index 2 etc.
     *
     * @throws IllegalStateException if game isn't over yet.
     */
    fun showWinner(): String {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        // Get the winner's name using the index
        return gameState.players[getWinnerIndex()].name
    }

    // Dennis implemented showWinner with a return value to test it
    /*
    fun showWinner(): String {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }

        val gameState = game.bonsaiGameState.lastOrNull()
        checkNotNull(gameState) { "No active game state." }

        // Get the winner's name using the index and return it
        return gameState.players[getWinnerIndex()].name
    }
    */

    /**
     * Help function to get the index of the winner
     */
    private fun getWinnerIndex(): Int {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val maxScore = gameState.players.maxOf { it.score }

        //val playerOrder: MutableList<Player> = mutableListOf()
        val playerOrder = gameState.players

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
    fun calculateScore(): Int {
        val game = rootService.currentGame
        checkNotNull(game) { "No game was started." }
        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        val actPlayer = gameState.currentPlayer
        val playersBonsaiTree = actPlayer.bonsaiTree
        val playersCollectedCards = actPlayer.collectedCards
        val claimGoals = actPlayer.claimedGoals

        // Tile
        val leaf = countTilesType(playersBonsaiTree, TileType.LEAF)
        val flower = countTilesType(playersBonsaiTree, TileType.FLOWER)
        val fruit = countTilesType(playersBonsaiTree, TileType.FRUIT)
        val wood = countTilesType(playersBonsaiTree, TileType.WOOD)

        val scoreOfLeaf = leaf * 3
        val scoreOfFlower = calculateFlowerPoints(playersBonsaiTree)
        val scoreOfFruit = fruit * 7

        val scoreTiles = scoreOfLeaf + scoreOfFlower + scoreOfFruit

        // Parchment
        val masterCard = 2 * countZencardType(playersCollectedCards, CardType.MASTERCARD)
        val growthCard = 2 * countZencardType(playersCollectedCards, CardType.GROWTHCARD)
        val helperCard = 2 * countZencardType(playersCollectedCards, CardType.HELPERCARD)
        val flowerTile = 2 * flower
        val leafTile = 2 * leaf
        val woodTile = 2 * wood

        val scoreParchment = masterCard + growthCard + helperCard + flowerTile + leafTile + woodTile

        // Goal
        val scoreOfGoal = claimGoals.sumOf { it.score }

        return scoreTiles + scoreParchment + scoreOfGoal
    }


    private fun countTilesType(bonsaiTree: MutableMap<Pair<Int, Int>, Tile>, type: TileType): Int {
        return bonsaiTree.values.count { it.tileType == type }
    }

    private fun countZencardType(collectedZenCard: MutableList<Card>, type: CardType): Int {
        return collectedZenCard.count { it.cardType == type }
    }

    private fun calculateFlowerPoints(bonsaiTree: Map<Pair<Int, Int>, Tile>): Int {
        var totalPoints = 0

        for ((position, tile) in bonsaiTree) {
            if (tile.tileType == TileType.FLOWER) {
                val (q, r) = position

                // Define neighbor positions
                val neighbors = listOf(
                    Pair(q + 1, r),
                    Pair(q, r + 1),
                    Pair(q - 1, r + 1),
                    Pair(q - 1, r),
                    Pair(q, r - 1),
                    Pair(q + 1, r - 1)
                )

                // Count sides that are NOT touching other tiles
                val emptySides = neighbors.count { neighborPos -> !bonsaiTree.containsKey(neighborPos) }

                // Add points (1 point per empty side)
                totalPoints += emptySides
            }
        }

        return totalPoints
    }


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

        val gameState = game.currentBonsaiGameState
        checkNotNull(gameState) { "No active game state." }

        if (gameState.zenDeck.isEmpty()) {
            throw IllegalStateException("Zen deck is empty.")
        }

        if (gameState.faceUpCards.size < 4) {
            val newCard = gameState.zenDeck.removeAt(0)
            gameState.faceUpCards.add(0, newCard)
        }

        onAllRefreshables { refreshAfterChooseCard() }
    }

    /**
     *
     */
    private fun createGoalTiles(goalTilesTypesEntries: MutableList<GoalTileType>, playerSize: Int)
            : MutableList<MutableList<GoalTile>> {

        val goalTiles: MutableList<MutableList<GoalTile>> = mutableListOf()

        val brownGoalTiles = mutableListOf(
            GoalTile(GoalTileType.BROWN, 8, 5),
            GoalTile(GoalTileType.BROWN, 10, 10),
            GoalTile(GoalTileType.BROWN, 12, 15)
        )
        val greenGoalTiles = mutableListOf(
            GoalTile(GoalTileType.GREEN, 5, 6),
            GoalTile(GoalTileType.GREEN, 7, 9),
            GoalTile(GoalTileType.GREEN, 9, 12)
        )
        val pinkGoalTiles = mutableListOf(
            GoalTile(GoalTileType.PINK, 3, 8),
            GoalTile(GoalTileType.PINK, 4, 12),
            GoalTile(GoalTileType.PINK, 5, 16)
        )
        val orangeGoalTiles = mutableListOf(
            GoalTile(GoalTileType.ORANGE, 3, 9),
            GoalTile(GoalTileType.ORANGE, 4, 11),
            GoalTile(GoalTileType.ORANGE, 5, 13)
        )
        val blueGoalTiles = mutableListOf(
            GoalTile(GoalTileType.ORANGE, 0, 9),
            GoalTile(GoalTileType.ORANGE, 0, 11),
            GoalTile(GoalTileType.ORANGE, 0, 13)
        )

        goalTilesTypesEntries.forEach { goalTileType ->
            run {
                when (goalTileType) {
                    GoalTileType.BROWN -> goalTiles.add(brownGoalTiles)
                    GoalTileType.GREEN -> goalTiles.add(greenGoalTiles)
                    GoalTileType.PINK -> goalTiles.add(pinkGoalTiles)
                    GoalTileType.ORANGE -> goalTiles.add(orangeGoalTiles)
                    GoalTileType.BLUE -> goalTiles.add(blueGoalTiles)
                }
            }
        }

        if (playerSize == 2) {
            goalTiles.forEach { goalTile ->
                run {
                    goalTile.removeAt(1)
                }
            }
        }
        println("Goal Tiles$goalTiles")

        return goalTiles

    }
}
