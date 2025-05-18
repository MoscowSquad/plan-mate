package presentation.state

import domain.models.TaskState
import domain.usecases.task_state.EditTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class EditTaskStateUI(
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

        runCatching { editTaskStateUseCase(state) }
            .onSuccess { write("✅ State updated successfully.") }
            .onFailure { write("❌ Failed to update state: ${it.message}") }
    }
}