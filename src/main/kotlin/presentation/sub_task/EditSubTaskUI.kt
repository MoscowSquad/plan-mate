package presentation.sub_task

import domain.models.SubTask
import domain.usecases.sub_task.UpdateSubTaskUseCase
import presentation.io.ConsoleIO
import java.util.UUID

class EditSubTaskUI (
    private val updateSubTaskUseCase: UpdateSubTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        write("Please enter the task ID:")
        val taskId = readUUID()
        write("Please enter sub task ID:")
        val subTaskId = readUUID()
        write("Please enter sub task name:")
        val subTaskName = read()

        write("Please enter the task description:")
        val taskDescription = read()

        if (subTaskId == null || taskId == null) {
            write("❌ Invalid task sub task.")
            return
        }

        val subTask = SubTask(
            id = subTaskId,
            title = subTaskName,
            description = taskDescription,
            isCompleted = true,
            parentTaskId = taskId
        )

        runCatching { updateSubTaskUseCase(subTask) }
            .onSuccess { write("✅ Task updated successfully.") }
            .onFailure { write("❌ Failed to update task: ${it.message}") }
    }
}


