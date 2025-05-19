package presentation.task

import domain.models.Task
import domain.models.TaskState
import domain.usecases.task.AddTaskUseCase
import domain.usecases.task_state.GetTaskStatesByProjectIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class CreateTaskUITest {
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var getTaskStatesByProjectIdUseCase: GetTaskStatesByProjectIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var createTaskUI: CreateTaskUI

    private val projectId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()
    private val taskName = "Implement Feature"
    private val taskDescription = "Implement the new feature"

    @BeforeEach
    fun setUp() {
        addTaskUseCase = mockk(relaxed = true)
        getTaskStatesByProjectIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        createTaskUI = CreateTaskUI(addTaskUseCase, getTaskStatesByProjectIdUseCase, consoleIO)

        every { consoleIO.read() } returnsMany listOf(taskName, taskDescription, stateId.toString())

        coEvery { getTaskStatesByProjectIdUseCase(any()) } returns listOf(
            TaskState(id = stateId, name = "To Do", projectId = projectId)
        )
    }

    @Test
    fun `should create task successfully`() = runTest {
        // Given
        coEvery { addTaskUseCase(any()) } returns true

        // When
        createTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task name:")
            consoleIO.read() // Task name
            consoleIO.write("Please enter the task description:")
            consoleIO.read() // Task description
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("Available task states:")
            consoleIO.write("State ID: $stateId, State Name: To Do")
            consoleIO.write("Please enter the state ID:")
            consoleIO.read() // State ID
            addTaskUseCase(any())
            consoleIO.write("✅ Task created successfully.")
        }

        val taskSlot = slot<Task>()
        coVerify { addTaskUseCase(capture(taskSlot)) }

        with(taskSlot.captured) {
            assert(title == taskName)
            assert(description == taskDescription)
            assert(projectId == this@CreateTaskUITest.projectId)
            assert(stateId == this@CreateTaskUITest.stateId)
            assert(subTasks.isEmpty())
        }
    }

    @Test
    fun `should handle empty states list`() = runTest {
        // Given
        coEvery { getTaskStatesByProjectIdUseCase(any()) } returns emptyList()

        // When
        createTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task name:")
            consoleIO.read()
            consoleIO.write("Please enter the task description:")
            consoleIO.read()
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("ℹ️ No task states found for this project.")
        }

        coVerify(exactly = 0) {
            addTaskUseCase(any())
        }
    }

    @Test
    fun `should handle exception when fetching states`() = runTest {
        // Given
        val errorMessage = "Failed to fetch states"
        coEvery { getTaskStatesByProjectIdUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        createTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task name:")
            consoleIO.read()
            consoleIO.write("Please enter the task description:")
            consoleIO.read()
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("❌ Failed to fetch task states: $errorMessage")
        }

        coVerify(exactly = 0) {
            addTaskUseCase(any())
        }
    }

    @Test
    fun `should handle exception when adding task`() = runTest {
        // Given
        val errorMessage = "Failed to add task"
        coEvery { addTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        createTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task name:")
            consoleIO.read()
            consoleIO.write("Please enter the task description:")
            consoleIO.read()
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("Available task states:")
            consoleIO.write("State ID: $stateId, State Name: To Do")
            consoleIO.write("Please enter the state ID:")
            consoleIO.read()
            addTaskUseCase(any())
            consoleIO.write("❌ Failed to create task: $errorMessage")
        }
    }

    @Test
    fun `should create task with correct project ID`() = runTest {
        // Given
        val differentProjectId = UUID.randomUUID()
        val capturedTask = slot<Task>()
        coEvery { addTaskUseCase(capture(capturedTask)) } returns true

        // When
        createTaskUI.invoke(differentProjectId)

        // Then
        coVerify {
            getTaskStatesByProjectIdUseCase(differentProjectId)
            addTaskUseCase(any())
        }
        assert(capturedTask.captured.projectId == differentProjectId)
        assert(capturedTask.captured.title == taskName)
        assert(capturedTask.captured.description == taskDescription)
    }

    @Test
    fun `should handle invalid state ID input`() = runTest {
        // Given
        val invalidStateId = "invalid-uuid"
        every { consoleIO.read() } returnsMany listOf(taskName, taskDescription, invalidStateId)

        try {
            // When
            createTaskUI.invoke(projectId)

            assert(false) { "Expected exception was not thrown" }
        } catch (e: IllegalArgumentException) {
            // Then
            assert(e.message?.contains("invalid-uuid") == true)
        }

        coVerify {
            consoleIO.write("Please enter the task name:")
            consoleIO.read()
            consoleIO.write("Please enter the task description:")
            consoleIO.read()
            getTaskStatesByProjectIdUseCase(projectId)
            consoleIO.write("Available task states:")
            consoleIO.write("State ID: $stateId, State Name: To Do")
            consoleIO.write("Please enter the state ID:")
            consoleIO.read()
        }

        coVerify(exactly = 0) {
            addTaskUseCase(any())
        }
    }
}