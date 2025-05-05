package presentation.user

import logic.models.User
import logic.models.UserRole
import logic.usecases.user.GetAllUsersUseCase
import presentation.io.ConsoleIO

class GetAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val currentUserRole: () -> UserRole,
    consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\n========================")
        write("║      ALL USERS        ║")
        write("========================")
        val role = currentUserRole()
        val users: List<User> = getAllUsersUseCase(role)

        if (users.isEmpty()) {
            write("ℹ️  No users found.")
            return
        }

        write("\nList of Users:\n")
        users.forEachIndexed { index, user ->
            write("${index + 1}. ID: ${user.id}")
            write("  Username: ${user.name}")
            write(" Role: ${user.role}")
            write("-------------------------------")
        }


    }
}
