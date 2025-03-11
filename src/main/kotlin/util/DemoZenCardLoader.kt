package util

fun main() {
    val zenCardLoader = ZenCardLoader()
    val zenDeck = zenCardLoader.readAllZenCards(2)
    zenDeck.forEach{
        println(it.id)
        println(it.cardType)
    }
    println(zenDeck.size)
}
