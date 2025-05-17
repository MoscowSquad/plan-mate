package data.csv_data.csv_parser

import data.csv_data.dto.TaskDto

private const val ID: Int = 0
private const val TITLE: Int = 1
private const val DESCRIPTION: Int = 2
private const val PROJECT_ID: Int = 3
private const val STATE_ID: Int = 4

class TaskCsvParser : CsvParser<TaskDto> {
    override fun parse(data: List<CsvData>): List<TaskDto> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            TaskDto(
                id = it[ID],
                name = it[TITLE],
                description = it[DESCRIPTION],
                projectId = it[PROJECT_ID],
                stateId = it[STATE_ID],
                subTasks = emptyList() // Assuming subTasks are not included in the CSV
            )
        }
    }

    override fun serialize(data: List<TaskDto>): List<String> {
        return listOf("id,title,description,projectId,stateId") + data.map { datum ->
            "${datum.id},${datum.name},${datum.description},${datum.projectId},${datum.stateId}"
        }
    }
}