package logic.models

import java.util.UUID

data class TaskState(
    val id: UUID,
    val name: String,
    val projectId: UUID,
)