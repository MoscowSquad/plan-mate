package presentation.state

import domain.usecases.task_state.DeleteTaskStateUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteTaskStateUITest {
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteTaskStateUI: DeleteTaskStateUI
    private val projectId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        deleteTaskStateUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        deleteTaskStateUI = DeleteTaskStateUI(deleteTaskStateUseCase, consoleIO)

        every { consoleIO.readUUID() } returns stateId
    }

    @Test
    fun `should delete task state successfully`() = runTest {
        // Given
        coEvery { deleteTaskStateUseCase(any(), any()) } returns true

        // When
        deleteTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            deleteTaskStateUseCase(stateId, projectId)
            consoleIO.write("✅ State deleted successfully.")
        }
    }

    @Test
    fun `should handle invalid state ID`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        deleteTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid state ID.")
        }

        coVerify(exactly = 0) {
            deleteTaskStateUseCase(any(), any())
        }
    }

    @Test
    fun `should handle exception when deleting state`() = runTest {
        // Given
        val errorMessage = "Failed to delete state"
        coEvery { deleteTaskStateUseCase(any(), any()) } throws RuntimeException(errorMessage)

        // When
        deleteTaskStateUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            deleteTaskStateUseCase(stateId, projectId)
            consoleIO.write("❌ Failed to delete state: $errorMessage")
        }
    }

    @Test
    fun `should delete task state with correct project ID`() = runTest {
        // Given
        val differentProjectId = UUID.randomUUID()
        val capturedStateId = slot<UUID>()
        val capturedProjectId = slot<UUID>()
        coEvery { deleteTaskStateUseCase(capture(capturedStateId), capture(capturedProjectId)) } returns true

        // When
        deleteTaskStateUI.invoke(differentProjectId)

        // Then
        coVerify {
            deleteTaskStateUseCase(any(), any())
        }
        assert(capturedStateId.captured == stateId)
        assert(capturedProjectId.captured == differentProjectId)
    }

    @Test
    fun `should pass correct parameters to use case`() = runTest {
        // Given
        val differentStateId = UUID.randomUUID()
        every { consoleIO.readUUID() } returns differentStateId

        // When
        deleteTaskStateUI.invoke(projectId)

        // Then
        coVerify {
            deleteTaskStateUseCase(differentStateId, projectId)
        }
    }
}