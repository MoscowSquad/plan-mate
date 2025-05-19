package data.csv_data.csv_parser

import data.csv_data.dto.UserDto

private const val ID: Int = 0
private const val NAME: Int = 1
private const val HASHED_PASSWORD: Int = 2
private const val ROLE: Int = 3
private const val PROJECT_IDS: Int = 4
private const val Task_IDS: Int = 5

class UserCsvParser : CsvParser<UserDto> {
    override fun parse(data: List<CsvData>): List<UserDto> {
        return data.drop(1).map { line ->
            val parts = line.split(",", limit = 6)
            UserDto(
                id = parts[ID],
                name = parts[NAME],
                hashedPassword = parts[HASHED_PASSWORD],
                role = parts[ROLE],
                projectIds = if (parts.size > PROJECT_IDS) parts[PROJECT_IDS].toStringList() else emptyList(),
                taskIds = if (parts.size > Task_IDS) parts[Task_IDS].toStringList() else emptyList(),
            )
        }
    }

    private fun String.toStringList(): List<String> {
        if (this.isBlank() || this == "[]") {
            return emptyList()
        }

        return this.trim()
            .removePrefix("[")
            .removeSuffix("]")
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