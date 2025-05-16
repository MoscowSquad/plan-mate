package domain.models

import java.util.*

data class TaskState(
    val id: UUID,
    val name: String,
    val projectId: UUID,
)