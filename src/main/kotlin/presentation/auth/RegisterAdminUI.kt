package presentation.auth

import logic.usecases.auth.RegisterUseCase
import presentation.io.ConsoleIO

class RegisterAdminUI(
    private val registerUseCase: RegisterUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {

    }
}