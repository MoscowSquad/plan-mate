package logic.usecases.tasksUseCases

import io.mockk.every
import io.mockk.mockk
import logic.models.Task
import logic.repositoies.TasksRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteAllTasksUseCaseTest {
    private lateinit var deleteAllTasksUseCase: DeleteAllTasksUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        deleteAllTasksUseCase = DeleteAllTasksUseCase(tasksRepository)
    }

    @Test
    fun `should return true when all tasks deleted successfully `() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks

        val expected = true
        val isAdmin = true
        // When
        val result = deleteAllTasksUseCase.deleteAllTasks(isAdmin)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should Throw exception when there is no tasks found `() {
        // Given
        every { tasksRepository.getAll() } returns emptyList()
        val isAdmin = true
        // When & Then
        assertThrows<Exception> {
            deleteAllTasksUseCase.deleteAllTasks(isAdmin)
        }
    }

    @Test
    fun `should return exception when user is not admin`() {
        // Given
        val tasks: List<Task> = listOf(
            Task(id = 1234, title = "Videos1"),
            Task(id = 12345, title = "Videos2")
        )
        every { tasksRepository.getAll() } returns tasks
        val isAdmin = false
        // When & Then
        assertThrows<Exception> {
            deleteAllTasksUseCase.deleteAllTasks(isAdmin)
        }
    }
}