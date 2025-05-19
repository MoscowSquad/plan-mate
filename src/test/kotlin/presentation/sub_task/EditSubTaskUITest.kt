package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.UpdateSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class EditSubTaskUITest {
    private lateinit var updateSubTaskUseCase: UpdateSubTaskUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var editSubTaskUI: EditSubTaskUI

    private val taskId = UUID.randomUUID()
    private val subTaskId = UUID.randomUUID()
    private val subTaskTitle = "Test SubTask"
    private val subTaskDescription = "Test Description"
    private val subTaskIsCompleted = false

    private val newSubTaskTitle = "Updated SubTask"
    private val newSubTaskDescription = "Updated Description"
    private val newSubTaskIsCompleted = true

    private lateinit var subTask: SubTask
    private lateinit var task: Task

    @BeforeEach
    fun setUp() {
        updateSubTaskUseCase = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        editSubTaskUI = EditSubTaskUI(updateSubTaskUseCase, editTaskUseCase, consoleIO)

        subTask = SubTask(
            id = subTaskId,
            title = subTaskTitle,
            description = subTaskDescription,
            isCompleted = subTaskIsCompleted,
            parentTaskId = taskId
        )

        task = Task(
            id = taskId,
            title = "Task Title",
            description = "Task Description",
            projectId = UUID.randomUUID(),
            stateId = UUID.randomUUID(),
            subTasks = listOf(subTask)
        )
    }

    @Test
    fun `should show warning when no sub-tasks available`() = runTest {
        // Given
        val emptyTask = task.copy(subTasks = emptyList())

        // When
        editSubTaskUI.invoke(emptyTask)

        // Then
        verify {
            consoleIO.write("⚠️ No sub-tasks available to edit.")
        }
    }

    @Test
    fun `should edit sub-task successfully`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(
            subTaskId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            newSubTaskIsCompleted.toString(),
            "yes"
        )
        every { consoleIO.readUUID() } returns subTaskId

        coEvery { updateSubTaskUseCase.invoke(ofType(SubTask::class)) } returns true
        coEvery { editTaskUseCase.invoke(ofType(Task::class)) } returns true

        // When
        editSubTaskUI.invoke(task)

        // Then
        coVerify {
            updateSubTaskUseCase.invoke(ofType(SubTask::class))
            editTaskUseCase.invoke(ofType(Task::class))
        }

        verify {
            consoleIO.write(match { it.contains("Available Sub-Tasks for Task") })
            consoleIO.write("Enter sub-task ID:")
            consoleIO.write("Enter new title [Test SubTask]: (press Enter to keep current value)")
            consoleIO.write("Enter new description [Test Description]: (press Enter to keep current value)")
            consoleIO.write("Is the task completed? (true/false) [false]: (press Enter to keep current value)")
            consoleIO.write("Do you want to update the sub-task? (yes/no):")
            consoleIO.write("✅ Sub-task updated successfully.")
            consoleIO.write("✅ Task updated successfully.")
        }
    }

    @Test
    fun `should handle invalid sub-task ID`() = runTest {
        // Given
        val invalidId = UUID.randomUUID()
        val validId = subTaskId

        every { consoleIO.readUUID() } returnsMany listOf(invalidId, validId)
        every { consoleIO.read() } returnsMany listOf(
            invalidId.toString(),
            validId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            newSubTaskIsCompleted.toString(),
            "yes"
        )

        // When
        editSubTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("❌ Invalid sub-task ID. Please try again.")
        }
    }

    @Test
    fun `should handle invalid boolean input`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns subTaskId
        every { consoleIO.read() } returnsMany listOf(
            subTaskId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            "invalid", // Invalid boolean
            "true",    // Correct value
            "yes"
        )

        // When
        editSubTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("❌ Please enter 'true' or 'false'.")
        }
    }

    @Test
    fun `should cancel update when user declines`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns subTaskId
        every { consoleIO.read() } returnsMany listOf(
            subTaskId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            newSubTaskIsCompleted.toString(),
            "no" // User declines update
        )

        // When
        editSubTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("❌ Update cancelled.")
        }

        coVerify(exactly = 0) {
            updateSubTaskUseCase.invoke(ofType(SubTask::class))
            editTaskUseCase.invoke(ofType(Task::class))
        }
    }

    @Test
    fun `should handle exception when updating sub-task`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns subTaskId
        every { consoleIO.read() } returnsMany listOf(
            subTaskId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            newSubTaskIsCompleted.toString(),
            "yes"
        )

        val errorMessage = "Failed to update sub-task"
        coEvery { updateSubTaskUseCase.invoke(ofType(SubTask::class)) } throws RuntimeException(errorMessage)

        // When
        editSubTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("❌ Failed to update sub-task: $errorMessage")
        }

        coVerify(exactly = 0) {
            editTaskUseCase.invoke(ofType(Task::class))
        }
    }

    @Test
    fun `should handle exception when updating task`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns subTaskId
        every { consoleIO.read() } returnsMany listOf(
            subTaskId.toString(),
            newSubTaskTitle,
            newSubTaskDescription,
            newSubTaskIsCompleted.toString(),
            "yes"
        )

        val errorMessage = "Failed to update task"
        coEvery { updateSubTaskUseCase.invoke(ofType(SubTask::class)) } returns true
        coEvery { editTaskUseCase.invoke(ofType(Task::class)) } throws RuntimeException(errorMessage)

        // When
        editSubTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("✅ Sub-task updated successfully.")
            consoleIO.write("❌ Failed to update task: $errorMessage")
        }
    }
}