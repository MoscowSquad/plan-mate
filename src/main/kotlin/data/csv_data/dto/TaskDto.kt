package data.csv_data.dto

import domain.models.SubTask

data class TaskDto(
    val id: String,
    val name: String,
    val description: String,
    val projectId: String,
    val stateId: String,
    val subTasks: List<String>
)
