package presentation.user

import logic.models.User
import logic.models.UserRole
import logic.usecases.user.CreateUserUseCase
import logic.util.toMD5Hash
import presentation.io.ConsoleIO
import java.util.UUID

class CreateUserUI(
    private val createUserUseCase: CreateUserUseCase,
    private val currentUserRole: UserRole,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        // Check permissions first
        if (currentUserRole != UserRole.ADMIN) {
            write("\nError: Only ADMIN users can create new accounts.")
            return
        }

        write("\n=== Create New User ===")

        // Get username
        write("Enter username:")
        val username = read().takeIf { it.isNotBlank() } ?: run {
            write("Username cannot be empty.")
            return
        }

        // Get password
        write("Enter password:")
        val password = read().takeIf { it.isNotBlank() } ?: run {
            write("Password cannot be empty.")
            return
        }

        // Get role with validation
        write("Select role (ADMIN or MATE):")
        val roleInput = read().uppercase()
        val newUserRole = try {
            UserRole.valueOf(roleInput)
        } catch (e: IllegalArgumentException) {
            write("Invalid role. Please enter either 'ADMIN' or 'MATE'.")
            return
        }

        // Create user object
        val newUser = User(
            id = UUID.randomUUID(),
            role = newUserRole,
            name = username,
            hashedPassword = password.toMD5Hash(),
            projectIds = listOf()
        )

        // Attempt creation
        val success = createUserUseCase(currentUserRole, newUser)

        when {
            success -> write("User '$username' created successfully.")
            else -> write("Failed to create user. Username might already exist.")
        }
    }
}