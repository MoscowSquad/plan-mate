package utilities.csv_parser

import logic.models.State

class StateCsvParser : CsvParser<State> {
    override fun parse(): List<State> {
        return emptyList()
    }

    override fun serialize(data: List<State>): CsvData {
        return ""
    }
}