package presentation.auth

import logic.usecases.auth.LoginUseCase
import presentation.io.ConsoleIO

class LoginUserUI(
    private val loginUseCase: LoginUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("🔐 Login")

        var isLoggedIn = false
        while (!isLoggedIn) {

            write("Enter username: ")
            val username = read()

            write("Enter password: 🔑")
            val password = read()

            runCatching { loginUseCase(username, password) }
                .onSuccess {
                    write("✅ Logged in successfully! Welcome back, $username.")
                    isLoggedIn = true
                }
                .onFailure {
                    write("Login failed. ${it.message} 😞")
                }
        }
    }
}
