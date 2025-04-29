package test_helper

import utilities.csv_parser.CsvData

fun List<String>.toCsvData(): CsvData {
    var data: CsvData = ""
    this.forEach {
        data += it + "\n"
    }
    return data
}