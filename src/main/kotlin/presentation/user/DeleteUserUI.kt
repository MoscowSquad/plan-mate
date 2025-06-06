package presentation.user

import data.mongodb_data.mappers.toUUID
import data.session_manager.SessionManager
import domain.usecases.user.DeleteUserUseCase
import presentation.io.ConsoleIO

class DeleteUserUI(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()

        write("\n=======================")
        write("║      DELETE USER     ║")
        write("=======================\n")

        write("Enter user ID to delete: ")
        val input = read().trim()
        val userId = try {
            input.toUUID()
        } catch (e: IllegalArgumentException) {
            write("❌ Error: Invalid UUID format. Please provide a valid user ID.")
            return
        }

        val result = runCatching {
            deleteUserUseCase(currentUserRole, userId)
        }

        result
            .onSuccess {
                write("✅ User with ID $userId has been successfully deleted.")
            }
            .onFailure {
                write("❌ Failed to delete user. ${it.message}")
            }
    }
}
