package utilities.csv_parser

import logic.models.State

class StateCsvParser(
    private val csvReader: StateCsvReader,
    private val csvWriter: StateCsvWriter,
) : CsvParser<State> {
    override fun parse(): List<State> {
        return emptyList()
    }

    override fun serialize(data: List<State>): CsvData {
        return ""
    }
}