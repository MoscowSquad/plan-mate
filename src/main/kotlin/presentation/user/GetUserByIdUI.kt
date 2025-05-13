package presentation.user

import data.mongodb_data.mappers.toUUID
import logic.usecases.user.GetUserByIdUseCase
import presentation.io.ConsoleIO

class GetUserByIdUI(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\nEnter user ID:")
        val id = try {
            read().trim().toUUID()
        } catch (e: IllegalArgumentException) {
            write("❌ Error: Invalid UUID format")
            return
        }

        runCatching { getUserByIdUseCase(id) }
            .onSuccess { user ->
                write("\n✅ User Details:")
                write("ID       : ${user.id}")
                write("Name     : ${user.name}")
                write("Role     : ${user.role}")
                write("Projects : ${user.projectIds.joinToString()}")
            }
            .onFailure { error ->
                when (error) {
                    is NoSuchElementException -> write("❌ ${error.message}")
                    else -> write("❌ Unexpected error: ${error.message}")
                }
            }
    }
}
