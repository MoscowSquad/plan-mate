package data.csv_data.dto

data class SubTaskDto (
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: String,
    val parentTaskId: String
)