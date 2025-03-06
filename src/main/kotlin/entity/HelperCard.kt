package entity

/**
 * A helper card in the game, which lets a player play tiles at acquisition
 *
 * preconditions
 * - card ID must be a non-negative int
 *
 * post conditions
 * - helper card is created with a specific tile type.
 * - always associated with [TileType.ANY] and one other [TileType]
 *
 * @property tileTypes A list containing `TileType.ANY` and one other [TileType]
 *
 * @throws IllegalArgumentException If [id] is not a non-negative int
 */

class HelperCard(tileType: TileType, id : Int) : Card(id, CardType.HELPERCARD) {
    val tileTypes = mutableListOf(TileType.ANY, tileType)
}
