package entity

/**
 * A growth card in the game, which lets a player play more tiles in the cultivate step
 *
 * @property tileType [TileType] associated with this [GrowthCard]
 *
 * @throws IllegalArgumentException If [id] is not a non-negative int
 */

class GrowthCard(val tileType: TileType, id: Int) : Card(id, CardType.GROWTHCARD)
