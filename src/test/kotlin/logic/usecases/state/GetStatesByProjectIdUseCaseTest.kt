package logic.usecases.state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositoies.StateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.NoExistProjectException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetStatesByProjectIdUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: GetStatesByProjectIdUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        useCase = GetStatesByProjectIdUseCase(stateRepository)
    }

    @Test
    fun `should throw NoExistProjectException when project has no states`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        every { stateRepository.getByProjectId(projectId) } returns emptyList()

        // When/Then
        val exception = assertThrows<NoExistProjectException> {
            useCase(projectId)
        }

        // Verify exception message and repository call
        assertEquals("Project '$projectId' does not exist", exception.message)
        verify(exactly = 1) { stateRepository.getByProjectId(projectId) }
    }

    @Test
    fun `should return states with correct project ID`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val testStates = listOf(
            TaskState(UUID.randomUUID(), "To Do", projectId),
            TaskState(UUID.randomUUID(), "In Progress", projectId)
        )
        every { stateRepository.getByProjectId(projectId) } returns testStates

        // When
        val result = useCase(projectId)

        // Then (Single assertion)
        assertTrue(result.all { it.projectId == projectId })  // Only checks project ID match
    }

}