package logic.models

import java.util.*

data class Project(
    val projectId: UUID,
    val name: String,
    val states: MutableList<State>,
    val tasks: MutableList<Task>,
)
