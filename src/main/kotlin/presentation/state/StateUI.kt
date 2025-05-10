package presentation.state

import data.mongodb_data.mappers.toUUID
import logic.usecases.project.GetProjectByIdUseCase
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI

class StateUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val createStateUI: CreateStateUI,
    private val getAllStatesUI: GetAllStatesUI,
    private val editStateUI: EditStateUI,
    private val deleteStateUI: DeleteStateUI,
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

        getAllStatesUI(projectId)

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
            "1" -> createStateUI(projectId)
            "2" -> editStateUI(projectId)
            "3" -> deleteStateUI(projectId)
            "4" -> write("Navigating back...")
            else -> {
                write("❌ Invalid option.")
                invoke()
            }
        }
    }
}