package data.csv_data.csv_parser

import data.csv_data.dto.TaskStateDto

private const val ID: Int = 0
private const val TITLE: Int = 1
private const val PROJECT_ID: Int = 2

class TaskStateCsvParser : CsvParser<TaskStateDto> {
    override fun parse(data: List<CsvData>): List<TaskStateDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            TaskStateDto(
                id = it[ID],
                name = it[TITLE],
                projectId = it[PROJECT_ID],
            )
        }
    }

    override fun serialize(data: List<TaskStateDto>): List<String> {
        return listOf("id,title,projectId") + data.map { datum ->
            "${datum.id},${datum.name},${datum.projectId}"
        }
    }
}