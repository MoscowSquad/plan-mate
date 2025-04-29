package logic.models

import java.util.*

data class Project(
    val id: UUID,
    val name: String,
    val states: MutableList<State>,
    val tasks: MutableList<Task>,
)
