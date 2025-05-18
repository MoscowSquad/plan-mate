package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.UpdateSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import presentation.io.ConsoleIO

class EditSubTaskUI(
    private val updateSubTaskUseCase: UpdateSubTaskUseCase,
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
📋 Available Sub-Tasks for Task: "${task.title}"
────────────────────────────────────────────────────
${task.subTasks.joinToString(separator = "\n") { "🔹 ID: ${it.id} | Title: ${it.title}" }}
────────────────────────────────────────────────────
🔽 Please enter the ID of the sub-task you want to edit:
""".trimIndent()

        )

        val subTask = readValidSubTask(task)

        val newTitle = readNonBlankInput("Enter new title [${subTask.title}]:", subTask.title)
        val newDescription = readNonBlankInput("Enter new description [${subTask.description}]:", subTask.description)
        val isCompleted =
            readBooleanInput("Is the task completed? (true/false) [${subTask.isCompleted}]:", subTask.isCompleted)

        write("Do you want to update the sub-task? (yes/no):")
        val confirm = read().trim().lowercase()
        if (confirm != "yes") {
            write("❌ Update cancelled.")
            return
        }

        val updatedSubTask = subTask.copy(
            title = newTitle,
            description = newDescription,
            isCompleted = isCompleted
        )

        val updatedTask = task.copy(
            subTasks = task.subTasks.map { if (it.id == subTask.id) updatedSubTask else it }
        )

        runCatching { updateSubTaskUseCase(updatedSubTask) }
            .onSuccess {
                write("✅ Sub-task updated successfully.")
                runCatching { editTaskUseCase(updatedTask) }
                    .onSuccess { write("✅ Task updated successfully.") }
                    .onFailure { write("❌ Failed to update task: ${it.message}") }
            }
            .onFailure { write("❌ Failed to update sub-task: ${it.message}") }
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

    private fun readNonBlankInput(prompt: String, default: String): String {
        write("$prompt (press Enter to keep current value)")
        val input = read()
        return input.ifBlank { default }
    }

    private fun readBooleanInput(prompt: String, default: Boolean): Boolean {
        while (true) {
            write("$prompt (press Enter to keep current value)")
            val input = read().trim()
            if (input.isBlank()) return default
            if (input.equals("true", ignoreCase = true)) return true
            if (input.equals("false", ignoreCase = true)) return false
            write("❌ Please enter 'true' or 'false'.")
        }
    }
}
