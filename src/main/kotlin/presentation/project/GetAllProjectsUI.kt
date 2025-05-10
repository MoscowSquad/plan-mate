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
                    write("No projects found.")
                } else {
                    write("Projects:")
                    projects.forEach { project ->
                        write(
                            """
                               |-------------------------------------------------------------------|
                               | "Project ID: ${project.id}                    |
                               |  Name: ${project.name}"                                               |
                               |-------------------------------------------------------------------|
                                """)
                    }
                }
            }
            .onFailure { exception ->
                write("Error retrieving projects: ${exception.message}")
            }
    }
}