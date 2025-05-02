package data.csv_parser

import logic.models.State

class TaskStateCsvParser : CsvParser<State> {
    override fun parse(data: List<CsvData>): List<State> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<State>): List<String> {
        TODO("Not yet implemented")
    }
}