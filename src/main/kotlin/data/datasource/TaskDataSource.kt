package data.datasource

import logic.models.Task
import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import data.csv_parser.UserCsvParser

class TaskDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskCsvParser,
) : DataSource<Task> {
    override fun fetch(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<Task>) {
        TODO("Not yet implemented")
    }
}