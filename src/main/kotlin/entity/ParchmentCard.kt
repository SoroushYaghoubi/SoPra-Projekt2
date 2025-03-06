package entity

/**
 * Represents a ParchmentCard in the game.
 * extends the `Card` class and is categorized under `CardType.PARCHMENTCARD`
 *
 * @param parchmentTileType is the tile type shown on parchment card
 * @param parchmentCardType is the other card type shown on parchment card
 * @param basePoints is the point parchment card gives
 * @param id is ID for the card
 */

class ParchmentCard(val parchmentTileType: TileType?,
                    val parchmentCardType: CardType?,
                    val basePoints : Int,
                    id : Int) : Card(id, CardType.PARCHMENTCARD) {
}
