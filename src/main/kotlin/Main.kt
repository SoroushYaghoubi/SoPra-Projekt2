import util.CSVLoader
import util.readAllGrowthCards
import util.translateToolCardsFromCSV

fun main() {
    /**
    translateToolCardsFromCSV(4).forEach {
        println(it.id)
    }
    */

    val alist = readAllGrowthCards()
    val csvLoader = CSVLoader()
    val blist = csvLoader.readCsvFile<String>("zengrowth.csv")
    println(blist)
}
