package logic.models

import java.util.*

data class Task(
    val id: UUID,
    val title: String,
    val description: String,
    val projectId: UUID,
    val stateId: UUID,
)
