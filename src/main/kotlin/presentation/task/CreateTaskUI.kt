package presentation.task

import logic.models.Task
import logic.usecases.task.AddTaskUseCase
import presentation.io.ConsoleIO
import java.util.*

class CreateTaskUI(
    private val addTaskUseCase: AddTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke(projectId: UUID) {
        write("Please enter the task name:")
        val taskName = read()

        write("Please enter the task description:")
        val taskDescription = read()

        val task = Task(
            id = UUID.randomUUID(),
            name = taskName,
            description = taskDescription,
            projectId = projectId,
            stateId = UUID.randomUUID() // Temporary state ID
        )

        runCatching { addTaskUseCase(task) }
            .onSuccess { write("✅ Task created successfully.") }
            .onFailure { write("❌ Failed to create task: ${it.message}") }
    }
}