package logic.models

import java.util.UUID

data class TaskState(
    val id: UUID,
    val title: String,
    val projectId: UUID,
)