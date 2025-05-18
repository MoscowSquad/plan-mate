package presentation

import presentation.audit.AuditUI
import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI
import presentation.state.TaskStateUI
import presentation.task.TasksUI
import presentation.user.UserUI
import kotlin.system.exitProcess

class PlanMateConsoleUI(
    val authenticationUI: AuthenticationUI,
    private val projectsUI: ProjectsUI,
    private val tasksUI: TasksUI,
    private val taskStateUI: TaskStateUI,
    private val userUI: UserUI,
    private val auditUI: AuditUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend fun start() {
        write(
            """
🔷 Welcome to PlanMate v2.0 🔷
Let's set up the app. Please sign up as the admin user.
        """.trimIndent()
        )
        authenticationUI()
        menuLoop()
    }

    private suspend fun menuLoop() {
        while (true) {
            showOptions()
            goToScreen()
        }
    }

    private fun showOptions() {
        write(
            """
🏠 Main Menu:
1. 📁 Projects Management
2. 👥 User Management
3. 🗂️ Task Management
4. 📊 State Management
5. 📜 View Audit Log
6. ❌ Exit

Enter your option:""".trimIndent()
        )
    }

    private suspend fun goToScreen() {
        val input = read().toIntOrNull()
        when (input) {
            1 -> projectsUI()
            2 -> userUI()
            3 -> tasksUI()
            4 -> taskStateUI()
            5 -> auditUI()
            6 -> exitProcess(0)
            else -> {
                write("\nInvalid input. Please enter a number between 1 and 6.")
            }
        }
    }
}