package entity

/**
 * A growth card in the game, which lets a player play more tiles in the cultivate step
 *
 * preconditions
 * - card ID must be a non-negative int
 *
 * post conditions
 * - [GrowthCard] is created with a specified [TileType], but not ANY or EMPTY
 *
 * @property tileType [TileType] associated with this [GrowthCard]
 *
 * @throws IllegalArgumentException If [id] is not a non-negative int
 */

class GrowthCard(val tileType: TileType, id : Int) : Card(id, CardType.GROWTHCARD)
