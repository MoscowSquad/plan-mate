package logic.usecases.state

import org.junit.jupiter.api.Assertions.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.StateRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.NoStateExistException
import java.util.*

class GetStateByIdUseCaseTest {
    private val stateRepository = mockk<StateRepository>()
    private val useCase = GetStateByIdUseCase(stateRepository)

    @Test
    fun `should throw NoStateExistException when state not found`() {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        every { stateRepository.getById(stateId) } throws NoStateExistException("State with ID $stateId does not exist")

        // When/Then
        val exception = assertThrows<NoStateExistException> {
            useCase(stateId)
        }

        assertEquals("State with ID $stateId does not exist", exception.message)
        verify(exactly = 1) { stateRepository.getById(stateId) }
    }

    @Test
    fun `should return state when exists`() {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedState = TaskState(stateId, "To Do", UUID.randomUUID())
        every { stateRepository.getById(stateId) } returns expectedState

        // When
        val result = useCase(stateId)

        // Then
        assertEquals(expectedState, result)
        verify(exactly = 1) { stateRepository.getById(stateId) }
    }
}