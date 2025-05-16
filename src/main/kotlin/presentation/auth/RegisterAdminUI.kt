package presentation.auth

import domain.models.User.UserRole
import domain.usecases.auth.RegisterUseCase
import presentation.io.ConsoleIO

class RegisterAdminUI(
    private val registerUseCase: RegisterUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {
        write("🛡️ Register Admin")

        var isRegistered = false
        while (!isRegistered) {

            write("Enter admin username: ")
            val username = read()

            write(
                """
                # At least one lowercase letter
                # At least one uppercase letter
                # At least one digit
                # At least one special character
                # Minimum length of 8 characters""".trimIndent()
            )

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
