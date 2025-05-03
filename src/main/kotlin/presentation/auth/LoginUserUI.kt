package presentation.auth

import logic.usecases.auth.LoginUseCase
import presentation.io.ConsoleIO

class LoginUserUI(
    private val loginUseCase: LoginUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        while (true) {
            write("\uD83D\uDD10 Login")
            write("Enter username: ")
            val username = read()
            write("Enter password: \uD83D\uDD11")
            val password = read()
            try {
                val success = loginUseCase(username, password)
                if (success) {
                    write("Login successful! \uD83D\uDE0A")
                    break
                }
            } catch (e: Exception){
                write("Login failed. ${e.message} \uD83D\uDE1E")
            }
        }
    }
}
