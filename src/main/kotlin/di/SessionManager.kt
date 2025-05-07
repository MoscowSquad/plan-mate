package di

import logic.models.UserRole

class SessionManager {
    private var currentUserRole: UserRole? = null

    fun setCurrentUser(role: UserRole) {
        currentUserRole = role
    }

    fun getCurrentUserRole(): UserRole? = currentUserRole
}