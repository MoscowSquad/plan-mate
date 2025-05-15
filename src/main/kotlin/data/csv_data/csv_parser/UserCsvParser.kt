package data.csv_data.csv_parser

import data.csv_data.dto.UserDto

private const val ID: Int = 0
private const val NAME: Int = 1
private const val HASHED_PASSWORD: Int = 2
private const val ROLE: Int = 3
private const val PROJECT_IDS: Int = 4

class UserCsvParser : CsvParser<UserDto> {
    override fun parse(data: List<CsvData>): List<UserDto> {
        return data.drop(1).map { line ->
            val it = line.split(",", limit = 5)
            UserDto(
                id = it[ID],
                name = it[NAME],
                hashedPassword = it[HASHED_PASSWORD],
                role = it[ROLE],
                projectIds = it[PROJECT_IDS].toProjectIds(),
            )
        }
    }

    private fun String.toProjectIds(): List<String> {
        if (this.isBlank() || this == "[]") {
            return emptyList()
        }

        return this.removeSurrounding("[", "]")
            .split(",")
            .filter { it.trim().isNotBlank() }
            .map { it.trim() }
    }

    override fun serialize(data: List<UserDto>): List<String> {
        return listOf("id,name,hashedPassword,role,projectIds") + data.map { datum ->
            "${datum.id},${datum.name},${datum.hashedPassword},${datum.role},${datum.projectIds}"
        }
    }
}