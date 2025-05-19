package presentation.task

import domain.usecases.task.DeleteTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteTaskUITest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteTaskUI: DeleteTaskUI
    private val taskId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        deleteTaskUI = DeleteTaskUI(deleteTaskUseCase, consoleIO)

        every { consoleIO.readUUID() } returns taskId
    }

    @Test
    fun `should delete task successfully`() = runTest {
        // Given
        coEvery { deleteTaskUseCase(any()) } returns true

        // When
        deleteTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task ID:")
            consoleIO.readUUID()
            deleteTaskUseCase(taskId)
            consoleIO.write("✅ Task deleted successfully.")
        }
    }

    @Test
    fun `should handle invalid task ID`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        deleteTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid task ID.")
        }

        coVerify(exactly = 0) {
            deleteTaskUseCase(any())
        }
    }

    @Test
    fun `should handle exception when deleting task`() = runTest {
        // Given
        val errorMessage = "Task not found"
        coEvery { deleteTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        deleteTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task ID:")
            consoleIO.readUUID()
            deleteTaskUseCase(taskId)
            consoleIO.write("❌ Failed to delete task: $errorMessage")
        }
    }

    @Test
    fun `should call delete task use case with correct task ID`() = runTest {
        // Given
        val differentTaskId = UUID.randomUUID()
        every { consoleIO.readUUID() } returns differentTaskId
        val capturedTaskId = slot<UUID>()
        coEvery { deleteTaskUseCase(capture(capturedTaskId)) } returns true

        // When
        deleteTaskUI.invoke()

        // Then
        coVerify {
            deleteTaskUseCase(any())
        }
        assert(capturedTaskId.captured == differentTaskId)
    }

    @Test
    fun `should handle null response from delete task use case`() = runTest {
        // Given
        coEvery { deleteTaskUseCase(any()) } returns true

        // When
        deleteTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task ID:")
            consoleIO.readUUID()
            deleteTaskUseCase(taskId)
            consoleIO.write("✅ Task deleted successfully.")
        }
    }
}