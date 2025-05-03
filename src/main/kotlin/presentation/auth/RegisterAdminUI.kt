package presentation.auth

import logic.models.UserRole
import logic.usecases.auth.RegisterUseCase
import presentation.io.ConsoleIO

class RegisterAdminUI(
    private val registerUseCase: RegisterUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("🛡️ Register Admin")

        var isRegistered = false
        while (!isRegistered) {

            write("Enter admin username: ")
            val username = read()

            write("Enter password: 🔑")
            val password = read()

            runCatching { registerUseCase(username, password, UserRole.ADMIN) }
                .onSuccess {
                    write("Admin registered successfully! 🎉")
                    write("✅ Welcome, $username Let's begin by adding your first project.")
                    isRegistered = true
                }
                .onFailure {
                    write("Registration failed. ${it.message} ❌")
                }
        }
    }
}
