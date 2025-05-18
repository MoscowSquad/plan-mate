package data.mongodb_data.util

import data.session_manager.SessionManager
import domain.models.User
import domain.util.NotAdminException

fun ensureAdminPrivileges() {
    if (SessionManager.currentUser?.role != User.UserRole.ADMIN) {
        throw NotAdminException()
    }
}