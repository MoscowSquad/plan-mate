package presentation.user

import data.mongodb_data.mappers.toUUID
import di.SessionManager
import logic.usecases.user.AssignProjectToUserUseCase
import presentation.io.ConsoleIO

class AssignProjectToUserUI(
    private val assignProjectToUserUseCase: AssignProjectToUserUseCase,
    private val sessionManager: SessionManager,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\n=== Assign Project to User ===")

        write("Enter Project ID:")
        val projectIdInput = read()
        val projectId = projectIdInput.toUUID()

        write("Enter User ID:")
        val userIdInput = read()
        val userId = userIdInput.toUUID()

        runCatching { sessionManager.getCurrentUserRole()?.let { assignProjectToUserUseCase(it, projectId, userId) } }
            .onSuccess {
                write("User successfully assigned to the project.")
            }.onFailure {
                write("Failed to assign user. ${it.message}")
            }
    }
}