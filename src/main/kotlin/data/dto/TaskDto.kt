package data.dto

data class TaskDto(
    val id: String,
    val name: String,
    val description: String,
    val projectId: String,
    val stateId: String,
)
