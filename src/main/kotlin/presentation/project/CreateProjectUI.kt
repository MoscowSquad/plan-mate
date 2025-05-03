package presentation.project

import logic.models.UserRole
import logic.usecases.project.CreateProjectUseCase
import presentation.io.ConsoleIO
import presentation.session.SessionManager

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
                write("Project $it created successfully!")
            }
            .onFailure {
                write("Error creating project: ${it.message}")
            }
    }
}