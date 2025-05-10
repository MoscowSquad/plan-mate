package presentation.user

import logic.models.User
import logic.models.UserRole
import logic.usecases.user.GetAllUsersUseCase
import presentation.io.ConsoleIO

class GetAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val currentUserRole: () -> UserRole,
    private val consoleIO: ConsoleIO
) {
    operator fun invoke() {
        printHeader()

        try {
            val users = getAllUsersUseCase(currentUserRole())

            if (users.isEmpty()) {
                consoleIO.write("\nℹ️  No users found in the system")
                return
            }

            printUserTable(users)

        } catch (e: Exception) {
            consoleIO.write("\n❌ Unexpected error: ${e.message}")
        }
    }

    private fun printHeader() {
        consoleIO.write("\n╔══════════════════════════════╗")
        consoleIO.write("║        USER DIRECTORY        ║")
        consoleIO.write("╚══════════════════════════════╝")
    }

    private fun printUserTable(users: List<User>) {
        consoleIO.write("\n┌────────────────────────────────────────────────────────────────────────")
        consoleIO.write("│        REGISTERED USERS       │                                          ")
        consoleIO.write("├───────┬───────────────┬───────┤──────────────────────────────────────────")
        consoleIO.write("│ Index │ Username      │ Role  │                   ID                     ")
        consoleIO.write("├───────┼───────────────┼───────│──────────────────────────────────────────")

        users.forEachIndexed { index, user ->
            consoleIO.write(
                "│ ${(index + 1).toString().padEnd(5)} │ " +
                        "${user.name.padEnd(13)} │ " +
                        "${user.role.toString().padEnd(5)} │"+
                        "${user.id.toString().padEnd(5)} │"
            )
        }

        consoleIO.write("└───────┴───────────────┴────────────────────────┴───────────────────")
        consoleIO.write("\nTotal registered users: ${users.size}\n")
    }
}
