package data.csv_parser

import logic.models.User
import logic.models.UserRole
import utilities.UserIndex
import java.util.*

class UserCsvParser : CsvParser<User> {
    override fun parse(data: List<CsvData>): List<User> {
        return data.drop(1).map { line ->
            val it = line.split(",", limit = 5)
            User(
                id = it[UserIndex.ID].toUUID(),
                name = it[UserIndex.NAME],
                hashedPassword = it[UserIndex.HASHED_PASSWORD],
                role = it[UserIndex.ROLE].toRole(),
                projectIds = it[UserIndex.PROJECT_IDS].toUserIds(),
            )
        }
    }

    private fun String.toRole(): UserRole {
        return when (this) {
            UserRole.ADMIN.toString() -> UserRole.ADMIN
            UserRole.MATE.toString() -> UserRole.MATE
            else -> throw Exception("No user-role named $this")
        }
    }

    private fun String.toUserIds(): List<UUID> {
        return this.removeSurrounding("[", "]")
            .split(",")
            .takeIf { it.isNotEmpty() }
            ?.filter { it.isNotBlank() }
            ?.map {
                UUID.fromString(it.trim())
            }
            ?: return emptyList()
    }

    override fun serialize(data: List<User>): List<String> {
        return listOf("id,name,hashedPassword,role,projectIds") + data.map { datum ->
            "${datum.id},${datum.name},${datum.hashedPassword},${datum.role},${datum.projectIds}"
        }
    }
}