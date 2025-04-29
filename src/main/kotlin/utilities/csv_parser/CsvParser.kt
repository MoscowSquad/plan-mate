package utilities.csv_parser

typealias CsvData = String

interface CsvParser<T> {
    fun parse(): List<T>
    fun serialize(data: List<T>): CsvData
}