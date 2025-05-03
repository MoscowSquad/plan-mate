package logic.models

import java.util.*

data class Task(
    val id: UUID,
    val name: String,
    val description: String,
    val projectId: UUID,
    val stateId: UUID,
)
