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

            write("\n=== Create New User ===")

            write("Enter username:")
            val username = read()

            write("Enter password:")
            val password = read()

            write("Select role (ADMIN or MATE):")
            val roleInput = read()
            val newUserRole = try {
                UserRole.valueOf(roleInput)
            } catch (e: IllegalArgumentException) {
                write("Invalid role. Please enter 'ADMIN' or 'MATE'.")
                return
            }

        val newUser = User(
            id = UUID.randomUUID(),
            role = newUserRole,
            name = username,
            hashedPassword = password.toMD5Hash(),
            projectIds = listOf()
        )

            val success = createUserUseCase(currentUserRole, newUser)

            if (success) {
                write("User '$username' created successfully.")
            } else {
                write("Failed to create user. Username might already exist.")
            }


    }

}