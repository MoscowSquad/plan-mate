package presentation.user

import data.session_manager.SessionManager
import logic.models.User
import logic.usecases.user.GetAllUsersUseCase
import presentation.io.ConsoleIO

class GetAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()
        printHeader()

        val result = runCatching {
            getAllUsersUseCase(currentUserRole)
        }

        result
            .onSuccess { users ->
                if (users.isEmpty()) {
                    write("\nℹ️  No users found in the system")
                } else {
                    printUserTable(users)
                    write("\nTotal registered users: ${users.size}\n")
                }
            }
            .onFailure {
                write("\n❌ Failed to load users: ${it.message}")
            }
    }

    private fun printHeader() {
        write("\n╔══════════════════════════════════════════════════════════╗")
        write("║                   USER DIRECTORY                         ║")
        write("╚══════════════════════════════════════════════════════════╝")
    }

    private fun printUserTable(users: List<User>) {
        write("\n┌───────┬───────────────┬───────┬───────────────────────────────────────┐")
        write("│ Index │ Username      │ Role  │ Assigned Tasks                        │")
        write("├───────┼───────────────┼───────┼───────────────────────────────────────┤")

        users.forEachIndexed { index, user ->
            val tasksInfo = if (user.taskIds.isNotEmpty()) {
                user.taskIds.joinToString(", ") { it.toString().take(8) + "..." }
            } else {
                "No tasks assigned"
            }

            write(
                "│ ${(index + 1).toString().padEnd(5)} │ " +
                        "${user.name.padEnd(13)} │ " +
                        "${user.role.toString().padEnd(5)} │ " +
                        tasksInfo.padEnd(35) + "│"
            )
        }

        write("└───────┴───────────────┴───────┴───────────────────────────────────────┘")
    }
}