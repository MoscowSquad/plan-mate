package presentation.task

import domain.usecases.task.DeleteTaskUseCase
import presentation.io.ConsoleIO

class DeleteTaskUI(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        write("Please enter task ID:")
        val taskId = readUUID()

        if (taskId == null) {
            write("❌ Invalid task ID.")
            return
        }

        runCatching { deleteTaskUseCase(taskId) }
            .onSuccess { write("✅ Task deleted successfully.") }
            .onFailure { write("❌ Failed to delete task: ${it.message}") }
    }
}