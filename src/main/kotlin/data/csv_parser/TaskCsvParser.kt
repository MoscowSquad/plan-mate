package data.csv_parser

import logic.models.Task
import utilities.TaskIndex

class TaskCsvParser : CsvParser<Task> {
    override fun parse(data: List<CsvData>): List<Task> {
        return data.drop(1).map { line ->
            val it = line.split(",")
            Task(
                id = it[TaskIndex.ID].toUUID(),
                title = it[TaskIndex.TITLE],
                description = it[TaskIndex.DESCRIPTION],
                projectId = it[TaskIndex.PROJECT_ID].toUUID(),
                stateId = it[TaskIndex.STATE_ID].toUUID(),
            )
        }
    }

    override fun serialize(data: List<Task>): List<String> {
        return listOf("id,title,description,projectId,stateId") + data.map { datum ->
            "${datum.id},${datum.title},${datum.description},${datum.projectId},${datum.stateId}"
        }
    }
}