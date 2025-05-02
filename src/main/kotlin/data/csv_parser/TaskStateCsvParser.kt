package data.csv_parser

import logic.models.TaskState
import utilities.TaskStateIndex

class TaskStateCsvParser : CsvParser<TaskState> {
    override fun parse(data: List<CsvData>): List<TaskState> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            TaskState(
                id = it[TaskStateIndex.ID].toUUID(),
                title = it[TaskStateIndex.TITLE],
                projectId = it[TaskStateIndex.PROJECT_ID].toUUID(),
            )
        }
    }

    override fun serialize(data: List<TaskState>): List<String> {
        return listOf("id,title,projectId") + data.map { datum ->
            "${datum.id},${datum.title},${datum.projectId}"
        }
    }
}