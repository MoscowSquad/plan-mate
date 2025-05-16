package presentation.state

import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.task_state.DeleteTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class DeleteStateUI(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        write("Please enter the state ID:")
        val stateId = readUUID()

        if (stateId == null) {
            write("❌ Invalid state ID.")
            return
        }

        runCatching {
            deleteTaskStateUseCase(
                stateId, projectId,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }
            .onSuccess { write("✅ State deleted successfully.") }
            .onFailure { write("❌ Failed to delete state: ${it.message}") }
    }
}