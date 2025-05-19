package presentation.state

import domain.models.TaskState
import domain.usecases.task_state.EditTaskStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class EditTaskStateUITest {
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var editTaskStateUI: EditTaskStateUI
    private val projectId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()
    private val stateName = "In Progress"

    @BeforeEach
    fun setUp() {
        editTaskStateUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        editTaskStateUI = EditTaskStateUI(editTaskStateUseCase, consoleIO)

        every { consoleIO.readUUID() } returns stateId
        every { consoleIO.read() } returns stateName
    }

    @Test
    fun `should edit task state successfully`() = runTest {
        // Given
        coEvery { editTaskStateUseCase(any()) } returns TaskState(id = stateId, name = stateName, projectId = projectId)
        // When
        editTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new state name:")
            consoleIO.read()
            editTaskStateUseCase(
                TaskState(
                    id = stateId,
                    name = stateName,
                    projectId = projectId
                )
            )
            consoleIO.write("✅ State updated successfully.")
        }
    }

    @Test
    fun `should handle invalid state ID`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        editTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new state name:")
            consoleIO.read()
            consoleIO.write("❌ Invalid state ID.")
        }

        coVerify(exactly = 0) {
            editTaskStateUseCase(any())
        }
    }

    @Test
    fun `should handle empty state name`() = runTest {
        // Given
        val emptyStateName = ""
        every { consoleIO.read() } returns emptyStateName

        // When
        editTaskStateUI.invoke(projectId)

        // Then
        coVerify {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new state name:")
            consoleIO.read()
            editTaskStateUseCase(
                TaskState(
                    id = stateId,
                    name = emptyStateName,
                    projectId = projectId
                )
            )
        }
    }

    @Test
    fun `should handle exception when editing state`() = runTest {
        // Given
        val errorMessage = "Failed to edit state"
        coEvery { editTaskStateUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        editTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new state name:")
            consoleIO.read()
            editTaskStateUseCase(any())
            consoleIO.write("❌ Failed to update state: $errorMessage")
        }
    }

    @Test
    fun `should edit task state with correct project ID`() = runTest {
        // Given
        val differentProjectId = UUID.randomUUID()
        val capturedState = slot<TaskState>()
        coEvery { editTaskStateUseCase(capture(capturedState)) } returns TaskState(
            id = stateId,
            name = stateName,
            projectId = differentProjectId
        )
        // When
        editTaskStateUI.invoke(differentProjectId)

        // Then
        coVerify {
            editTaskStateUseCase(any())
        }
        assert(capturedState.captured.projectId == differentProjectId)
        assert(capturedState.captured.id == stateId)
        assert(capturedState.captured.name == stateName)
    }

    @Test
    fun `should edit task state with long name`() = runTest {
        // Given
        val longName = "A".repeat(100)
        every { consoleIO.read() } returns longName

        // When
        editTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new state name:")
            consoleIO.read()
            editTaskStateUseCase(
                TaskState(
                    id = stateId,
                    name = longName,
                    projectId = projectId
                )
            )
            consoleIO.write("✅ State updated successfully.")
        }
    }
}