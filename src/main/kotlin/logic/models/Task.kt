package logic.models

import java.util.*

data class Task(
    val id: UUID=UUID.randomUUID(),
    val title: String,
    val description: String="",
    val projectId: UUID=UUID.randomUUID(),
    val stateId: Int,
    val state:String,
)
