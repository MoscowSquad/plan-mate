package presentation.state

import di.SessionManager
import logic.models.TaskState
import logic.models.UserRole
import logic.usecases.task_state.AddTaskStateUseCase
import presentation.io.ConsoleIO
import java.util.*

class CreateStateUI(
    private val addTaskStateUseCase: AddTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        write("Please enter the state name:")
        val stateName = read()

        val state = TaskState(
            id = UUID.randomUUID(),
            name = stateName,
            projectId = projectId
        )

        runCatching {
            addTaskStateUseCase(
                state,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
        }
            .onSuccess { write("✅ State created successfully.") }
            .onFailure { write("❌ Failed to create state: ${it.message}") }
    }
}