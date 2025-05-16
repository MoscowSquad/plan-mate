package presentation.task

import data.mongodb_data.mappers.toUUID
import domain.models.Task
import domain.usecases.task.AddTaskUseCase
import domain.usecases.task_state.GetTaskStatesByProjectIdUseCase
import presentation.io.ConsoleIO
import java.util.*

class CreateTaskUI(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    suspend operator fun invoke(projectId: UUID) {
        write("Please enter the task name:")
        val taskName = read()

        write("Please enter the task description:")
        val taskDescription = read()

        lateinit var stateId: UUID
        runCatching { getTaskStatesByProjectIdUseCase(projectId) }
            .onSuccess { states ->
                if (states.isEmpty()) {
                    write("ℹ️ No task states found for this project.")
                    return
                } else {
                    write("Available task states:")
                    states.forEach { state ->
                        write("State ID: ${state.id}, State Name: ${state.name}")
                    }
                    write("Please enter the state ID:")
                    stateId = read().toUUID()
                }
            }
            .onFailure {
                write("❌ Failed to fetch task states: ${it.message}")
                return
            }


        val task = Task(
            id = UUID.randomUUID(),
            title = taskName,
            description = taskDescription,
            projectId = projectId,
            stateId = stateId
        )

        runCatching { addTaskUseCase(task) }
            .onSuccess { write("✅ Task created successfully.") }
            .onFailure { write("❌ Failed to create task: ${it.message}") }
    }
}