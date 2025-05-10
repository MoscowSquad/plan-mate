package presentation.project

import logic.models.TaskState
import logic.models.UserRole
import logic.usecases.project.CreateProjectUseCase
import logic.usecases.task_state.AddTaskStateUseCase
import presentation.io.ConsoleIO
import presentation.session.SessionManager
import java.util.*

class CreateProjectUI(
    private val createProjectUseCase: CreateProjectUseCase,
    private val addTaskStateUseCase: AddTaskStateUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    operator fun invoke() {
        write("Enter project name:")
        val projectName = read()


        runCatching {
            val projectId = createProjectUseCase(
                name = projectName,
                isAdmin = SessionManager.currentUser?.role == UserRole.ADMIN
            )
            val defaultStates = listOf(
                TaskState(
                    id = UUID.randomUUID(),
                    name = "Todo",
                    projectId = projectId
                ),
                TaskState(
                    id = UUID.randomUUID(),
                    name = "In Progress",
                    projectId = projectId
                ),
                TaskState(
                    id = UUID.randomUUID(),
                    name = "Done",
                    projectId = projectId
                )
            )

            defaultStates.forEach { addTaskStateUseCase(it) }
        }
            .onSuccess {
                write("✅Project created successfully!")
            }
            .onFailure {
                write("❌Error creating project: ${it.message}")
            }
    }
}