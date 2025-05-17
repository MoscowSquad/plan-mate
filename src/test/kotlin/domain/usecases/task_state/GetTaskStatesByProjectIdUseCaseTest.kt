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

class GetTaskStatesByProjectIdUseCaseTest {

    private lateinit var stateRepository: TaskStateRepository
    private lateinit var useCase: GetTaskStatesByProjectIdUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        useCase = GetTaskStatesByProjectIdUseCase(stateRepository)
    }

    @Test
    fun `should throw NoStateExistException when project has no states`() = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { stateRepository.getTaskStateByProjectId(projectId) } throws NoStateExistException()

        // When/Then
        assertThrows<NoStateExistException> {
            runBlocking { useCase(projectId) }
        }

        coVerify(exactly = 1) { stateRepository.getTaskStateByProjectId(projectId) }
    }

    @Test
    fun `should return states when they exist`() = runBlocking {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val expectedStates = listOf(
            TaskState(UUID.randomUUID(), "To Do", projectId),
            TaskState(UUID.randomUUID(), "In Progress", projectId)
        )
        coEvery { stateRepository.getTaskStateByProjectId(projectId) } returns expectedStates

        // When
        val result = useCase(projectId)

        // Then
        assertThat(result).isEqualTo(expectedStates)
        coVerify(exactly = 1) { stateRepository.getTaskStateByProjectId(projectId) }
    }
}