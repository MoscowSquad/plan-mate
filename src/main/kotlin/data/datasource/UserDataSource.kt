package data.datasource

import data.dto.UserDto
import data.csv_parser.CsvHandler
import data.csv_parser.UserCsvParser

class UserDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: UserCsvParser,
) : DataSource<UserDto> {
    override fun fetch(): List<UserDto> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<UserDto>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}