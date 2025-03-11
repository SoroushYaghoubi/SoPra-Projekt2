import gui.BonsaiApplication
import util.translateToolCardsFromCSV

fun main() {
    translateToolCardsFromCSV(4).forEach {
        println(it.id)
    }
    BonsaiApplication().show()
    println("Application ended. Goodbye")

}
