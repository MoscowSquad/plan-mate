package domain.models

import java.util.*

data class SubTask(
    val id: UUID,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val parentTaskId: UUID
)