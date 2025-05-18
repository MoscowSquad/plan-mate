package presentation.sub_task

import data.mongodb_data.mappers.toUUID
import domain.usecases.project.GetProjectByIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import presentation.task.EditTaskUI
import presentation.task.GetAllTasksUI

class SubTaskUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val createSubTaskUI: CreateSubTaskUI,
    private val deleteSubTaskUI: DeleteSubTaskUI,
    private val getAllTasksUI: GetAllTasksUI,
    private val editTaskUI: EditTaskUI,
    private val getAllProjectsUI: GetAllProjectsUI,
    private val consoleIO: ConsoleIO,
    private val getSubTasksByTaskIdUI: GetSubTasksByTaskIdUI
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
        1️⃣ - Create Sub Task
        2️⃣ - Edit Sub Task
        3️⃣ - Delete Sub Task
        4️⃣ - Get Sub Task By Id
        5️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createSubTaskUI()
            "2" -> editTaskUI(projectId)
            "3" -> deleteSubTaskUI()
            "4" -> getSubTasksByTaskIdUI()
            "5" -> write("Navigating back...")

            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}