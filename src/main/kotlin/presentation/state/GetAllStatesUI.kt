package presentation.state

import logic.usecases.task_state.GetTaskStatesByProjectIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class GetAllStatesUI(
    private val getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        runCatching { getTaskStatesByProjectIdUseCase(projectId) }
            .onSuccess { states ->
                if (states.isEmpty()) {
                    write("ℹ️ No states found for this project.")
                } else {
                    states.forEach { state ->
                        write(
                            """
                            -------------------------------------------------------
                            | State Name: ${state.name}
                            | State ID: ${state.id}
                            -------------------------------------------------------
                            """.trimIndent()
                        )
                    }
                }
            }
            .onFailure { write("❌ Failed to fetch states: ${it.message}") }
    }
}