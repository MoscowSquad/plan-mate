package presentation.user

import di.SessionManager
import logic.models.User
import logic.models.UserRole
import logic.usecases.user.CreateUserUseCase
import logic.util.toMD5Hash
import presentation.io.ConsoleIO
import java.util.*

class CreateUserUI(
    private val createUserUseCase: CreateUserUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()
        write("\n=== 👤 Create New User ===")

        val username = promptForUsername()
        val password = promptForPassword()
        val role = promptForRole()

        val newUser = User(
            id = UUID.randomUUID(),
            role = role,
            name = username,
            hashedPassword = password.toMD5Hash(),
            projectIds = listOf()
        )

        val result = runCatching {
            createUserUseCase(currentUserRole, newUser)
        }

        result
            .onSuccess {
                write("✅ User '$username' created successfully.")
            }
            .onFailure {
                write("❌ Failed to create user. ${it.message}")
            }
    }

    private fun promptForUsername(): String {
        while (true) {
            write("Enter username:")
            val input = read().trim()
            if (input.isNotBlank()) return input
            write("❌ Username cannot be empty.")
        }
    }

    private fun promptForPassword(): String {
        while (true) {
            write("Enter password (at least 8 characters):")
            val input = read()
            if (input.length >= 8) return input
            write("❌ Password must be at least 8 characters long.")
        }
    }

    private fun promptForRole(): UserRole {
        while (true) {
            write("Select role (ADMIN or MATE):")
            val input = read().trim().uppercase()
            try {
                return UserRole.valueOf(input)
            } catch (e: IllegalArgumentException) {
                write("❌ Invalid role. Please enter either 'ADMIN' or 'MATE'.")
            }
        }
    }
}
