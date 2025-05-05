package data.csv_data.csv_parser

import data.csv_data.dto.TaskStateDto
import data.csv_data.util.TaskStateIndex

class TaskStateCsvParser : CsvParser<TaskStateDto> {
    override fun parse(data: List<CsvData>): List<TaskStateDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            TaskStateDto(
                id = it[TaskStateIndex.ID],
                name = it[TaskStateIndex.TITLE],
                projectId = it[TaskStateIndex.PROJECT_ID],
            )
        }
    }

    override fun serialize(data: List<TaskStateDto>): List<String> {
        return listOf("id,title,projectId") + data.map { datum ->
            "${datum.id},${datum.name},${datum.projectId}"
        }
    }
}