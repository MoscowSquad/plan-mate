package presentation.project

import logic.usecases.project.GetAllProjectsUseCase
import presentation.io.ConsoleIO

class GetAllProjectsUI(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val consoleIO: ConsoleIO,
) : ConsoleIO by consoleIO {

    operator fun invoke() {
        runCatching {
            getAllProjectsUseCase()
        }
            .onSuccess { projects ->
                if (projects.isEmpty()) {
                    consoleIO.write("No projects found.")
                } else {
                    consoleIO.write("Projects:")
                    projects.forEach { project ->
                        consoleIO.write("Project ID: ${project.id}, Name: ${project.name}")
                    }
                }
            }
            .onFailure { exception ->
                consoleIO.write("Error retrieving projects: ${exception.message}")
            }
    }
}