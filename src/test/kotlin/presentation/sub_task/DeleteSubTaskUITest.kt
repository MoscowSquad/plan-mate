package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.DeleteSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteSubTaskUITest {
    private lateinit var deleteSubTaskUseCase: DeleteSubTaskUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteSubTaskUI: DeleteSubTaskUI

    private val taskId = UUID.randomUUID()
    private val subTaskId1 = UUID.randomUUID()
    private val subTaskId2 = UUID.randomUUID()

    private val subTask1 = SubTask(id = subTaskId1, title = "SubTask 1", description = "", isCompleted = false, parentTaskId = taskId)
    private val subTask2 = SubTask(id = subTaskId2, title = "SubTask 2", description = "", isCompleted = true, parentTaskId = taskId)
    private lateinit var task: Task

    @BeforeEach
    fun setUp() {
        deleteSubTaskUseCase = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        deleteSubTaskUI = DeleteSubTaskUI(deleteSubTaskUseCase, editTaskUseCase, consoleIO)

        task = Task(
            id = taskId,
            title = "Test Task",
            description = "Description",
            projectId = UUID.randomUUID(),
            stateId = UUID.randomUUID(),
            subTasks = listOf(subTask1, subTask2)
        )

        every { consoleIO.readUUID() } returns subTaskId1
    }

    @Test
    fun `should delete sub-task successfully`() = runTest {
        // Given
        coEvery { deleteSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(any()) } returns true

        // When
        deleteSubTaskUI.invoke(task)

        // Then
        val expectedUpdatedTask = task.copy(
            subTasks = listOf(subTask2)
        )

        coVerifySequence {
            consoleIO.write(any())
            consoleIO.write("Enter sub-task ID:")
            consoleIO.readUUID()
            deleteSubTaskUseCase(subTaskId1)
            consoleIO.write("✅ Sub Task deleted successfully.")
            editTaskUseCase(expectedUpdatedTask)
            consoleIO.write("✅ Task updated successfully.")
        }
    }

    @Test
    fun `should notify when there are no sub-tasks to edit`() = runTest {
        // Given
        val emptyTask = task.copy(subTasks = emptyList())

        // When
        deleteSubTaskUI.invoke(emptyTask)

        // Then
        verify(exactly = 1) {
            consoleIO.write("⚠️ No sub-tasks available to edit.")
        }
        coVerify(exactly = 0) {
            deleteSubTaskUseCase(any())
            editTaskUseCase(any())
        }
    }

    @Test
    fun `should continue prompting until a valid sub-task ID is entered`() = runTest {
        // Given
        val invalidId = UUID.randomUUID()
        every { consoleIO.readUUID() } returnsMany listOf(invalidId, invalidId, subTaskId1)
        coEvery { deleteSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(any()) } returns true

        // When
        deleteSubTaskUI.invoke(task)

        // Then
        verifySequence {
            consoleIO.write(any()) // Initial instructions
            consoleIO.write("Enter sub-task ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid sub-task ID. Please try again.")
            consoleIO.write("Enter sub-task ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid sub-task ID. Please try again.")
            consoleIO.write("Enter sub-task ID:")
            consoleIO.readUUID()
            consoleIO.write("✅ Sub Task deleted successfully.")
            consoleIO.write("✅ Task updated successfully.")
        }
        coVerify {
            deleteSubTaskUseCase(subTaskId1)
        }
    }

    @Test
    fun `should handle exception when deleting sub-task`() = runTest {
        // Given
        val errorMessage = "Failed to delete sub-task"
        coEvery { deleteSubTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        deleteSubTaskUI.invoke(task)

        // Then
        coVerify {
            deleteSubTaskUseCase(subTaskId1)
            consoleIO.write("❌ Failed to delete Sub task: $errorMessage")
        }
        coVerify(exactly = 0) {
            editTaskUseCase(any())
        }
    }

    @Test
    fun `should handle exception when updating task`() = runTest {
        // Given
        val errorMessage = "Failed to update task"
        coEvery { deleteSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        deleteSubTaskUI.invoke(task)

        // Then
        coVerify {
            deleteSubTaskUseCase(subTaskId1)
            consoleIO.write("✅ Sub Task deleted successfully.")
            editTaskUseCase(any())
            consoleIO.write("❌ Failed to update task: $errorMessage")
        }
    }
}