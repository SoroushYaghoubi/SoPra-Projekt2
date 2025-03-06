package entity

class ParchmentCard(val parchmentTileType: TileType?, val parchmentCardType: CardType?,
                    id : Int, val basePoints : Int) : Card(id, CardType.PARCHMENTCARD) {
}
