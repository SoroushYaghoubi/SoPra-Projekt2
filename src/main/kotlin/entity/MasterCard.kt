package entity

/**
 * Represents a MasterCard in the game.
 * extends the [Card] class and is categorized under [CardType.MASTERCARD]
 *
 * @param tileTypes is a list of tile types that Master card grants when activated
 * @param id is ID for the card
 */
class MasterCard(val tileTypes : MutableList<TileType>, id : Int) : Card(id, CardType.MASTERCARD){
}
