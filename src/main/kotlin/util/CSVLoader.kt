package util

import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule


/**
 * this class translate a given csv into a dataclass
 */
class CSVLoader {
    val csvMapper = CsvMapper().apply {
        registerModule(kotlinModule())
    }

    inline fun <reified T> readCsvFile(fileName: String): List<T> {
        val lines = object {}.javaClass.getResourceAsStream(fileName)?.bufferedReader()


        lines.use { reader ->
            return csvMapper
                .readerFor(T::class.java)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues<T>(reader)
                .readAll()
                .toList()
        }
    }
}
