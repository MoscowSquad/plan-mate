package presentation.task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.task.GetTaskByProjectIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetAllTasksUITest {
    private lateinit var getTaskByProjectIdUseCase: GetTaskByProjectIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllTasksUI: GetAllTasksUI
    private val projectId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()
    private val subTaskId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        getTaskByProjectIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getAllTasksUI = GetAllTasksUI(getTaskByProjectIdUseCase, consoleIO)
    }

    @Test
    fun `should display tasks when they exist`() = runTest {
        // Given
        val subTask = SubTask(
            id = subTaskId,
            title = "Subtask 1",
            description = "Subtask description",
            isCompleted = false,
            parentTaskId = taskId
        )

        val task = Task(
            id = taskId,
            title = "Sample Task",
            description = "Task description",
            projectId = projectId,
            stateId = stateId,
            subTasks = listOf(subTask)
        )

        coEvery { getTaskByProjectIdUseCase(projectId) } returns listOf(task)

        // When
        getAllTasksUI.invoke(projectId)

        // Then
        coVerify {
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write(match {
                it.contains("Sample Task") &&
                        it.contains("Task description") &&
                        it.contains(taskId.toString()) &&
                        it.contains(stateId.toString()) &&
                        it.contains("Subtask 1") &&
                        it.contains("Pending")
            })
        }
    }

    @Test
    fun `should display message when no tasks exist`() = runTest {
        // Given
        coEvery { getTaskByProjectIdUseCase(projectId) } returns emptyList()

        // When
        getAllTasksUI.invoke(projectId)

        // Then
        coVerify {
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write("ℹ️ No tasks found for this project.")
        }
    }

    @Test
    fun `should handle exception when fetching tasks`() = runTest {
        // Given
        val errorMessage = "Failed to fetch tasks"
        coEvery { getTaskByProjectIdUseCase(projectId) } throws RuntimeException(errorMessage)

        // When
        getAllTasksUI.invoke(projectId)

        // Then
        coVerify {
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write("❌ Failed to fetch tasks: $errorMessage")
        }
    }

    @Test
    fun `should display multiple tasks correctly`() = runTest {
        // Given
        val task1 = Task(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "First task",
            projectId = projectId,
            stateId = stateId,
            subTasks = emptyList()
        )

        val task2 = Task(
            id = UUID.randomUUID(),
            title = "Task 2",
            description = "Second task",
            projectId = projectId,
            stateId = stateId,
            subTasks = emptyList()
        )

        coEvery { getTaskByProjectIdUseCase(projectId) } returns listOf(task1, task2)

        // When
        getAllTasksUI.invoke(projectId)

        // Then
        coVerify {
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write(match { it.contains("Task 1") && it.contains("First task") })
            consoleIO.write(match { it.contains("Task 2") && it.contains("Second task") })
        }
    }

    @Test
    fun `should display completed subtasks correctly`() = runTest {
        // Given
        val completedSubTask = SubTask(
            id = UUID.randomUUID(),
            title = "Completed Subtask",
            description = "Completed subtask description",
            isCompleted = true,
            parentTaskId = taskId
        )

        val pendingSubTask = SubTask(
            id = UUID.randomUUID(),
            title = "Pending Subtask",
            description = "Pending subtask description",
            isCompleted = false,
            parentTaskId = taskId
        )

        val task = Task(
            id = taskId,
            title = "Task with subtasks",
            description = "Description",
            projectId = projectId,
            stateId = stateId,
            subTasks = listOf(completedSubTask, pendingSubTask)
        )

        coEvery { getTaskByProjectIdUseCase(projectId) } returns listOf(task)

        // When
        getAllTasksUI.invoke(projectId)

        // Then
        coVerify {
            getTaskByProjectIdUseCase(projectId)
            consoleIO.write(match {
                it.contains("Completed Subtask") &&
                        it.contains("Pending Subtask") &&
                        it.contains("Completed") &&
                        it.contains("Pending")
            })
        }
    }
}