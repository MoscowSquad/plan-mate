package presentation.project

import domain.usecases.project.CreateProjectUseCase
import presentation.io.ConsoleIO
import presentation.state.CreateStateUI

class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val createStateUI: CreateStateUI,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke() {
        write("Enter project name:")
        val projectName = read()

        val projectId = runCatching {
            createProjectUseCase(name = projectName)
        }.onSuccess {
            write("Project named $projectName with id $it created successfully!")
        }.onFailure {
            write("Error creating project: ${it.message}")
            return
        }.getOrNull() ?: return

        write("Would you like to add a state to the project now? (yes/no):")
        val addStateResponse = read().trim().lowercase()

        if (addStateResponse == "yes") {
            createStateUI(projectId)
        } else {
            write("⚠️ Warning: You won't be able to add tasks to this project unless you add at least one state.")
        }
    }
}