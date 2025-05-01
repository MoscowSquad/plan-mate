package utilities.csv_parser

import logic.models.User

class UserCsvParser(
    private val csvHandler: CsvHandler,
) : CsvParser<User> {
    override fun parse(): List<User> {
        return emptyList()
    }

    override fun serialize(data: List<User>): CsvData {
        return ""
    }
}