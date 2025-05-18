package presentation.task

import domain.usecases.task.GetTaskByProjectIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class GetAllTasksUI(
    private val getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        runCatching { getTaskByProjectIdUseCase(projectId) }
            .onSuccess { tasks ->
                if (tasks.isEmpty()) {
                    write("ℹ️ No tasks found for this project.")
                } else {
                    tasks.forEach { task ->
                        write(
                            """
╔════════════════════════════════════════════════════╗
║                   📝 Task Details                  ║
╠════════════════════════════════════════════════════╣
║ 🔹 Title       : ${task.title}
║ 🧾 Description : ${task.description}
║ 🆔 Task ID     : ${task.id}
║ 📌 State ID    : ${task.stateId}
║ 📁 Project ID  : ${task.projectId}
║ 📋 Sub-Tasks   :
${task.subTasks.joinToString(separator = "\n") { "║   • ${it.title} (ID: ${it.id}) (Status: ${if (it.isCompleted) "Completed" else "Pending"})" }}
╚════════════════════════════════════════════════════╝
""".trimIndent()

                        )
                    }
                }
            }
            .onFailure { write("❌ Failed to fetch tasks: ${it.message}") }
    }
}