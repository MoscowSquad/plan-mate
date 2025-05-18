package presentation.task

import domain.usecases.task.EditTaskUseCase
import domain.usecases.task.GetTaskByIdUseCase
import presentation.io.ConsoleIO
import presentation.sub_task.SubTaskUI
import java.util.*

class EditTaskUI(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val subTaskUI: SubTaskUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        write("Please enter the task ID:")
        val taskId = readUUID()

        if (taskId == null) {
            write("❌ Invalid task ID.")
            return
        }

        val chosenTask = runCatching { getTaskByIdUseCase(taskId) }
            .onFailure { write("❌ Failed to retrieve task: ${it.message}") }
            .getOrElse {
                write("❌ Task not found.")
                return
            }

        write(
            "What do you want to edit?" +
                    "\n1. Task details" +
                    "\n2. Subtasks" +
                    "\n3. Back"
        )

        val choice = read()

        when (choice) {
            "1" -> {
                write("Please enter the state ID:")
                val stateId = readUUID()

                write("Please enter the new task name:")
                val taskName = read()

                write("Please enter the new task description:")
                val taskDescription = read()

                if (stateId == null) {
                    write("❌ Invalid task or state ID.")
                    return
                }

                val task = chosenTask.copy(
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

            "2" -> {
                subTaskUI(chosenTask)
            }

            "3" -> {
                write("Navigating back...")
            }

            else -> {
                write("❌ Invalid choice.")
                invoke(projectId)
            }
        }
    }
}