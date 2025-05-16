package data.mongodb_data.util

import data.session_manager.SessionManager
import data.util.AdminOnlyException
import domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> executeInIO(
    requireAdmin: Boolean = false,
    crossinline block: suspend () -> T
): T {
    return withContext(Dispatchers.IO) {
        if (requireAdmin) {
            val user = SessionManager.currentUser
            if (user == null || user.role != User.UserRole.ADMIN) {
                throw AdminOnlyException()
            }
        }
        block()
    }
}
