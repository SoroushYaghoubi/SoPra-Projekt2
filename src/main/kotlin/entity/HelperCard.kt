package entity

class HelperCard(tileType: TileType, id : Int) : Card(id, CardType.HELPERCARD) {
    val tileTypes = mutableListOf(TileType.ANY, tileType)
}
