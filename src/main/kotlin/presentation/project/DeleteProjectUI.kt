package presentation.project

import data.csv_data.mappers.toUUID
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.DeleteProjectUseCase
import presentation.io.ConsoleIO

class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        consoleIO.write("Enter the project ID to delete:")
        val projectId = consoleIO.read().toUUID()
        runCatching {
            deleteProjectUseCase(
                projectId,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }
            .onSuccess {
                consoleIO.write("Project deleted successfully.")
            }
            .onFailure { exception ->
                consoleIO.write("Error deleting project: ${exception.message}")
            }
    }
}