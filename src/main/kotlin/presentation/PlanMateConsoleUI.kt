package presentation

import AssignProjectToUserUI
import CreateUserUI
import presentation.io.ConsoleIO
import kotlin.system.exitProcess

class PlanMateConsoleUI(
    private val assignProjectToUserUI: AssignProjectToUserUI,
    private val createUserUI: CreateUserUI,
    private val deleteUserUI: DeleteUserUI,
    private val getAllUserUI: GetAllUserUI,
    private val getUserByIdUI: GetUserByIdUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    fun start(stopImminently: Boolean = false) {
        write(
            """
=============================================
║       Welcome to Plan-Mate System!        ║
=============================================
        """.trimIndent()
        )
        loginScreen()
        menuLoop(stopImminently)
    }

    private fun loginScreen() {
        write("Please login with your username and password:")
        write("Username: ")
        val username = read()
        write("Password: ")
        val password = read()
        write("Welcome, $username!")
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
 === Main Menu === 

1  - Assign Project to User
2  - Create New User
3  - Delete User
4  - Show All Users
5  - Get User by ID
6  - Exit

Enter your option:
""".trimIndent()
        )
    }

    private fun goToScreen() {
        val input = read().toIntOrNull()
        when (input) {
            1 -> assignProjectToUserUI()
            2 -> createUserUI()
            3 -> deleteUserUI()
            4 -> getAllUserUI()
            5 -> getUserByIdUI()
            6 -> exitProcess(0)
            else -> write("\nInvalid input. Please enter a number between 1 and 6.")
        }
    }
}
