package presentation.sub_task

import domain.models.SubTask
import domain.models.Task
import domain.usecases.sub_task.CreateSubTaskUseCase
import domain.usecases.task.EditTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class CreateSubTaskUITest {
    private lateinit var createSubTaskUseCase: CreateSubTaskUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var createSubTaskUI: CreateSubTaskUI

    private val taskId = UUID.randomUUID()
    private val subTaskId = UUID.randomUUID()
    private val taskTitle = "Main Task"
    private val subTaskTitle = "Sub Task Title"
    private val subTaskDescription = "Sub Task Description"
    private val task = Task(
        id = taskId,
        title = taskTitle,
        description = "Task Description",
        projectId = UUID.randomUUID(),
        stateId = UUID.randomUUID(),
        subTasks = emptyList()
    )

    @BeforeEach
    fun setUp() {
        createSubTaskUseCase = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        createSubTaskUI = CreateSubTaskUI(createSubTaskUseCase, editTaskUseCase, consoleIO)

        every { consoleIO.read() } returnsMany listOf(subTaskTitle, subTaskDescription)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns subTaskId
    }

    @Test
    fun `should create sub task successfully`() = runTest {
        // Given
        coEvery { createSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(any()) } returns true

        // When
        createSubTaskUI.invoke(task)

        // Then
        val expectedSubTask = SubTask(
            id = subTaskId,
            title = subTaskTitle,
            description = subTaskDescription,
            isCompleted = false,
            parentTaskId = taskId
        )

        val expectedUpdatedTask = task.copy(
            subTasks = task.subTasks + expectedSubTask
        )

        coVerifySequence {
            consoleIO.write("Please enter sub task title:")
            consoleIO.read()
            consoleIO.write("Please enter sub task description:")
            consoleIO.read()
            createSubTaskUseCase(expectedSubTask)
            consoleIO.write("✅ sub task added successfully.")
            editTaskUseCase(expectedUpdatedTask)
            consoleIO.write("✅ Task updated successfully.")
        }
    }

    @Test
    fun `should handle empty sub task title and description`() = runTest {
        // Given
        val emptyTitle = ""
        val emptyDescription = ""
        every { consoleIO.read() } returnsMany listOf(emptyTitle, emptyDescription)

        // When
        createSubTaskUI.invoke(task)

        // Then
        val expectedSubTask = SubTask(
            id = subTaskId,
            title = emptyTitle,
            description = emptyDescription,
            isCompleted = false,
            parentTaskId = taskId
        )

        coVerify {
            consoleIO.write("Please enter sub task title:")
            consoleIO.read()
            consoleIO.write("Please enter sub task description:")
            consoleIO.read()
            createSubTaskUseCase(expectedSubTask)
        }
    }

    @Test
    fun `should handle exception when creating sub task`() = runTest {
        // Given
        val errorMessage = "Failed to create sub task"
        coEvery { createSubTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        createSubTaskUI.invoke(task)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter sub task title:")
            consoleIO.read()
            consoleIO.write("Please enter sub task description:")
            consoleIO.read()
            createSubTaskUseCase(any())
            consoleIO.write("❌ failed to add sub  task: $errorMessage")
        }
    }

    @Test
    fun `should handle exception when updating task`() = runTest {
        // Given
        val errorMessage = "Failed to update task"
        coEvery { createSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(any()) } throws RuntimeException(errorMessage)

        // When
        createSubTaskUI.invoke(task)

        // Then
        coVerifySequence {
            consoleIO.write("Please enter sub task title:")
            consoleIO.read()
            consoleIO.write("Please enter sub task description:")
            consoleIO.read()
            createSubTaskUseCase(any())
            consoleIO.write("✅ sub task added successfully.")
            editTaskUseCase(any())
            consoleIO.write("❌ Failed to update task: $errorMessage")
        }
    }

    @Test
    fun `should add sub task to existing task with sub tasks`() = runTest {
        // Given
        val existingSubTask = SubTask(
            id = UUID.randomUUID(),
            title = "Existing SubTask",
            description = "Description",
            isCompleted = true,
            parentTaskId = taskId
        )

        val taskWithSubTasks = task.copy(subTasks = listOf(existingSubTask))
        val capturedTask = slot<Task>()

        coEvery { createSubTaskUseCase(any()) } returns true
        coEvery { editTaskUseCase(capture(capturedTask)) } returns true

        // When
        createSubTaskUI.invoke(taskWithSubTasks)

        // Then
        coVerify {
            createSubTaskUseCase(any())
            editTaskUseCase(any())
        }

        assert(capturedTask.captured.subTasks.size == 2)
        assert(capturedTask.captured.subTasks[0] == existingSubTask)
        assert(capturedTask.captured.subTasks[1].title == subTaskTitle)
        assert(capturedTask.captured.subTasks[1].description == subTaskDescription)
    }
}