package logic.usecases.AddTaskUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.State
import logic.models.Task
import logic.repositoies.TasksRepository
import logic.usecases.tasksUseCases.AddTaskUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.PropertyNullException
import java.util.*

class AddTaskUseCaseTest {
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        addTaskUseCase = AddTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return list of Tasks when add task without any issue`() {
        // Given
        val id: UUID = mockk()
        val projectId: UUID = mockk()
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1", state = State(id, "TODO", projectId)),
            Task(id = 12345, title = "Videos2", state = State(id, "TODO", projectId))
        )
        val task = Task(id = 123456, title = "Videos3", state = State(id, "TODO", projectId))
        every { tasksRepository.getAll() } returns tasks

        val expected = listOf(
            Task(id = 1234, title = "Videos1", state = State(id, "TODO", projectId)),
            Task(id = 12345, title = "Videos2", state = State(id, "TODO", projectId)),
            Task(id = 123456, title = "Videos3", state = State(id, "TODO", projectId))
        )

        // When
        val result = addTaskUseCase.addTask(task)
        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw PropertyNullException when user title is null`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = 12345, title = null))
        }
    }

    @Test
    fun `should Throw PropertyNullException when state ID is null `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = null, title = "Videos3"))
        }
    }

    @Test
    fun `should Throw PropertyNullException when user state is null `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<PropertyNullException> {
            addTaskUseCase.addTask(Task(id = 12345, title = "Videos3", state = null))
        }
    }
}