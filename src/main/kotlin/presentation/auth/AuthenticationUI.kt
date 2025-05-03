package presentation.auth

import presentation.io.ConsoleIO
import kotlin.system.exitProcess

class AuthenticationUI(
    private val registerAdminUI: RegisterAdminUI,
    private val loginUserUI: LoginUserUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        write(
            """
Please choose an option:
1️⃣  Register as new 🛡️ Admin
2️⃣  Login 🔐 (Admin or 👤 Mate)
3️⃣  Exit ❌
        """.trimIndent()
        )
        val input = read().toIntOrNull()
        when (input) {
            1 -> registerAdminUI()
            2 -> loginUserUI()
            3 -> exitProcess(0)
            else -> write("\nInvalid input. Please enter a number between 1 and 3.")
        }
    }
}