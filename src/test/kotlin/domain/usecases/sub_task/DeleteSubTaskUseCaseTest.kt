package domain.usecases.sub_task

import domain.repositories.SubTaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteSubTaskUseCaseTest {
    private lateinit var deleteSubTaskUseCase: DeleteSubTaskUseCase
    private lateinit var subTaskRepository: SubTaskRepository

    @BeforeEach
    fun setup() {
        subTaskRepository = mockk()
        deleteSubTaskUseCase = DeleteSubTaskUseCase(subTaskRepository)
    }

    @Test
    fun `should return true when delete subtask without any issue`() = runTest {
        // Given
        val subTaskId = UUID.randomUUID()
        coEvery { subTaskRepository.deleteSubTask(subTaskId) } returns true

        // When
        val result = deleteSubTaskUseCase(subTaskId)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { subTaskRepository.deleteSubTask(subTaskId) }
    }

    @Test
    fun `should return false when repository fails to delete subtask`() = runTest {
        // Given
        val subTaskId = UUID.randomUUID()
        coEvery { subTaskRepository.deleteSubTask(subTaskId) } returns false

        // When
        val result = deleteSubTaskUseCase(subTaskId)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { subTaskRepository.deleteSubTask(subTaskId) }
    }

    @Test
    fun `should pass correct UUID to repository`() = runTest {
        // Given
        val subTaskId = UUID.randomUUID()
        coEvery { subTaskRepository.deleteSubTask(any()) } returns true

        // When
        deleteSubTaskUseCase(subTaskId)

        // Then
        coVerify {
            subTaskRepository.deleteSubTask(match {
                it == subTaskId
            })
        }
    }
}