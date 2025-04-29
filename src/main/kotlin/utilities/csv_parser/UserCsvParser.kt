package utilities.csv_parser

import logic.models.User

class UserCsvParser : CsvParser<User> {
    override fun parse(): List<User> {
        return emptyList()
    }

    override fun serialize(data: List<User>): CsvData {
        return ""
    }
}