package presentation

import presentation.audit.AuditUI
import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI
import presentation.user.UserUI
import kotlin.system.exitProcess

open class PlanMateConsoleUI(
    private val authenticationUI: AuthenticationUI,
    private val projectsUI: ProjectsUI,
    private val userUI: UserUI,
    private val auditUI: AuditUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    fun start(isStopped: Boolean = false) {
        write(
            """
🔷 Welcome to PlanMate v1.0 🔷
Let's set up the app. Please sign up as the admin user.
        """.trimIndent()
        )
        authenticationUI()
        menuLoop(isStopped)
    }

    private fun menuLoop(isStopped: Boolean = false) {
        while (true) {
            showOptions()
            goToScreen()
            if (isStopped)
                break
        }
    }

    private fun showOptions() {
        write(
            """
🏠 Main Menu:
1. 📁 Projects Management
2. 👥 User management
3. 📜 View audit-log
4. ❌ Exit

Enter your option:""".trimIndent()
        )
    }

     private fun goToScreen() {
        val input = read().toIntOrNull()
        when (input) {
            1 -> projectsUI()
            2 -> userUI()
            3 -> auditUI()
            4 -> exitProcess(0)
            else -> write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}
