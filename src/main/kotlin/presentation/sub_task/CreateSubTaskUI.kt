package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.CreateSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import presentation.io.ConsoleIO
import java.util.*

class CreateSubTaskUI(
    private val createSubTaskUseCase: CreateSubTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {

    suspend operator fun invoke(task: Task) {
        write("Please enter sub task title:")
        val subTaskTitle = read()
        write("Please enter sub task description:")
        val subTaskDescription = read()

        val subTask = SubTask(
            id = UUID.randomUUID(),
            title = subTaskTitle,
            description = subTaskDescription,
            isCompleted = false,
            parentTaskId = task.id
        )

        val updatedTask = task.copy(
            subTasks = task.subTasks + subTask
        ).also { print(it) }

        runCatching { createSubTaskUseCase(subTask) }
            .onSuccess {
                write("✅ sub task added successfully.")
                runCatching { editTaskUseCase(updatedTask) }
                    .onSuccess { write("✅ Task updated successfully.") }
                    .onFailure { write("❌ Failed to update task: ${it.message}") }
            }
            .onFailure { write("❌ failed to add sub  task: ${it.message}") }
    }
}