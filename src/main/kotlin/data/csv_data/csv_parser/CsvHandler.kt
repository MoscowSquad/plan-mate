package data.csv_data.csv_parser

import java.io.File

class CsvHandler(private val file: File) {
    fun getLines(): List<String> {
        if (file.exists().not())
            file.createNewFile()
        return file.readLines().filter { it.isNotBlank() }
    }

    fun write(lines: List<String>) {
        if (file.exists().not())
            file.createNewFile()

        val str = StringBuilder()
        lines.forEach { line ->
            if (line.isBlank().not())
                str.append(line).append("\n")
        }

        file.writeText(str.toString())
    }
}