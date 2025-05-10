package presentation.task

import data.mongodb_data.mappers.toUUID
import logic.usecases.project.GetProjectByIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI

class TasksUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteTaskUI: DeleteTaskUI,
    private val createTaskUI: CreateTaskUI,
    private val getAllTasksUI: GetAllTasksUI,
    private val editTaskUI: EditTaskUI,
    private val getAllProjectsUI: GetAllProjectsUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        getAllProjectsUI()
        write("Enter the project ID:")
        val projectId = runCatching { read().trimIndent().toUUID() }.getOrElse {
            write("❌ Invalid project ID format. Please enter a valid UUID.")
            invoke()
            return
        }

        if (runCatching { getProjectByIdUseCase(projectId) }.isFailure) {
            write("❌ No project exists. Add a new project to manage tasks. Please try again")
            return
        }

        getAllTasksUI(projectId)

        write(
            """
        Select an operation:
        1️⃣ - Create Task
        2️⃣ - Edit Task
        3️⃣ - Delete Task
        4️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createTaskUI(projectId)
            "2" -> editTaskUI(projectId)
            "3" -> deleteTaskUI()
            "4" -> write("Navigating back...")
            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}