package presentation.user

import data.mongodb_data.mappers.toUUID
import data.session_manager.SessionManager
import domain.usecases.user.AssignProjectToUserUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI

class AssignProjectToUserUI(
    private val assignProjectToUserUseCase: AssignProjectToUserUseCase,
    private val consoleIO: ConsoleIO,
    private val getAllProjectsUI: GetAllProjectsUI
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        val currentUserRole = SessionManager.getCurrentUserRole()
        getAllProjectsUI.invoke()
        write("\n=== Assign Project to User ===")

        write("Enter Project ID:")
        val projectIdInput = read()

        val projectId = try {
            projectIdInput.toUUID()
        } catch (e: IllegalArgumentException) {
            write("Invalid Project ID format. Please enter a valid UUID.")
            return
        }

        write("Enter User ID:")
        val userIdInput = read()

        val result = runCatching {
            val userId = try {
                userIdInput.toUUID()
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid UUID format.")
            }

            assignProjectToUserUseCase(currentUserRole, projectId, userId)
        }

        result
            .onSuccess {
                write("User successfully assigned to the project.")
            }
            .onFailure {
                write("Failed to assign user. ${it.message}")
            }
    }
}
