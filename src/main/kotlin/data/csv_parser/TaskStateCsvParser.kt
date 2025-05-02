package data.csv_parser

import logic.models.TaskState

class TaskStateCsvParser : CsvParser<TaskState> {
    override fun parse(data: List<CsvData>): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun serialize(data: List<TaskState>): List<String> {
        TODO("Not yet implemented")
    }
}