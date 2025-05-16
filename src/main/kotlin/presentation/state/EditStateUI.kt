package presentation.state

import data.session_manager.SessionManager
import domain.models.TaskState
import domain.models.User.UserRole
import domain.usecases.task_state.EditTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class EditStateUI(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        write("Please enter the state ID:")
        val stateId = readUUID()

        write("Please enter the new state name:")
        val stateName = read()

        if (stateId == null) {
            write("❌ Invalid state ID.")
            return
        }

        val state = TaskState(
            id = stateId,
            name = stateName,
            projectId = projectId
        )

        runCatching {
            editTaskStateUseCase(
                state,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }
            .onSuccess { write("✅ State updated successfully.") }
            .onFailure { write("❌ Failed to update state: ${it.message}") }
    }
}