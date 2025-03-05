package entity

class MasterCard(val tileTypes : MutableList<TileType>, id : Int) : Card(id, CardType.MASTERCARD){
}
