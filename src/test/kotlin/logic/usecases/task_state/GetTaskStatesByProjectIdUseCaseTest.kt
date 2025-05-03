package logic.usecases.task_state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.NoStateExistException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetTaskStatesByProjectIdUseCaseTest {

    private lateinit var stateRepository: TaskStateRepository
    private lateinit var useCase: GetTaskStatesByProjectIdUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        useCase = GetTaskStatesByProjectIdUseCase(stateRepository)
    }

    @Test
    fun `should throw NoStateExistException when project has no states`() {
        // Given
        val projectId = UUID.randomUUID()
        every { stateRepository.getTaskStateByProjectId(projectId) } throws NoStateExistException("No State Exist")

        // When/Then
        val exception = assertThrows<NoStateExistException> {
            useCase(projectId)
        }

        assertEquals("No State Exist", exception.message)
        verify(exactly = 1) { stateRepository.getTaskStateByProjectId(projectId) }
    }

    @Test
    fun `should return states with correct project ID`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val testStates = listOf(
            TaskState(UUID.randomUUID(), "To Do", projectId),
            TaskState(UUID.randomUUID(), "In Progress", projectId)
        )
        every { stateRepository.getTaskStateByProjectId(projectId) } returns testStates

        // When
        val result = useCase(projectId)

        // Then (Single assertion)
        assertTrue(result.all { it.projectId == projectId })  // Only checks project ID match
    }

}