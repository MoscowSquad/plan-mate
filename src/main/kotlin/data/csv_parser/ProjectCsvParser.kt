package data.csv_parser

import data.dto.ProjectDto
import data.util.ProjectIndex

class ProjectCsvParser : CsvParser<ProjectDto> {
    override fun parse(data: List<CsvData>): List<ProjectDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            ProjectDto(
                id = it[ProjectIndex.ID],
                name = it[ProjectIndex.NAME],
            )
        }
    }

    override fun serialize(data: List<ProjectDto>): List<String> {
        return listOf("id,name") +
                data.map { datum ->
                    "${datum.id},${datum.name}"
                }
    }
}
