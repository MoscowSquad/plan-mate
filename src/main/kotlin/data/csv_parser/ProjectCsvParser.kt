package data.csv_parser

import logic.models.Project
import utilities.ProjectIndex
import java.util.*

class ProjectCsvParser : CsvParser<Project> {
    override fun parse(data: List<CsvData>): List<Project> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            Project(
                id = it[ProjectIndex.ID].toUUID(),
                name = it[ProjectIndex.NAME],
                userIds = it[ProjectIndex.USER_IDS].toUserIds()
            )
        }
    }

    override fun serialize(data: List<Project>): List<String> {
        return listOf("id,name,userIds") +
                data.map { datum ->
                    "${datum.id},${datum.name},${datum.userIds}"
                }
    }

    private fun String.toUserIds(): List<UUID> {
        return this.removeSurrounding("[", "]")
            .split(",")
            .takeIf { it.isNotEmpty() }
            ?.filter { it.isNotBlank() }
            ?.map { UUID.fromString(it.trim()) }
            ?: return emptyList()
    }
}
