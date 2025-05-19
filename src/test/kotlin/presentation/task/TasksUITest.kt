package presentation.task

import domain.models.Project
import domain.models.TaskState
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task_state.GetTaskStatesByProjectIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import presentation.sub_task.CalculateSubTaskPercentageUI
import java.util.*

class TasksUITest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase
    private lateinit var deleteTaskUI: DeleteTaskUI
    private lateinit var createTaskUI: CreateTaskUI
    private lateinit var getAllTasksUI: GetAllTasksUI
    private lateinit var editTaskUI: EditTaskUI
    private lateinit var calculateSubTaskPercentageUI: CalculateSubTaskPercentageUI
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var tasksUI: TasksUI

    private val projectId = UUID.randomUUID()
    private val validProjectIdString = projectId.toString()
    private val invalidProjectIdString = "invalid-uuid"

    @BeforeEach
    fun setUp() {
        getProjectByIdUseCase = mockk()
        getTaskStatesByProjectIdUseCase = mockk()
        deleteTaskUI = mockk(relaxed = true)
        createTaskUI = mockk(relaxed = true)
        getAllTasksUI = mockk(relaxed = true)
        editTaskUI = mockk(relaxed = true)
        calculateSubTaskPercentageUI = mockk(relaxed = true)
        getAllProjectsUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        tasksUI = TasksUI(
            getProjectByIdUseCase,
            getTaskStatesByProjectIdUseCase,
            deleteTaskUI,
            createTaskUI,
            getAllTasksUI,
            editTaskUI,
            calculateSubTaskPercentageUI,
            getAllProjectsUI,
            consoleIO
        )

        // Mock the extension function without using mockkStatic
        mockkStatic(UUID::class)
        every { UUID.fromString(validProjectIdString) } returns projectId
        every { UUID.fromString(invalidProjectIdString) } throws IllegalArgumentException("Invalid UUID")
    }

    @Test
    fun `should show all projects and handle invalid project ID input`() = runTest {
        // Given
        every { consoleIO.read() } returns invalidProjectIdString

        // When
        tasksUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            consoleIO.write("❌ Invalid project ID format. Please enter a valid UUID.")
        }
    }

    @Test
    fun `should handle non-existent project`() = runTest {
        // Given
        every { consoleIO.read() } returns validProjectIdString
        coEvery { getProjectByIdUseCase(projectId) } throws RuntimeException("Project not found")

        // When
        tasksUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            consoleIO.write("❌ No project exists. Add a new project to manage tasks. Please try again.")
        }
    }

    @Test
    fun `should handle project with no states`() = runTest {
        // Given
        every { consoleIO.read() } returns validProjectIdString
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns emptyList()

        // When
        tasksUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("❌ No states exist for this project. Please add at least one state before managing tasks.")
        }
    }

    @Test
    fun `should handle error checking states`() = runTest {
        // Given
        every { consoleIO.read() } returns validProjectIdString
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } throws RuntimeException("Error fetching states")

        // When
        tasksUI.invoke()

        // Then
        coVerify {
            consoleIO.write("❌ Error checking states: Error fetching states")
        }
    }

    @Test
    fun `should navigate to create task`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "1")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            getTaskStatesByProjectIdUseCase(projectId)
            getAllTasksUI(projectId)
            consoleIO.write(any<String>()) // Menu options
            consoleIO.read()
            createTaskUI(projectId)
        }
    }

    @Test
    fun `should navigate to edit task`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "2")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        coVerify {
            editTaskUI(projectId)
        }
    }

    @Test
    fun `should navigate to delete task`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "3")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        coVerify {
            deleteTaskUI()
        }
    }

    @Test
    fun `should navigate to calculate task percentage`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "4")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        coVerify {
            calculateSubTaskPercentageUI()
        }
    }

    @Test
    fun `should navigate back`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "5")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        verify {
            consoleIO.write("Navigating back...")
        }
    }

    @Test
    fun `should handle invalid menu option`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "invalid", "invalid-uuid")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()
        coEvery { getTaskStatesByProjectIdUseCase(projectId) } returns listOf(mockk<TaskState>())

        // When
        tasksUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            getTaskStatesByProjectIdUseCase(projectId)
            getAllTasksUI(projectId)
            consoleIO.write(any<String>()) // Menu options
            consoleIO.read()
            consoleIO.write("❌ Invalid option.")
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            consoleIO.write("❌ Invalid project ID format. Please enter a valid UUID.")
        }

        coVerify {
            tasksUI.invoke()
        }
    }
}