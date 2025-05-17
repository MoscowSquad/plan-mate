package presentation.task

import data.mongodb_data.mappers.toUUID
import domain.usecases.project.GetProjectByIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import presentation.sub_task.CalculatSubTaskPercentageUI
import presentation.sub_task.CreateSubTaskUI
import presentation.sub_task.SubTaskUI

class TasksUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteTaskUI: DeleteTaskUI,
    private val createTaskUI: CreateTaskUI,
    private val getAllTasksUI: GetAllTasksUI,
    private val editTaskUI: EditTaskUI,
    private val subTaskUI: SubTaskUI,
    private val getAllProjectsUI: GetAllProjectsUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
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
        4️⃣ - Create Sub Task
        5️⃣ - Calculate Completed Task Percentage
        6️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createTaskUI(projectId)
            "2" -> editTaskUI(projectId)
            "3" -> deleteTaskUI()
            "4" -> subTaskUI()
            "5" -> CalculatSubTaskPercentageUI()
            "6" -> write("Navigating back...")
            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}