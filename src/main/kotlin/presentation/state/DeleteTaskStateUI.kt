package presentation.state

import domain.usecases.task_state.DeleteTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class DeleteTaskStateUI(
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

        runCatching { deleteTaskStateUseCase(stateId, projectId) }
            .onSuccess { write("✅ State deleted successfully.") }
            .onFailure { write("❌ Failed to delete state: ${it.message}") }
    }
}