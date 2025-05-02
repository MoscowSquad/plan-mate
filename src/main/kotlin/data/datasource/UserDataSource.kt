package data.datasource

import logic.models.User
import data.csv_parser.CsvHandler
import data.csv_parser.UserCsvParser

class UserDataSource(
    private val csvHandler: CsvHandler,
    private val csvParser: UserCsvParser,
) : DataSource<User> {
    override fun fetch(): List<User> {
        TODO("Not yet implemented")
    }

    override fun save(data: List<User>) {
        TODO("Not yet implemented")
    }
}