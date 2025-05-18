package presentation.state

import data.mongodb_data.mappers.toUUID
import domain.usecases.project.GetProjectByIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI

class TaskStateUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val createTaskStateUI: CreateTaskStateUI,
    private val getAllTaskStatesUI: GetAllTaskStatesUI,
    private val editTaskStateUI: EditTaskStateUI,
    private val deleteTaskStateUI: DeleteTaskStateUI,
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

        getAllTaskStatesUI(projectId)

        write(
            """
        Select an operation:
        1️⃣ - Create State
        2️⃣ - Edit State
        3️⃣ - Delete State
        4️⃣ - Back
    """.trimIndent()
        )

        when (read().trim()) {
            "1" -> createTaskStateUI(projectId)
            "2" -> editTaskStateUI(projectId)
            "3" -> deleteTaskStateUI(projectId)
            "4" -> write("Navigating back...")
            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}