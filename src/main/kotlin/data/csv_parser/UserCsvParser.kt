package data.csv_parser

import logic.models.User

class UserCsvParser : CsvParser<User> {
    override fun parse(data: List<CsvData>): List<User> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<User>): List<CsvData> {
        TODO("Not yet implemented")
    }
}