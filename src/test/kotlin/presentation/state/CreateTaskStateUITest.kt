package presentation.state

import domain.models.TaskState
import domain.usecases.task_state.AddTaskStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class CreateTaskStateUITest {
    private lateinit var addTaskStateUseCase: AddTaskStateUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var createTaskStateUI: CreateTaskStateUI
    private val projectId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()
    private val stateName = "To Do"

    @BeforeEach
    fun setUp() {
        addTaskStateUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        createTaskStateUI = CreateTaskStateUI(addTaskStateUseCase, consoleIO)

        every { consoleIO.read() } returns stateName

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns stateId
    }

    @Test
    fun `should create task state successfully`() = runTest {
        // Given
        coEvery { addTaskStateUseCase(any()) } returns true

        // When
        createTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state name:")
            consoleIO.read()
            addTaskStateUseCase(TaskState(
                id = stateId,
                name = stateName,
                projectId = projectId
            ))
            consoleIO.write("✅ State created successfully.")
        }
    }

    @Test
    fun `should handle empty state name`() = runTest {
        // Given
        val emptyStateName = ""
        every { consoleIO.read() } returns emptyStateName

        // When
        createTaskStateUI.invoke(projectId)

        // Then
        coVerify {
            consoleIO.write("Please enter the state name:")
            consoleIO.read()
            addTaskStateUseCase(TaskState(
                id = stateId,
                name = emptyStateName,
                projectId = projectId
            ))
        }
    }

    @Test
    fun `should handle exception when adding state`() = runTest {
        // Given
        val errorMessage = "Failed to add state"
        coEvery { addTaskStateUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        createTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state name:")
            consoleIO.read()
            addTaskStateUseCase(any())
            consoleIO.write("❌ Failed to create state: $errorMessage")
        }
    }

    @Test
    fun `should create task state with correct project ID`() = runTest {
        // Given
        val differentProjectId = UUID.randomUUID()
        val capturedState = slot<TaskState>()
        coEvery { addTaskStateUseCase(capture(capturedState)) } returns true

        // When
        createTaskStateUI.invoke(differentProjectId)

        // Then
        coVerify {
            addTaskStateUseCase(any())
        }
        assert(capturedState.captured.projectId == differentProjectId)
        assert(capturedState.captured.name == stateName)
    }

    @Test
    fun `should create task state with long name`() = runTest {
        // Given
        val longName = "A".repeat(100)
        every { consoleIO.read() } returns longName

        // When
        createTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state name:")
            consoleIO.read()
            addTaskStateUseCase(TaskState(
                id = stateId,
                name = longName,
                projectId = projectId
            ))
            consoleIO.write("✅ State created successfully.")
        }
    }
}