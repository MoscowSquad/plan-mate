package presentation.user

import logic.usecases.user.GetUserByIdUseCase
import presentation.io.ConsoleIO
import java.util.UUID

class GetUserByIdUI(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\nEnter user ID:")
        val id = try {
            UUID.fromString(read())
        } catch (e: Exception) {
            write("Invalid UUID"); return
        }
        val user = getUserByIdUseCase(id)
        write("Found: ${user.name}, Role: ${user.role}")

    }
}