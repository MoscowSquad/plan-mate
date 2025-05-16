package presentation.user

import data.mongodb_data.dto.TaskDto
import data.session_manager.SessionManager
import logic.models.User
import logic.models.User.UserRole
import logic.usecases.user.CreateUserUseCase
import presentation.io.ConsoleIO
import java.util.*

class CreateUserUI(
    private val createUserUseCase: CreateUserUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
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

        runCatching {
            val success = createUserUseCase.createNewUser(currentUserRole, newUser, password)
            if (success) {
                write("\n✅ User '$username' created successfully!")

                // Task assignment flow
                if (promptYesNo("Do you want to assign this user to a task?")) {
                    // Get available tasks (mock implementation)
                    val tasks = listOf(
                        TaskDto(
                            id = UUID.randomUUID().toString(),
                            name = "Implement Login Feature",
                            description = "Create user authentication system",
                            projectId = UUID.randomUUID().toString(),
                            stateId = UUID.randomUUID().toString()
                        ),
                        TaskDto(
                            id = UUID.randomUUID().toString(),
                            name = "Design Dashboard UI",
                            description = "Create mockups for admin dashboard",
                            projectId = UUID.randomUUID().toString(),
                            stateId = UUID.randomUUID().toString()
                        )
                    )

                    if (tasks.isEmpty()) {
                        write("ℹ️ No tasks available for assignment")
                    } else {
                        write("\nAvailable Tasks:")
                        tasks.forEachIndexed { index, task ->
                            write("${index + 1}. ${task.name} (${task.description.take(30)}...)")
                        }

                        while (true) {
                            write("\nEnter task number to assign (or 0 to cancel):")
                            val input = read().trim()

                            if (input == "0") break

                            try {
                                val taskIndex = input.toInt() - 1
                                if (taskIndex in tasks.indices) {
                                    val selectedTask = tasks[taskIndex]
                                    write("\n🔗 User would be assigned to: ${selectedTask.name}")
                                    write("   (Actual assignment implementation would go here)")
                                    break
                                } else {
                                    write("⚠️ Please enter a number between 1 and ${tasks.size}")
                                }
                            } catch (e: NumberFormatException) {
                                write("⚠️ Please enter a valid number")
                            }
                        }
                    }
                }
            } else {
                write("❌ Failed to create user")
            }
        }.onFailure {
            write("❌ Failed to create user: ${it.message}")
        }
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
            write("\nEnter password (min 8 characters):")
            val input = read()
            if (input.length >= 8) return input
            write("❌ Password must be at least 8 characters")
        }
    }

    private fun promptForRole(): UserRole {
        while (true) {
            write("\nSelect role:")
            UserRole.values().forEach { role ->
                write("${role.ordinal + 1}. $role")
            }
            write("Enter choice (1-${UserRole.values().size}):")

            try {
                val input = read().trim().toInt()
                if (input in 1..UserRole.values().size) {
                    return UserRole.values()[input - 1]
                }
                write("⚠️ Please enter a number between 1 and ${UserRole.values().size}")
            } catch (e: NumberFormatException) {
                write("⚠️ Please enter a valid number")
            }
        }
    }

    private fun promptYesNo(question: String): Boolean {
        while (true) {
            write("\n$question (Y/N):")
            when (read().trim().uppercase()) {
                "Y" -> return true
                "N" -> return false
                else -> write("⚠️ Please enter Y or N")
            }
        }
    }
}