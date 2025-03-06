package entity

/**
 * Game instance that manages the [BonsaiGameState] of a game and the saved states of the game
 * that are saved in [History]
 *
 * preconditions
 *-
 *
 * post conditions
 * - game and history are initialized
 *
 * @property history Stores [History] of game actions. `null` at start
 * a [History] needs to be recorded at game start
 */

class BonsaiGame() {
    val history: History? = null
}
