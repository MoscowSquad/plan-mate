package presentation.state

import data.mongodb_data.mappers.toUUID
import logic.usecases.task_state.DeleteTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class DeleteStateUI(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        write("Please enter the state ID:")
        val stateId = runCatching { read().trimIndent().toUUID() }.getOrNull()

        if (stateId == null) {
            write("❌ Invalid state ID.")
            return
        }

        runCatching { deleteTaskStateUseCase(stateId, projectId) }
            .onSuccess { write("✅ State deleted successfully.") }
            .onFailure { write("❌ Failed to delete state: ${it.message}") }
    }
}