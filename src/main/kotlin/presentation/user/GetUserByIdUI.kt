package presentation.user

import logic.usecases.user.GetUserByIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class GetUserByIdUI(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\nEnter user ID:")

        // Validate UUID format
        val id = try {
            UUID.fromString(read().trim())
        } catch (e: IllegalArgumentException) {
            write("Error: Invalid UUID format")
            return
        }

        try {
            val user = getUserByIdUseCase(id)
            write("\n=== User Details ===")
            write("ID: ${user.id}")
            write("Name: ${user.name}")
            write("Role: ${user.role}")
            write("Projects: ${user.projectIds.joinToString()}")
        } catch (e: NoSuchElementException) {
            write("Error: User with ID $id not found")
        } catch (e: Exception) {
            write("Error: An unexpected error occurred while fetching user details")
        }
    }
}