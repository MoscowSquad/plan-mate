package data.datasource

import logic.models.State
import data.csv_parser.CsvHandler
import data.csv_parser.TaskStateCsvParser

class TaskStateDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskStateCsvParser,
) : DataSource<State> {
    override fun fetch(): List<State> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<State>) {
        TODO("Not yet implemented")
    }
}