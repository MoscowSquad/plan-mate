package presentation.sub_task

import domain.models.Task
import domain.models.SubTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class SubTaskUITest {
    private lateinit var createSubTaskUI: CreateSubTaskUI
    private lateinit var deleteSubTaskUI: DeleteSubTaskUI
    private lateinit var editSubTaskUI: EditSubTaskUI
    private lateinit var getSubTasksByTaskIdUI: GetSubTasksByTaskIdUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var subTaskUI: SubTaskUI
    private lateinit var task: Task

    @BeforeEach
    fun setUp() {
        createSubTaskUI = mockk(relaxed = true)
        deleteSubTaskUI = mockk(relaxed = true)
        editSubTaskUI = mockk(relaxed = true)
        getSubTasksByTaskIdUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        subTaskUI = SubTaskUI(
            createSubTaskUI,
            deleteSubTaskUI,
            editSubTaskUI,
            getSubTasksByTaskIdUI,
            consoleIO
        )

        task = Task(
            id = UUID.randomUUID(),
            title = "Test Task",
            description = "Test Description",
            projectId = UUID.randomUUID(),
            stateId = UUID.randomUUID(),
            subTasks = listOf(
                SubTask(
                    id = UUID.randomUUID(),
                    title = "Test SubTask",
                    description = "Test SubTask Description",
                    isCompleted = false,
                    parentTaskId = UUID.randomUUID()
                )
            )
        )
    }

    @Test
    fun `should display task details correctly`() = runTest {
        // Given
        every { consoleIO.read() } returns "5"

        // When
        subTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write(match { it.contains("Task Details") && it.contains(task.title) })
            consoleIO.write(match { it.contains("Select an operation:") })
        }
    }

    @Test
    fun `should navigate to create sub task when option 1 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        subTaskUI.invoke(task)

        // Then
        coVerify {
            createSubTaskUI.invoke(task)
        }
    }

    @Test
    fun `should navigate to edit sub task when option 2 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        subTaskUI.invoke(task)

        // Then
        coVerify {
            editSubTaskUI.invoke(task)
        }
    }

    @Test
    fun `should navigate to delete sub task when option 3 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        subTaskUI.invoke(task)

        // Then
        coVerify {
            deleteSubTaskUI.invoke(task)
        }
    }

    @Test
    fun `should navigate to get sub tasks when option 4 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "4"

        // When
        subTaskUI.invoke(task)

        // Then
        coVerify {
            getSubTasksByTaskIdUI.invoke()
        }
    }

    @Test
    fun `should navigate back when option 5 is selected`() = runTest {
        // Given
        every { consoleIO.read() } returns "5"

        // When
        subTaskUI.invoke(task)

        // Then
        verify {
            consoleIO.write("Navigating back...")
        }
    }

    @Test
    fun `should show error message and retry when invalid option is selected`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "5")

        // When
        subTaskUI.invoke(task)

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("❌ Invalid option.")
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Navigating back...")
        }
    }

    @Test
    fun `should display completed and pending sub-tasks correctly`() = runTest {
        // Given
        val completedSubTask = SubTask(
            id = UUID.randomUUID(),
            title = "Completed SubTask",
            description = "Completed SubTask Description",
            isCompleted = true,
            parentTaskId = task.id
        )
        val pendingSubTask = SubTask(
            id = UUID.randomUUID(),
            title = "Pending SubTask",
            description = "Pending SubTask Description",
            isCompleted = false,
            parentTaskId = task.id
        )

        val taskWithMixedSubTasks = task.copy(
            subTasks = listOf(completedSubTask, pendingSubTask)
        )

        every { consoleIO.read() } returns "5"

        // When
        subTaskUI.invoke(taskWithMixedSubTasks)

        // Then
        verify {
            consoleIO.write(match {
                it.contains("Completed") &&
                it.contains("Pending") &&
                it.contains(completedSubTask.title) &&
                it.contains(pendingSubTask.title)
            })
        }
    }
}