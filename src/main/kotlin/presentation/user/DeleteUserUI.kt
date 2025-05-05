package presentation.user

import logic.models.UserRole
import logic.usecases.user.DeleteUserUseCase
import presentation.io.ConsoleIO
import presentation.session.SessionManager
import java.util.*

class DeleteUserUI(
    private val deleteUserUseCase: DeleteUserUseCase,
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
        val role = SessionManager.currentUser?.role
        if (role != null) {
            runCatching {
                deleteUserUseCase(role, userId)
            }.onSuccess {
                write("User with ID $userId has been successfully deleted.")
            }.onFailure {
                write("Error while deleting user id: $userId, ${it.message}")
            }
        }

    }
}
