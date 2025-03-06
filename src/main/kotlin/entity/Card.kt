package entity


/**
 * general card in the game
 *
 * preconditions
 * - the card ID must be a non-negative int
 *
 * post conditions
 * - a card is created with a unique ID and a related [CardType]
 *
 * @property id Unique identifier of the card. Must be a non-negative int
 * @property cardType Type of the card, of [CardType]
 *
 * @throws IllegalArgumentException If [id] is negative or not an int
 */

abstract class Card(val id : Int, val cardType: CardType)
