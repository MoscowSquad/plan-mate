package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return list of Tasks when task deleted successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        val inputTask = 1234
        every { tasksRepository.getAll() } returns tasks

        val expected = listOf(
            Task(id = 12345, title = "Videos2")
        )

        // When
        val result = deleteTaskUseCase.deleteTask(inputTask)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw exception when task ID is null during deleting `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks
        val input = null
        // When & Then
        assertThrows<Exception> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw exception when wanted Id not found `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks
        val input = 456
        // When & Then
        assertThrows<Exception> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw exception when there is no tasks found to delete`() {
        // Given
        every { tasksRepository.getAll() } returns emptyList()
        val input = 1234
        // When & Then
        assertThrows<Exception> {
            deleteTaskUseCase.deleteTask(input)
        }
    }

    @Test
    fun `should Throw exception when no task is removed in deleteTask`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1111, title = "Not matching")
        )
        every { tasksRepository.getAll() } returns tasks

        // When & Then
        assertThrows<Exception> {
            deleteTaskUseCase.deleteTask(1234)
        }
    }
}