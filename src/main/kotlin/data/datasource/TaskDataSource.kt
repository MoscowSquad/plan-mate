package data.datasource

import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import logic.models.Task

class TaskDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskCsvParser,
) : DataSource<Task> {
    override fun fetch(): List<Task> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<Task>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}