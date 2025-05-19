package presentation.state

import domain.models.Project
import domain.usecases.project.GetProjectByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import java.util.*

class TaskStateUITest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var createTaskStateUI: CreateTaskStateUI
    private lateinit var getAllTaskStatesUI: GetAllTaskStatesUI
    private lateinit var editTaskStateUI: EditTaskStateUI
    private lateinit var deleteTaskStateUI: DeleteTaskStateUI
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var taskStateUI: TaskStateUI

    private val projectId = UUID.randomUUID()
    private val validProjectIdString = projectId.toString()
    private val invalidProjectIdString = "invalid-uuid"

    @BeforeEach
    fun setUp() {
        getProjectByIdUseCase = mockk()
        createTaskStateUI = mockk(relaxed = true)
        getAllTaskStatesUI = mockk(relaxed = true)
        editTaskStateUI = mockk(relaxed = true)
        deleteTaskStateUI = mockk(relaxed = true)
        getAllProjectsUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        taskStateUI = TaskStateUI(
            getProjectByIdUseCase,
            createTaskStateUI,
            getAllTaskStatesUI,
            editTaskStateUI,
            deleteTaskStateUI,
            getAllProjectsUI,
            consoleIO
        )

        // Mock the extension function without using mockkStatic
        mockkStatic(UUID::class)
        every { UUID.fromString(validProjectIdString) } returns projectId
        every { UUID.fromString(invalidProjectIdString) } throws IllegalArgumentException("Invalid UUID")
    }

    @Test
    fun `should handle non-existent project`() = runTest {
        // Given
        every { consoleIO.read() } returns validProjectIdString
        coEvery { getProjectByIdUseCase(projectId) } throws RuntimeException("Project not found")

        // When
        taskStateUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            consoleIO.write("❌ No project exists. Add a new project to manage tasks. Please try again")
        }
    }

    @Test
    fun `should navigate to create state`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "1")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()

        // When
        taskStateUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI()
            consoleIO.write("Enter the project ID:")
            consoleIO.read()
            getProjectByIdUseCase(projectId)
            getAllTaskStatesUI(projectId)
            consoleIO.write(any<String>()) // Menu options
            consoleIO.read()
            createTaskStateUI(projectId)
        }
    }

    @Test
    fun `should navigate to edit state`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "2")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()

        // When
        taskStateUI.invoke()

        // Then
        coVerify {
            editTaskStateUI(projectId)
        }
    }

    @Test
    fun `should navigate to delete state`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "3")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()

        // When
        taskStateUI.invoke()

        // Then
        coVerify {
            deleteTaskStateUI(projectId)
        }
    }

    @Test
    fun `should navigate back`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(validProjectIdString, "4")
        coEvery { getProjectByIdUseCase(projectId) } returns mockk<Project>()

        // When
        taskStateUI.invoke()

        // Then
        verify {
            consoleIO.write("Navigating back...")
        }
    }
}