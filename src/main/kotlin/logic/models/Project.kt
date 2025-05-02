package logic.models

import java.util.*

data class Project(
    val id: UUID,
    val name: String,
    val userIds: List<UUID>
)
