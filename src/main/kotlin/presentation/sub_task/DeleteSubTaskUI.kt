package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.DeleteSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import presentation.io.ConsoleIO

class DeleteSubTaskUI(
    private val deleteSubTaskUseCase: DeleteSubTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(task: Task) {
        if (task.subTasks.isEmpty()) {
            write("⚠️ No sub-tasks available to edit.")
            return
        }

        write(
            """
            Please choose a sub-task to edit:
            ${task.subTasks.joinToString(separator = "\n") { "${it.id} - ${it.title}" }}
            """.trimIndent()
        )

        val subTask = readValidSubTask(task)

        val updatedTask = task.copy(
            subTasks = task.subTasks.filter { it.id != subTask.id }
        )

        runCatching { deleteSubTaskUseCase(subTask.id) }
            .onSuccess {
                write("✅ Sub Task deleted successfully.")
                runCatching { editTaskUseCase(updatedTask) }
                    .onSuccess { write("✅ Task updated successfully.") }
                    .onFailure { write("❌ Failed to update task: ${it.message}") }
            }
            .onFailure { write("❌ Failed to delete Sub task: ${it.message}") }
    }

    private fun readValidSubTask(task: Task): SubTask {
        while (true) {
            write("Enter sub-task ID:")
            val input = readUUID()
            val subTask = task.subTasks.find { it.id == input }
            if (subTask != null) return subTask
            write("❌ Invalid sub-task ID. Please try again.")
        }
    }
}