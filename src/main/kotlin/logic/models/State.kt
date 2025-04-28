package logic.entities

import java.util.UUID

data class State(
    val id: UUID,
    val title: String,
    val projectId: UUID,
)