package presentation.user

import data.mongodb_data.mappers.toUUID
import di.SessionManager
import logic.models.UserRole
import logic.usecases.user.DeleteUserUseCase
import logic.util.UnauthorizedAccessException
import presentation.io.ConsoleIO

class DeleteUserUI(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()
        if (currentUserRole != UserRole.ADMIN) {
            write("\nError: Only ADMIN users can delete accounts.")
            return
        }

        write("\n=======================")
        write("║      DELETE USER     ║")
        write("=======================\n")

        write("Enter user ID to delete: ")
        val input = read().trim()
        val userId = try {
            input.toUUID()
        } catch (e: IllegalArgumentException) {
            write("Error: Invalid UUID format. Please provide a valid user ID.")
            return
        }

        try {
            deleteUserUseCase(currentUserRole, userId)
            write("User with ID $userId has been successfully deleted.")
        } catch (e: UnauthorizedAccessException) {
            write("Error: You don't have permission to delete users.")
        } catch (e: NoSuchElementException) {
            write("Error: User with ID $userId not found.")
        } catch (e: Exception) {
            write("Error: An unexpected error occurred while deleting the user.")
        }
    }
}