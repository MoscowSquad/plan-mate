package presentation.sub_task

import domain.models.SubTask
import domain.usecases.sub_task.CreateSubTaskUseCase
import presentation.io.ConsoleIO
import java.util.UUID

class CreateSubTaskUI(
    private val createSubTaskUseCase: CreateSubTaskUseCase,
    private val consoleIO: ConsoleIO

) : ConsoleIO by consoleIO{

    suspend operator fun invoke() {
        write("Please enter task ID:")
        val taskId = readUUID()
        write("Please enter sup task title:")
        val subTaskTitle = read()
        write("Please enter sub task description:")
        val subTaskDescription= read()


        if (taskId == null) {
            write("❌ Invalid task ID.")
            return
        }
        val subTask= SubTask(
            title = subTaskTitle,
            isCompleted = false,
            description = subTaskDescription,
            parentTaskId = taskId,
            id = UUID.randomUUID()
        )
        runCatching { createSubTaskUseCase(subTask) }
            .onSuccess { write("✅ sub task added successfully.") }
            .onFailure { write("❌ failed to add sub  task: ${it.message}") }

    }

}