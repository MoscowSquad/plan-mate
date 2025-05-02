import logic.models.User
import logic.models.UserRole
import logic.usecases.user.CreateUserUseCase
import presentation.io.ConsoleIO
import utilities.toMD5Hash
import utilities.isValidPasswordFormat

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

        val hashedPassword = password.toMD5Hash()
        val newUser = User(username, hashedPassword, newUserRole)

            val success = createUserUseCase(currentUserRole, newUser)

            if (success) {
                write("User '$username' created successfully.")
            } else {
                write("Failed to create user. Username might already exist.")
            }


    }

}