package data.datasource

import data.csv_parser.CsvHandler
import data.csv_parser.TaskCsvParser
import data.dto.TaskDto

class TaskDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskCsvParser,
) : DataSource<TaskDto> {
    override fun fetch(): List<TaskDto> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<TaskDto>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}