package data.csv_parser

import data.dto.TaskDto
import data.util.TaskIndex

class TaskCsvParser : CsvParser<TaskDto> {
    override fun parse(data: List<CsvData>): List<TaskDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            TaskDto(
                id = it[TaskIndex.ID],
                name = it[TaskIndex.TITLE],
                description = it[TaskIndex.DESCRIPTION],
                projectId = it[TaskIndex.PROJECT_ID],
                stateId = it[TaskIndex.STATE_ID],
            )
        }
    }

    override fun serialize(data: List<TaskDto>): List<String> {
        return listOf("id,title,description,projectId,stateId") + data.map { datum ->
            "${datum.id},${datum.name},${datum.description},${datum.projectId},${datum.stateId}"
        }
    }
}