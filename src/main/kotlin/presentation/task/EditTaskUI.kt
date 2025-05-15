package presentation.task

import data.mongodb_data.mappers.toUUID
import logic.models.Task
import logic.usecases.task.EditTaskUseCase
import presentation.io.ConsoleIO
import java.util.*

class EditTaskUI(
    private val editTaskUseCase: EditTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        write("Please enter the task ID:")
        val taskId = readUUID()

        write("Please enter the state ID:")
        val stateId = readUUID()

        write("Please enter the task name:")
        val taskName = read()

        write("Please enter the task description:")
        val taskDescription = read()

        if (taskId == null || stateId == null) {
            write("❌ Invalid task or state ID.")
            return
        }

        val task = Task(
            id = taskId,
            title = taskName,
            description = taskDescription,
            projectId = projectId,
            stateId = stateId
        )

        runCatching { editTaskUseCase(task) }
            .onSuccess { write("✅ Task updated successfully.") }
            .onFailure { write("❌ Failed to update task: ${it.message}") }
    }
}