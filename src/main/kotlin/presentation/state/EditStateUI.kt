package presentation.state

import data.mongodb_data.mappers.toUUID
import logic.models.TaskState
import logic.usecases.task_state.EditTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class EditStateUI(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        write("Please enter the state ID:")
        val stateId = runCatching { read().trimIndent().toUUID() }.getOrNull()

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

        runCatching { editTaskStateUseCase(state) }
            .onSuccess { write("✅ State updated successfully.") }
            .onFailure { write("❌ Failed to update state: ${it.message}") }
    }
}