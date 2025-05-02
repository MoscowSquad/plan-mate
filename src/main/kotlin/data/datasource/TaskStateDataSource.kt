package data.datasource

import logic.models.TaskState
import data.csv_parser.CsvHandler
import data.csv_parser.TaskStateCsvParser

class TaskStateDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskStateCsvParser,
) : DataSource<TaskState> {
    override fun fetch(): List<TaskState> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<TaskState>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}