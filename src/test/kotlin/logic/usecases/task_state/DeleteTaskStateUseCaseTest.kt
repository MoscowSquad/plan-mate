package logic.usecases.task_state

import di.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.models.TaskState
import logic.models.UserRole
import logic.repositories.TaskStateRepository
import logic.util.NoStateExistException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class DeleteTaskStateUseCaseTest {

    private lateinit var stateRepository: TaskStateRepository
    private lateinit var deleteStateUseCase: DeleteTaskStateUseCase

    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = false) // Use strict mocking
        deleteStateUseCase = DeleteTaskStateUseCase(stateRepository)
    }

    @Test
    fun `should return true when state is successfully deleted`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        every { stateRepository.getTaskStateByProjectId(projectId) } returns listOf(
            TaskState(stateId, "Test", projectId)
        )
        every { stateRepository.deleteTaskState(projectId, stateId) } returns true

        // When
        val result = deleteStateUseCase(stateId, projectId, true)

        // Then
        assertTrue(result)
        verifyOrder {
            stateRepository.getTaskStateByProjectId(projectId)
            stateRepository.deleteTaskState(projectId, stateId)
        }
    }

    @Test
    fun `should throw NoStateExistException when state not found`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        every { stateRepository.getTaskStateByProjectId(projectId) } returns emptyList()

        // When/Then
        assertThrows<NoStateExistException> {
            deleteStateUseCase(stateId, projectId, true)
        }

        verify(exactly = 0) { stateRepository.deleteTaskState(any(), any()) }
    }

    @Test
    fun `should propagate repository exceptions`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedError = IllegalStateException("Database error")

        every { stateRepository.getTaskStateByProjectId(projectId) } throws expectedError

        // When/Then
        assertThrows<IllegalStateException> {
            deleteStateUseCase(stateId, projectId, true)
        }

        verify(exactly = 0) { stateRepository.deleteTaskState(any(), any()) }
    }

    @Test
    fun `should throw IllegalStateException when deletion fails`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        every { stateRepository.getTaskStateByProjectId(projectId) } returns listOf(
            TaskState(stateId, "Test", projectId)
        )
        every { stateRepository.deleteTaskState(projectId, stateId) } returns false

        // When/Then
        assertThrows<IllegalStateException> {
            deleteStateUseCase(stateId, projectId, true)
        }

        verifyOrder {
            stateRepository.getTaskStateByProjectId(projectId)
            stateRepository.deleteTaskState(projectId, stateId)
        }
    }
}
