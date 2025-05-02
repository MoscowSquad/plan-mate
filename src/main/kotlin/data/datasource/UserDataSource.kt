package data.datasource

import logic.models.User
import data.csv_parser.CsvHandler
import data.csv_parser.UserCsvParser

class UserDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: UserCsvParser,
) : DataSource<User> {
    override fun fetch(): List<User> {
        val lines = csvHandler.getLines()
        return csvParser.parse(lines)
    }

    override fun save(data: List<User>) {
        val serializedData = csvParser.serialize(data)
        csvHandler.write(serializedData)
    }
}