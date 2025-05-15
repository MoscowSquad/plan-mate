package data.csv_data.csv_parser

import data.csv_data.dto.ProjectDto

private const val ID: Int = 0
private const val NAME: Int = 1

class ProjectCsvParser : CsvParser<ProjectDto> {
    override fun parse(data: List<CsvData>): List<ProjectDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            ProjectDto(
                id = it[ID],
                name = it[NAME],
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
