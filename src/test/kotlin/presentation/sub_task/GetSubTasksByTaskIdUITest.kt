package presentation.sub_task

import domain.models.SubTask
import domain.usecases.sub_task.GetSubTasksByTaskIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetSubTasksByTaskIdUITest {
    private lateinit var getSubTasksByTaskIdUseCase: GetSubTasksByTaskIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getSubTasksByTaskIdUI: GetSubTasksByTaskIdUI
    private val taskId = UUID.randomUUID()
    private val subTaskId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        getSubTasksByTaskIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getSubTasksByTaskIdUI = GetSubTasksByTaskIdUI(getSubTasksByTaskIdUseCase, consoleIO)

        every { consoleIO.readUUID() } returns taskId
    }

    @Test
    fun `should fetch and display sub tasks successfully`() = runTest {
        // Given
        val subTasks = listOf(
            SubTask(
                id = subTaskId,
                title = "Test SubTask",
                description = "Test Description",
                isCompleted = false,
                parentTaskId = taskId
            )
        )
        coEvery { getSubTasksByTaskIdUseCase(taskId) } returns subTasks

        // When
        getSubTasksByTaskIdUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task id:")
            consoleIO.readUUID()
            getSubTasksByTaskIdUseCase(taskId)
            consoleIO.write(match { it.contains("Sub Task Name: Test SubTask") &&
                                    it.contains("Sub Description: Test Description") &&
                                    it.contains("Sub Task ID: $subTaskId") &&
                                    it.contains("Sub Task State ID: false") &&
                                    it.contains("Task ID: $taskId") })
        }
    }

    @Test
    fun `should display message when no sub tasks are found`() = runTest {
        // Given
        coEvery { getSubTasksByTaskIdUseCase(taskId) } returns emptyList()

        // When
        getSubTasksByTaskIdUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task id:")
            consoleIO.readUUID()
            getSubTasksByTaskIdUseCase(taskId)
            consoleIO.write("ℹ️ No sub tasks found for this Task.")
        }
    }

    @Test
    fun `should handle invalid task id input`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        getSubTasksByTaskIdUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task id:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid task sub task.")
        }
        coVerify(exactly = 0) { getSubTasksByTaskIdUseCase(any()) }
    }

    @Test
    fun `should handle exception when fetching sub tasks`() = runTest {
        // Given
        val errorMessage = "Failed to fetch sub tasks"
        coEvery { getSubTasksByTaskIdUseCase(taskId) } throws RuntimeException(errorMessage)

        // When
        getSubTasksByTaskIdUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter task id:")
            consoleIO.readUUID()
            getSubTasksByTaskIdUseCase(taskId)
            consoleIO.write("❌ Failed to fetch sub tasks: $errorMessage")
        }
    }

    @Test
    fun `should display multiple sub tasks correctly`() = runTest {
        // Given
        val subTasks = listOf(
            SubTask(
                id = UUID.randomUUID(),
                title = "First SubTask",
                description = "First Description",
                isCompleted = false,
                parentTaskId = taskId
            ),
            SubTask(
                id = UUID.randomUUID(),
                title = "Second SubTask",
                description = "Second Description",
                isCompleted = true,
                parentTaskId = taskId
            )
        )
        coEvery { getSubTasksByTaskIdUseCase(taskId) } returns subTasks

        // When
        getSubTasksByTaskIdUI.invoke()

        // Then
        coVerify {
            consoleIO.write("Please enter task id:")
            consoleIO.readUUID()
            getSubTasksByTaskIdUseCase(taskId)
            consoleIO.write(match { it.contains("Sub Task Name: First SubTask") })
            consoleIO.write(match { it.contains("Sub Task Name: Second SubTask") })
        }
    }
}