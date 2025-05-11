package presentation.project

import data.mongodb_data.mappers.toUUID
import di.SessionManager
import logic.models.UserRole
import logic.usecases.project.UpdateProjectUseCase
import presentation.io.ConsoleIO
import java.util.*

class UpdateProjectNameUI(
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        write("Enter the project ID to update:")
        lateinit var projectId: UUID
        runCatching {
            projectId = read().trimIndent().toUUID()
        }.onFailure {
            write("❌ please enter correct ID ")
            invoke()
            return
        }

        write("Please Enter new project name: ")
        val newProjectName = read()
        runCatching {
            updateProjectUseCase(
                id = projectId,
                name = newProjectName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }.onSuccess {
            write("✅ Project updated successfully.")
        }.onFailure { exception ->
            write("❌ Failed to update project: ${exception.message}")
        }
    }
}