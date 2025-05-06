package presentation.user

import di.SessionManager
import logic.models.UserRole
import presentation.io.ConsoleIO
import logic.usecases.user.AssignProjectToUserUseCase
import java.util.UUID

class AssignProjectToUserUI(
    private val assignProjectToUserUseCase: AssignProjectToUserUseCase,
    private val sessionManager: SessionManager,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        write("\n=== Assign Project to User ===")

        write("Enter Project ID:")
        val projectIdInput = read()
        val projectId = UUID.fromString(projectIdInput)

        write("Enter User ID:")
        val userIdInput = read()
        val userId = UUID.fromString(userIdInput)

        runCatching { sessionManager.getCurrentUserRole()?.let { assignProjectToUserUseCase(it, projectId, userId) } }
            .onSuccess {
                write("User successfully assigned to the project.")
            }.onFailure {
                write("Failed to assign user. ${it.message}")
            }
    }
}
