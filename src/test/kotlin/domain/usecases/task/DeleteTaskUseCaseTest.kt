package domain.usecases.task

import domain.repositories.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var tasksRepository: TasksRepository

    @BeforeEach
    fun setup() {
        tasksRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return true when task deleted successfully`() = runTest {
        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        coEvery { tasksRepository.deleteTask(id) } returns true

        // When
        val result = deleteTaskUseCase(id)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { tasksRepository.deleteTask(id) }
    }

    @Test
    fun `should return false when task is not deleted successfully`() = runTest {
        // Given
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        coEvery { tasksRepository.deleteTask(id) } returns false

        // When
        val result = deleteTaskUseCase(id)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { tasksRepository.deleteTask(id) }
    }

    @Test
    fun `should pass correct task ID to repository`() = runTest {
        // Given
        val id = UUID.randomUUID()
        coEvery { tasksRepository.deleteTask(any()) } returns true

        // When
        deleteTaskUseCase(id)

        // Then
        coVerify { tasksRepository.deleteTask(id) }
    }
}