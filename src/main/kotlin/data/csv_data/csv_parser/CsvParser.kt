package data.csv_data.csv_parser

typealias CsvData = String

interface CsvParser<T> {
    fun parse(data: List<CsvData>): List<T>
    fun serialize(data: List<T>): List<CsvData>
}