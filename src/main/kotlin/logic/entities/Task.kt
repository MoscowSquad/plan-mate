package logic.entities

import java.util.*

data class Task(
    val id: UUID,
    val title: String,
    val description: String,
    val state: State
)
