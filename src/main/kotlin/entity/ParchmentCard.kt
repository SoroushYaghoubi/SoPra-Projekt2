package entity

class ParchmentCard(val parchmentTileType: TileType?,
                    val parchmentCardType: CardType?,
                    val basePoints : Int,
                    id : Int) : Card(id, CardType.PARCHMENTCARD) {
}
