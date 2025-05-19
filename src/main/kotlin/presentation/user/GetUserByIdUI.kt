package presentation.user

import data.mongodb_data.mappers.toUUID
import domain.usecases.task.GetTaskByIdUseCase
import domain.usecases.user.GetUserByIdUseCase
import domain.util.UserNotFoundException
import presentation.io.ConsoleIO

class GetUserByIdUI(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase, // Add this dependency
    consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        write("\nEnter user ID:")
        val id = try {
            read().trim().toUUID()
        } catch (e: IllegalArgumentException) {
            write("❌ Error: Invalid UUID format")
            return
        }

        runCatching { getUserByIdUseCase(id) }
            .onSuccess { user ->
                write("\n╔══════════════════════════════╗")
                write("║        USER DETAILS         ║")
                write("╚══════════════════════════════╝")

                write("\n┌──────────────────────────────┐")
                write("│ ID       : ${user.id.toString().padEnd(24)}│")
                write("│ Name     : ${user.name.padEnd(24)}│")
                write("│ Role     : ${user.role.toString().padEnd(24)}│")
                write("├──────────────────────────────┤")

                if (user.projectIds.isNotEmpty()) {
                    write("│ PROJECTS:".padEnd(31) + "│")
                    user.projectIds.forEach { projectId ->
                        write("│ - $projectId".padEnd(31) + "│")
                    }
                } else {
                    write("│ PROJECTS: None assigned".padEnd(31) + "│")
                }

                if (user.taskIds.isNotEmpty()) {
                    write("├──────────────────────────────┤")
                    write("│ TASKS:".padEnd(31) + "│")
                    user.taskIds.forEach { taskId ->
                        val task = runCatching { getTaskByIdUseCase(taskId) }.getOrNull()
                        if (task != null) {
                            write("│ - ${task.title}".padEnd(31) + "│")
                            write("│   ${task.description.take(25)}...".padEnd(31) + "│")
                        } else {
                            write("│ - $taskId (details not found)".padEnd(31) + "│")
                        }
                    }
                } else {
                    write("├──────────────────────────────┤")
                    write("│ TASKS: None assigned".padEnd(31) + "│")
                }

                write("└──────────────────────────────┘")
            }
            .onFailure { error ->
                when (error) {
                    is UserNotFoundException -> write("❌ ${error.message}")
                    else -> write("❌ Unexpected error: ${error.message}")
                }
            }
    }
}