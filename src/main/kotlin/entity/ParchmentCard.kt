package entity

class ParchmentCard(val nullableTileType: TileType?, val nullableCardType: CardType?,
                    id : Int, basePoints : Int) : Card(id, CardType.PARCHMENTCARD) {
}
