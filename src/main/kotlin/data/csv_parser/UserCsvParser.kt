package data.csv_parser

import data.dto.UserDto
import data.util.UserIndex

class UserCsvParser : CsvParser<UserDto> {
    override fun parse(data: List<CsvData>): List<UserDto> {
        return data.drop(1).map { line ->
            val it = line.split(",", limit = 5)
            UserDto(
                id = it[UserIndex.ID],
                name = it[UserIndex.NAME],
                hashedPassword = it[UserIndex.HASHED_PASSWORD],
                role = it[UserIndex.ROLE],
                projectIds = it[UserIndex.PROJECT_IDS].toProjectIds(),
            )
        }
    }

    private fun String.toProjectIds(): List<String> {
        return this.removeSurrounding("[", "]")
            .split(",")
            .takeIf { it.isNotEmpty() }
            ?.filter { it.isNotBlank() }
            ?.map { it.trim() }
            ?: return emptyList()
    }

    override fun serialize(data: List<UserDto>): List<String> {
        return listOf("id,name,hashedPassword,role,projectIds") + data.map { datum ->
            "${datum.id},${datum.name},${datum.hashedPassword},${datum.role},${datum.projectIds}"
        }
    }
}