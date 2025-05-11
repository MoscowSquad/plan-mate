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
        if (currentUserRole != UserRole.ADMIN) {
            write("\n❌ Error: Only ADMIN users can create new accounts.")
            return
        }

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

        val success = createUserUseCase(currentUserRole, newUser)
        if (success) {
            write("✅ User '$username' created successfully.")
        } else {
            write("❌ Failed to create user. Username might already exist.")
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
