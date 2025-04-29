package utilities.csv_parser

import logic.models.Task

class TaskCsvParser : CsvParser<Task> {
    override fun parse(): List<Task> {
        return emptyList()
    }

    override fun serialize(data: List<Task>): CsvData {
        return ""
    }
}