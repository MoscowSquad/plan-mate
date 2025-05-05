package presentation.session

import logic.models.UserRole
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
}

fun main() {
    println(SessionManager.isLoggedIn())
    SessionManager.currentUser = LoggedInUser(
        id = UUID.randomUUID(),
        name = "John Doe",
        role = UserRole.ADMIN,
        projectIds = listOf(UUID.randomUUID())
    )
    println(SessionManager.isLoggedIn())
}
