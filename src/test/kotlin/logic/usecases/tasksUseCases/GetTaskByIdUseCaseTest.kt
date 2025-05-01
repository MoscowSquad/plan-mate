package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.PropertyNullException
import utilities.TaskIsNotFoundException

class GetTaskByIdUseCaseTest {
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)
    }

    @Test
    fun `should return task model when found task successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        val input = 1234
        every { tasksRepository.getAll() } returns tasks

        val expected = Task(id = 1234, title = "Videos1")

        // When
        val result = getTaskByIdUseCase.getTaskById(input)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw TaskIsNotFoundException when wanted task not found`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks
        val input = 456

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            getTaskByIdUseCase.getTaskById(input)
        }
    }

    @Test
    fun `should Throw PropertyNullException when ID is null`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks
        val input = null

        // When & Then
        assertThrows<PropertyNullException> {
            getTaskByIdUseCase.getTaskById(input)
        }
    }

    @Test
    fun `should Throw TaskIsNotFoundException when there is no tasks`() {
        // Given
        every { tasksRepository.getAll() } returns emptyList()
        val input = 456

        // When & Then
        assertThrows<TaskIsNotFoundException> {
            getTaskByIdUseCase.getTaskById(input)
        }
    }
}