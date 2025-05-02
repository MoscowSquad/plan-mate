package logic.models

import java.util.*

data class Task(
    val id: UUID?,
    var title: String?,
    var description: String="",
    val projectId: UUID,
    var stateId: UUID?,
)
