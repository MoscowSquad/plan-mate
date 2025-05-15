package logic.models

import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val role: UserRole,
    val projectIds: List<UUID>,
    val taskIds: List<UUID>
) {
    enum class UserRole {
        ADMIN, MATE
    }
}