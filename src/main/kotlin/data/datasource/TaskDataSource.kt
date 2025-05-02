package data.datasource

import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import logic.models.Task

class TaskDataSource(
    private val csvParser: TaskCsvParser,
) : DataSource<Task> {
    override fun fetch(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<Task>) {
        TODO("Not yet implemented")
    }
}