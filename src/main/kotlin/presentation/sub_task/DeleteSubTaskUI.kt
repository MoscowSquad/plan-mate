package presentation.sub_task

import domain.usecases.sub_task.DeleteSubTaskUseCase
import presentation.io.ConsoleIO

class DeleteSubTaskUI (
    private val deleteSubTaskUseCase: DeleteSubTaskUseCase,
    private val consoleIO: ConsoleIO
    ) : ConsoleIO by consoleIO {
        suspend operator fun invoke() {
            write("Please enter sub task ID:")
            val subTaskaskId = readUUID()

            if (subTaskaskId == null) {
                write("❌ Invalid Sub task ID.")
                return
            }

            runCatching { deleteSubTaskUseCase(subTaskaskId) }
                .onSuccess { write("✅ Sub Task deleted successfully.") }
                .onFailure { write("❌ Failed to delete Sub task: ${it.message}") }
        }
}