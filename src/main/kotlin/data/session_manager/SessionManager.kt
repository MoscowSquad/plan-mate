package data.session_manager

import domain.models.User.UserRole
import domain.util.UserNotLoggedInException
import java.util.*

data class LoggedInUser(
    val id: UUID,
    val name: String,
    val role: UserRole,
    val projectIds: List<UUID>
)

object SessionManager {
    var currentUser: LoggedInUser? = null

    fun setCurrentUser(role: UserRole) {
        currentUser = currentUser?.copy(role = role) ?: throw UserNotLoggedInException()
    }

    fun getCurrentUserRole(): UserRole = currentUser?.role ?: throw UserNotLoggedInException()
}
