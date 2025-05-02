package data.datasource

import logic.models.TaskState
import data.csv_parser.CsvHandler
import data.csv_parser.TaskStateCsvParser

class TaskStateDataSource(
    private val csvParser: TaskStateCsvParser,
) : DataSource<TaskState> {
    override fun fetch(): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<TaskState>) {
        TODO("Not yet implemented")
    }
}