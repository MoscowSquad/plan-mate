package data.mongodb_data.util

import data.session_manager.SessionManager
import data.util.AdminOnlyException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import logic.models.User

inline fun <T> executeInIO(crossinline block: suspend () -> T): T {
    return runBlocking(Dispatchers.IO) {
        async {
            block()
        }.await()
    }
}

inline fun <T> executeInIOAdminOnly(crossinline block: suspend () -> T): T {
    return runBlocking(Dispatchers.IO) {
        async {
            if (SessionManager.currentUser == null || SessionManager.currentUser?.role == User.UserRole.ADMIN) {
                throw AdminOnlyException()
            }
            block()
        }.await()
    }
}