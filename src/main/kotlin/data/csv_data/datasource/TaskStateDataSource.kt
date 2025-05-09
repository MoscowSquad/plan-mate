package data.csv_data.datasource

import data.csv_data.csv_parser.CsvHandler
import data.csv_data.csv_parser.TaskStateCsvParser
import data.csv_data.dto.TaskStateDto

class TaskStateDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: TaskStateCsvParser,
) : DataSource<TaskStateDto> {
    override fun fetch(): List<TaskStateDto> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<TaskStateDto>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}