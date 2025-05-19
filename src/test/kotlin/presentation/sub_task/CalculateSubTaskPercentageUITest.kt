package presentation.sub_task

import domain.models.Task
import domain.usecases.task.GetTaskByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class CalculateSubTaskPercentageUITest {
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var calculateSubTaskPercentageUI: CalculateSubTaskPercentageUI
    private val taskId = UUID.randomUUID()
    private val completionPercentage = 75.0

    @BeforeEach
    fun setUp() {
        getTaskByIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        calculateSubTaskPercentageUI = CalculateSubTaskPercentageUI(getTaskByIdUseCase, consoleIO)
    }

    @Test
    fun `should calculate subtask percentage successfully`() = runTest {
        // Given
        val task = mockk<Task>()
        every { consoleIO.readUUID() } returns taskId
        coEvery { getTaskByIdUseCase(taskId) } returns task
        every { task.calculateSubTaskCompletionPercentage() } returns completionPercentage

        // When
        calculateSubTaskPercentageUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter Task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            task.calculateSubTaskCompletionPercentage()
            consoleIO.write("Task completed percentage: $completionPercentage")
        }
    }

    @Test
    fun `should handle invalid task ID input`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        calculateSubTaskPercentageUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Please enter Task ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid Task ID.")
        }

        coVerify(exactly = 0) {
            getTaskByIdUseCase(any())
        }
    }

    @Test
    fun `should handle task not found`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns taskId
        coEvery { getTaskByIdUseCase(taskId) } throws NoSuchElementException("Task not found")
        // When
        calculateSubTaskPercentageUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter Task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("❌ Failed to retrieve task: Task not found")
            consoleIO.write("❌ Task not found.")
        }

    }

    @Test
    fun `should handle exception when retrieving task`() = runTest {
        // Given
        val errorMessage = "Database connection failed"
        every { consoleIO.readUUID() } returns taskId
        coEvery { getTaskByIdUseCase(taskId) } throws RuntimeException(errorMessage)

        // When
        calculateSubTaskPercentageUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter Task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("❌ Failed to retrieve task: $errorMessage")
            consoleIO.write("❌ Task not found.")
        }
    }

    @Test
    fun `should display zero percent when task has no subtasks`() = runTest {
        // Given
        val task = mockk<Task>()
        every { consoleIO.readUUID() } returns taskId
        coEvery { getTaskByIdUseCase(taskId) } returns task
        every { task.calculateSubTaskCompletionPercentage() } returns 0.0

        // When
        calculateSubTaskPercentageUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Please enter Task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            task.calculateSubTaskCompletionPercentage()
            consoleIO.write("Task completed percentage: 0.0")
        }
    }
}