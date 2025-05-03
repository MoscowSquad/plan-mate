package presentation

import logic.models.UserRole
import logic.usecases.user.DeleteUserUseCase
import presentation.io.ConsoleIO
import java.util.*

class DeleteUserUI(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val currentUserRole: () -> UserRole,
    consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\n=======================")
        write("║      DELETE USER     ║")
        write("=======================\n")

        write("Enter user ID to delete:")
        val input = read().trim()
        val userId = try {
            UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            write(" Invalid UUID format. Please provide a valid user ID.")
            return
        }
        val role = currentUserRole()
        deleteUserUseCase(role, userId)
        write(" User with ID $userId has been successfully deleted.")

    }
}
