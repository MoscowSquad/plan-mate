package logic.models

import java.util.*

data class User(
    val id: String,
    val name: String,
    val hashedPassword: String,
    val role: UserRole,
    val projectIds: List<UUID>
)