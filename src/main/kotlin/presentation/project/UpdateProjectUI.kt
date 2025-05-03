package presentation.project

import data.mappers.toUUID
import logic.models.UserRole
import logic.usecases.project.UpdateProjectUseCase
import presentation.io.ConsoleIO
import presentation.session.SessionManager

class UpdateProjectUI(
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        consoleIO.write("Enter the project ID to update:")
        val projectId = consoleIO.read().toUUID()
        consoleIO.write("Enter the new project name:")
        val newProjectName = consoleIO.read()
        runCatching {
            updateProjectUseCase(
                id = projectId,
                name = newProjectName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }.onSuccess {
            consoleIO.write("Project updated successfully.")
        }.onFailure { exception ->
            consoleIO.write("Error updating project: ${exception.message}")
        }
    }
}