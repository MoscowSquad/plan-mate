package presentation.project

import data.session_manager.SessionManager
import logic.models.User.UserRole
import logic.usecases.project.CreateProjectUseCase
import presentation.io.ConsoleIO

class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        write("Enter project name:")
        val projectName = read()

        runCatching {
            createProjectUseCase(
                name = projectName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }
            .onSuccess {
                write("Project named $projectName with id $it created successfully!")
            }
            .onFailure {
                write("Error creating project: ${it.message}")
            }
    }
}