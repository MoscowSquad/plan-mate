package presentation.sub_task

import domain.usecases.task.GetTaskByIdUseCase
import presentation.io.ConsoleIO


class CalculateSubTaskPercentageUI(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke() {

        write("Please enter Task ID:")
        val taskId = readUUID()

        if (taskId == null) {
            write("❌ Invalid Task ID.")
            return
        }

        val task = runCatching { getTaskByIdUseCase(taskId) }
            .onFailure { write("❌ Failed to retrieve task: ${it.message}") }
            .getOrNull()

        if (task == null) {
            write("❌ Task not found.")
            return
        }

        write("Task completed percentage: ${task.calculateSubTaskCompletionPercentage()}")
    }
}
