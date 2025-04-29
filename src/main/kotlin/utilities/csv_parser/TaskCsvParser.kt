package utilities.csv_parser

import logic.models.Task

class TaskCsvParser(
    private val csvReader: TaskCsvReader,
    private val csvWriter: TaskCsvWriter,
) : CsvParser<Task> {
    override fun parse(): List<Task> {
        return emptyList()
    }

    override fun serialize(data: List<Task>): CsvData {
        return ""
    }
}