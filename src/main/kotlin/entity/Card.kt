package entity

import kotlinx.serialization.Serializable

/**
 * general card in the game
 *
 * @property id Unique identifier of the card. Must be a non-negative int
 * @property cardType Type of the card, of [CardType]
 *
 * @throws IllegalArgumentException If [id] is negative or not an int
 */
@Serializable
abstract class Card(val id: Int, val cardType: CardType)


