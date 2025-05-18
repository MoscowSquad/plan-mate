package domain.usecases.task_state

import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.NoStateExistException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteTaskStateUseCaseTest {

    private lateinit var stateRepository: TaskStateRepository
    private lateinit var deleteStateUseCase: DeleteTaskStateUseCase

    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = false) // Use strict mocking
        deleteStateUseCase = DeleteTaskStateUseCase(stateRepository)
    }

    @Test
    fun `should return true when state is successfully deleted`() = runTest {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        coEvery { stateRepository.getTaskStateByProjectId(projectId) } returns listOf(
            TaskState(stateId, "Test", projectId)
        )
        coEvery { stateRepository.deleteTaskState(projectId, stateId) } returns true

        // When
        val result = deleteStateUseCase(stateId, projectId)

        // Then
        assertTrue(result)
        coVerifyOrder {
            stateRepository.getTaskStateByProjectId(projectId)
            stateRepository.deleteTaskState(projectId, stateId)
        }
    }

    @Test
    fun `should throw NoStateExistException when state not found`() = runTest {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        coEvery { stateRepository.getTaskStateByProjectId(projectId) } returns emptyList()

        // When/Then
        assertThrows<NoStateExistException> {
            deleteStateUseCase(stateId, projectId)
        }

        coVerify(exactly = 0) { stateRepository.deleteTaskState(any(), any()) }
    }

    @Test
    fun `should propagate repository exceptions`() = runTest {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedError = IllegalStateException("Database error")

        coEvery { stateRepository.getTaskStateByProjectId(projectId) } throws expectedError

        // When/Then
        assertThrows<IllegalStateException> {
            deleteStateUseCase(stateId, projectId)
        }

        coVerify(exactly = 0) { stateRepository.deleteTaskState(any(), any()) }
    }

    @Test
    fun `should throw IllegalStateException when deletion fails`() = runTest {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        coEvery { stateRepository.getTaskStateByProjectId(projectId) } returns listOf(
            TaskState(stateId, "Test", projectId)
        )
        coEvery { stateRepository.deleteTaskState(projectId, stateId) } returns false

        // When/Then
        assertThrows<IllegalStateException> {
            deleteStateUseCase(stateId, projectId)
        }

        coVerifyOrder {
            stateRepository.getTaskStateByProjectId(projectId)
            stateRepository.deleteTaskState(projectId, stateId)
        }
    }
}