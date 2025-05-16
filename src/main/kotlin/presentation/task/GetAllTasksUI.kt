package presentation.task

import domain.usecases.task.GetTaskByProjectIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class GetAllTasksUI(
    private val getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        runCatching { getTaskByProjectIdUseCase(projectId) }
            .onSuccess { tasks ->
                if (tasks.isEmpty()) {
                    write("ℹ️ No tasks found for this project.")
                } else {
                    tasks.forEach { task ->
                        write(
                            """
                            -------------------------------------------------------
                            | Task Name: ${task.title}
                            | Description: ${task.description}
                            | Task ID: ${task.id}
                            | Task State ID: ${task.stateId}
                            | Project ID: ${task.projectId}
                            -------------------------------------------------------
                            """.trimIndent()
                        )
                    }
                }
            }
            .onFailure { write("❌ Failed to fetch tasks: ${it.message}") }
    }
}