package logic.models

import java.util.*

data class Task(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = "",
    val projectId: UUID = UUID.randomUUID(),
    val stateId: Int? = null,
    val state: String? = null,
)
