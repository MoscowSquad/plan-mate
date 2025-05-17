package domain.usecases.task_state

import com.google.common.truth.Truth.assertThat
import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.NoStateExistException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTaskStateByIdUseCaseTest {
    private lateinit var stateRepository: TaskStateRepository
    private lateinit var useCase: GetTaskStateByIdUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        useCase = GetTaskStateByIdUseCase(stateRepository)
    }

    @Test
    fun `should throw NoStateExistException when state not found`() = runBlocking {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        coEvery { stateRepository.getTaskStateById(stateId) } throws NoStateExistException()

        // When/Then
        assertThrows<NoStateExistException> {
            runBlocking { useCase(stateId) }
        }

        coVerify(exactly = 1) { stateRepository.getTaskStateById(stateId) }
    }

    @Test
    fun `should return state when exists`() = runBlocking {
        // Given
        val stateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedState = TaskState(
            id = stateId,
            name = "To Do",
            projectId = UUID.randomUUID()
        )
        coEvery { stateRepository.getTaskStateById(stateId) } returns expectedState

        // When
        val result = useCase(stateId)

        // Then
        assertThat(result).isEqualTo(expectedState)
        coVerify(exactly = 1) { stateRepository.getTaskStateById(stateId) }
    }
}