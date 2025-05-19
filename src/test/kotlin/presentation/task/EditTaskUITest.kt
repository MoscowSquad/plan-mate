package presentation.task

import domain.models.Task
import domain.usecases.task.EditTaskUseCase
import domain.usecases.task.GetTaskByIdUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.sub_task.SubTaskUI
import java.util.*

class EditTaskUITest {
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var subTaskUI: SubTaskUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var editTaskUI: EditTaskUI

    private val projectId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()
    private val stateId = UUID.randomUUID()
    private val task = Task(
        id = taskId,
        title = "Original Task",
        description = "Original Description",
        projectId = projectId,
        stateId = UUID.randomUUID(),
        subTasks = emptyList()
    )

    @BeforeEach
    fun setUp() {
        getTaskByIdUseCase = mockk()
        editTaskUseCase = mockk(relaxed = true)
        subTaskUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        editTaskUI = EditTaskUI(getTaskByIdUseCase, editTaskUseCase, subTaskUI, consoleIO)

        every { consoleIO.read() } returns "1"
        every { consoleIO.readUUID() } returns taskId
        coEvery { getTaskByIdUseCase(any()) } returns task
    }

    @Test
    fun `should edit task details successfully`() = runTest {
        // Given
        val newTaskName = "New Task Name"
        val newTaskDescription = "New Task Description"

        every { consoleIO.read() } returnsMany listOf("1", newTaskName, newTaskDescription)
        every { consoleIO.readUUID() } returnsMany listOf(taskId, stateId)

        // When
        editTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new task name:")
            consoleIO.read()
            consoleIO.write("Please enter the new task description:")
            consoleIO.read()
            editTaskUseCase(withArg {
                assert(it.id == taskId)
                assert(it.title == newTaskName)
                assert(it.description == newTaskDescription)
                assert(it.stateId == stateId)
                assert(it.projectId == projectId)
            })
            consoleIO.write("✅ Task updated successfully.")
        }
    }

    @Test
    fun `should handle invalid task ID input`() = runTest {
        // Given
        every { consoleIO.readUUID() } returns null

        // When
        editTaskUI.invoke(projectId)

        // Then
        verifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            consoleIO.write("❌ Invalid task ID.")
        }

        coVerify(exactly = 0) { getTaskByIdUseCase(any()) }
    }

    @Test
    fun `should handle task not found`() = runTest {
        // Given
        coEvery { getTaskByIdUseCase(any()) } throws RuntimeException("Task not found")

        // When
        editTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("❌ Failed to retrieve task: Task not found")
            consoleIO.write("❌ Task not found.")
        }
    }

    @Test
    fun `should navigate to subtasks menu`() = runTest {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        editTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            subTaskUI(task)
        }
    }

    @Test
    fun `should navigate back when option 3 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        editTaskUI.invoke(projectId)

        // Then
        verifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            consoleIO.write("Navigating back...")
        }
    }

    @Test
    fun `should handle invalid menu option`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "3")

        // When
        editTaskUI.invoke(projectId)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            consoleIO.write("❌ Invalid choice.")
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            getTaskByIdUseCase(taskId)
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            consoleIO.write("Navigating back...")
        }
    }

    @Test
    fun `should handle invalid state ID when editing task`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", "New Name", "New Description")
        every { consoleIO.readUUID() } returnsMany listOf(taskId, null)

        // When
        editTaskUI.invoke(projectId)

        // Then
        verifySequence {
            consoleIO.write("Please enter the task ID:")
            consoleIO.readUUID()
            consoleIO.write("What do you want to edit?\n1. Task details\n2. Subtasks\n3. Back")
            consoleIO.read()
            consoleIO.write("Please enter the state ID:")
            consoleIO.readUUID()
            consoleIO.write("Please enter the new task name:")
            consoleIO.read()
            consoleIO.write("Please enter the new task description:")
            consoleIO.read()
            consoleIO.write("❌ Invalid task or state ID.")
        }
    }

    @Test
    fun `should handle exception when updating task`() = runTest {
        // Given
        val errorMessage = "Failed to update task"
        every { consoleIO.read() } returnsMany listOf("1", "New Name", "New Description")
        every { consoleIO.readUUID() } returnsMany listOf(taskId, stateId)
        coEvery { editTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        editTaskUI.invoke(projectId)

        // Then
        coVerify {
            consoleIO.write("❌ Failed to update task: $errorMessage")
        }
    }
}