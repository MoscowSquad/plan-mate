package presentation.project

import data.mappers.toUUID
import logic.models.UserRole
import logic.usecases.project.DeleteProjectUseCase
import presentation.io.ConsoleIO
import presentation.session.SessionManager

class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    operator fun invoke() {
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