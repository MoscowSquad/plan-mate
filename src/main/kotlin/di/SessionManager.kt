package di

import logic.models.UserRole
import logic.util.UserNotLoggedInException
import java.util.*

data class LoggedInUser(
    val id: UUID,
    val name: String,
    val role: UserRole,
    val projectIds: List<UUID>
)

object SessionManager {
    var currentUser: LoggedInUser? = null
    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    fun setCurrentUser(role: UserRole) {
        currentUser = currentUser?.copy(
            role = role
        ) ?: throw UserNotLoggedInException("User is not logged in")
    }

    fun getCurrentUserRole(): UserRole = currentUser?.role
        ?: throw UserNotLoggedInException("User is not logged in")
}
