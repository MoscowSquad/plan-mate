package utilities.csv_parser

import logic.models.User

class UserCsvParser(
    private val csvReader: UserCsvReader,
    private val csvWriter: UserCsvWriter,
) : CsvParser<User> {
    override fun parse(): List<User> {
        return emptyList()
    }

    override fun serialize(data: List<User>): CsvData {
        return ""
    }
}