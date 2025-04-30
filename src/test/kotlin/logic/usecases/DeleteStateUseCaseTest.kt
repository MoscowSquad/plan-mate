package logic.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utilities.StateNotExistException
import java.util.*
import kotlin.test.Test

class DeleteStateUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = false) // Use strict mocking
        deleteStateUseCase = DeleteStateUseCase(projectsRepository)
    }

    @Test
    fun `delete use case should return true when state is successfully deleted`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        every { projectsRepository.deleteState(projectId, stateId) } returns true

        // When
        val result = deleteStateUseCase(projectId, stateId)

        // Then
        assertTrue(result)
        verify(exactly = 1) { projectsRepository.deleteState(projectId, stateId) }
    }

    @Test
    fun `delete use case should throw StateNotExistException when state not found`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")

        every { projectsRepository.deleteState(projectId, stateId) } returns false

        // When/Then
        val exception = assertThrows<StateNotExistException> {
            deleteStateUseCase(projectId, stateId)
        }

        assertEquals("State with ID $stateId does not exist", exception.message)
        assertEquals(stateId, exception.message)
        verify(exactly = 1) { projectsRepository.deleteState(projectId, stateId) }
    }

    @Test
    fun `should propagate repository exceptions`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedError = IllegalStateException("Database error")

        every { projectsRepository.deleteState(projectId, stateId) } throws expectedError

        // When/Then
        val exception = assertThrows<IllegalStateException> {
            deleteStateUseCase(projectId, stateId)
        }

        assertEquals(expectedError, exception)
        verify(exactly = 1) { projectsRepository.deleteState(projectId, stateId) }
    }
}