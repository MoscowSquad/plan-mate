package presentation.task

import data.mongodb_data.mappers.toUUID
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task_state.GetTaskStatesByProjectIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import presentation.sub_task.CalculateSubTaskPercentageUI

class TasksUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase,
    private val deleteTaskUI: DeleteTaskUI,
    private val createTaskUI: CreateTaskUI,
    private val getAllTasksUI: GetAllTasksUI,
    private val editTaskUI: EditTaskUI,
    private val calculateSubTaskPercentageUI: CalculateSubTaskPercentageUI,
    private val getAllProjectsUI: GetAllProjectsUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        getAllProjectsUI()
        write("Enter the project ID:")
        val projectId = runCatching { read().trimIndent().toUUID() }.getOrElse {
            write("❌ Invalid project ID format. Please enter a valid UUID.")
            return
        }

        if (runCatching { getProjectByIdUseCase(projectId) }.isFailure) {
            write("❌ No project exists. Add a new project to manage tasks. Please try again.")
            return
        }

        val statesExist = runCatching { getTaskStatesByProjectIdUseCase(projectId).isNotEmpty() }.getOrElse {
            write("❌ Error checking states: ${it.message}")
            false
        }

        if (!statesExist) {
            write("❌ No states exist for this project. Please add at least one state before managing tasks.")
            return
        }

        getAllTasksUI(projectId)

        write(
            """
        Select an operation:
        1️⃣ - Create Task
        2️⃣ - Edit Task / Manage Sub-Tasks
        3️⃣ - Delete Task
        4️⃣ - Calculate Completed Task Percentage
        5️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createTaskUI(projectId)
            "2" -> editTaskUI(projectId)
            "3" -> deleteTaskUI()
            "4" -> calculateSubTaskPercentageUI()
            "5" -> write("Navigating back...")
            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}