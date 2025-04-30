package logic.usecases

import logic.models.Role
import logic.repositoies.AuthenticationRepository
import utilities.toMD5Hash

class AuthenticationUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun validateUsername(username: String): Boolean {
        require(username.isNotBlank()) { "Username cannot be blank" }

        // Validate username format
        if (username.length < 3 || username.length > 20) {
            return false
        }
        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return false
        }

        // Check if username exists (case-insensitive)
        return authenticationRepository.users.none {
            it.username.equals(username, ignoreCase = true)
        }
    }

    fun validateLogin(username: String, password: String): Role? {
        return authenticationRepository.users.find { user ->
            user.username.equals(username, ignoreCase = true) &&
                    user.hashedPassword == password.toMD5Hash()
        }?.role
    }
}