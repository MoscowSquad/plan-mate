package presentation

import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI
import kotlin.system.exitProcess

class PlanMateConsoleUI(
    private val authenticationUI: AuthenticationUI,
    private val projectsUI: ProjectsUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    fun start(stopImminently: Boolean = false) {
        write(
            """
🔷 Welcome to PlanMate v1.0 🔷
Let's set up the app. Please sign up as the admin user.
        """.trimIndent()
        )
        authenticationUI()
        menuLoop(stopImminently)
    }

    private fun menuLoop(stopImminently: Boolean = false) {
        while (true) {
            showOptions()
            goToScreen()
            if (stopImminently)
                break
        }
    }

    private fun showOptions() {
        write(
            """
🏠 Main Menu:
1. 📁 Show projects
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
            2 -> exitProcess(0)
            else -> write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}
