package presentation.session

import logic.models.UserRole
import java.util.UUID

data class LoggedInUser(
    val id: UUID,
    val name: String,
    val role: UserRole,
    val projectIds: List<UUID>
)

object SessionManager {
    var currentUser: LoggedInUser? = null
}
