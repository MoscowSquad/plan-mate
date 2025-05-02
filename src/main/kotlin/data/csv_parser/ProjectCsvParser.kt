package data.csv_parser

import logic.models.Project
import utilities.ProjectIndex

class ProjectCsvParser : CsvParser<Project> {
    override fun parse(data: List<CsvData>): List<Project> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            Project(
                id = it[ProjectIndex.ID].toUUID(),
                name = it[ProjectIndex.NAME],
            )
        }
    }


    override fun serialize(data: List<Project>): List<String> {
        return listOf("id,name") +
                data.map { datum ->
                    "${datum.id},${datum.name}"
                }
    }
}