package presentation.task

import logic.usecases.task.GetTaskByProjectIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class GetAllTasksUI(
    private val getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        runCatching { getTaskByProjectIdUseCase(projectId) }
            .onSuccess { tasks ->
                if (tasks.isEmpty()) {
                    write("ℹ️ No tasks found for this project.")
                } else {
                    tasks.forEach { task ->
                        write(
                            """
                            -------------------------------------------------------
                            | Task Name: ${task.name}
                            | Description: ${task.description}
                            | Task ID: ${task.id}
                            -------------------------------------------------------
                            """.trimIndent()
                        )
                    }
                }
            }
            .onFailure { write("❌ Failed to fetch tasks: ${it.message}") }
    }
}