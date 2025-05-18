package presentation.user

import data.session_manager.SessionManager
import domain.models.User
import domain.models.User.UserRole
import domain.usecases.user.CreateUserUseCase
import domain.util.isValidPasswordFormat
import presentation.io.ConsoleIO
import java.util.*

class CreateUserUI(
    private val createUserUseCase: CreateUserUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()
        write("\n╔══════════════════════════╗")
        write("║      CREATE NEW USER     ║")
        write("╚══════════════════════════╝")

        val username = promptForUsername()
        val password = promptForPassword()
        val role = promptForRole()

        // Create the user first
        val newUser = User(
            id = UUID.randomUUID(),
            role = role,
            name = username,
            projectIds = emptyList(),
            taskIds = emptyList()
        )

        runCatching { createUserUseCase(currentUserRole, newUser, password) }
            .onSuccess { write("\n✅ User '$username' created successfully!") }
            .onFailure { write("❌ Failed to create user: ${it.message}") }
    }

    private fun promptForUsername(): String {
        while (true) {
            write("\nEnter username:")
            val input = read().trim()
            if (input.isNotBlank()) return input
            write("❌ Username cannot be empty")
        }
    }

    private fun promptForPassword(): String {
        while (true) {
            write(
                """
            At least one lowercase letter
            At least one uppercase letter
            At least one digit
            At least one special character
            Minimum length of 8 characters
            """.trimIndent()
            )
            val password = read().trim()
            if (isValidPasswordFormat(password)) return password
            write("❌ Password must meet the specified criteria")
        }
    }

    private fun promptForRole(): UserRole {
        while (true) {
            write("\nSelect role:")
            UserRole.entries.forEach { role ->
                write("${role.ordinal + 1}. $role")
            }
            write("Enter choice (1-${UserRole.entries.size}):")

            try {
                val input = read().trim().toInt()
                if (input in 1..UserRole.entries.size) {
                    return UserRole.entries[input - 1]
                }
                write("⚠️ Please enter a number between 1 and ${UserRole.entries.size}")
            } catch (e: NumberFormatException) {
                write("⚠️ Please enter a valid number")
            }
        }
    }
}