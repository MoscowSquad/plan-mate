package logic.usecases.task_state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.NoStateExistException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTaskStateByIdUseCaseTest {
    private val stateRepository = mockk<TaskStateRepository>()
    private val useCase = GetTaskStateByIdUseCase(stateRepository)

    @Test
    fun `should throw NoStateExistException when state not found`() {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        every { stateRepository.getTaskStateById(stateId) } throws NoStateExistException()

        // When/Then
        assertThrows<NoStateExistException> {
            useCase(stateId)
        }

        verify(exactly = 1) { stateRepository.getTaskStateById(stateId) }
    }

    @Test
    fun `should return state when exists`() {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedState = TaskState(stateId, "To Do", UUID.randomUUID())
        every { stateRepository.getTaskStateById(stateId) } returns expectedState

        // When
        val result = useCase(stateId)

        // Then
        assertEquals(expectedState, result)
        verify(exactly = 1) { stateRepository.getTaskStateById(stateId) }
    }
}