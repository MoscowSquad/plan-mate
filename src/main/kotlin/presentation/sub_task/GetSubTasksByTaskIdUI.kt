package presentation.sub_task

import domain.usecases.sub_task.GetSubTasksByTaskIdUseCase
import presentation.io.ConsoleIO

class GetSubTasksByTaskIdUI (
    private val getSubTasksByTaskIdUseCase: GetSubTasksByTaskIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        write("Please enter task id:")
        val taskId= readUUID()
        if (taskId == null ) {
            write("❌ Invalid task sub task.")
            return
        }
        runCatching { getSubTasksByTaskIdUseCase(taskId) }
            .onSuccess { subTasks ->
                if (subTasks.isEmpty()) {
                    write("ℹ️ No sub tasks found for this Task.")
                } else {
                    subTasks.forEach { subtask ->
                        write(
                            """
                            -------------------------------------------------------
                            | Sub Task Name: ${subtask.title}
                            | Sub Description: ${subtask.description}
                            | Sub Task ID: ${subtask.id}
                            | Sub Task State ID: ${subtask.isCompleted}
                            | Task ID: ${subtask.parentTaskId}
                            -------------------------------------------------------
                            """.trimIndent()
                        )
                    }
                }
            }
            .onFailure { write("❌ Failed to fetch sub tasks: ${it.message}") }
    }
}