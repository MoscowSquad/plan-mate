package presentation.auth

import logic.usecases.auth.LoginUseCase
import presentation.io.ConsoleIO
import presentation.session.LoggedInUser
import presentation.session.SessionManager

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
                    SessionManager.currentUser = LoggedInUser(
                        id = it.id,
                        name = it.name,
                        role = it.role,
                        projectIds = it.projectIds
                    )
                }
                .onFailure {
                    write("Login failed. ${it.message} 😞")
                }
        }
    }
}
