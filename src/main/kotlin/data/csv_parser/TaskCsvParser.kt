package data.csv_parser

import logic.models.Task

class TaskCsvParser : CsvParser<Task> {
    override fun parse(data: List<CsvData>): List<Task> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<Task>): List<String> {
        TODO("Not yet implemented")
    }
}