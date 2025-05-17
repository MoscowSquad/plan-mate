package domain.usecases.task_state

import com.google.common.truth.Truth.assertThat
import domain.models.TaskState
import domain.repositories.TaskStateRepository
import domain.util.IllegalStateTitle
import domain.util.NoStateExistException
import domain.util.NotAdminException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditTaskStateUseCaseTest {
    private lateinit var editStateUseCase: EditTaskStateUseCase
    private lateinit var stateRepository: TaskStateRepository

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        editStateUseCase = EditTaskStateUseCase(stateRepository)
    }

    @Test
    fun `when valid title should return updated TaskState`() = runBlocking {
        // Given
        val updatedState = TaskState(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            name = "Updated TaskState",
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        coEvery { stateRepository.updateTaskState(updatedState) } returns true

        // When
        val result = editStateUseCase(updatedState, true)

        // Then
        assertThat(result).isEqualTo(updatedState)
        coVerify(exactly = 1) { stateRepository.updateTaskState(updatedState) }
    }

    @Test
    fun `when title is blank should throw IllegalStateTitle`() = runBlocking {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            runBlocking { editStateUseCase(invalidState, true) }
        }
        coVerify(exactly = 0) { stateRepository.updateTaskState(any()) }
    }

    @Test
    fun `when state not found should throw NoStateExistException`(): Unit = runBlocking {
        // Given
        val state = TaskState(
            id = UUID.randomUUID(),
            name = "Valid",
            projectId = UUID.randomUUID()
        )

        coEvery { stateRepository.updateTaskState(state) } returns false

        // When & Then
        assertThrows<NoStateExistException> {
            runBlocking { editStateUseCase(state, true) }
        }
    }

    @Test
    fun `when user is not admin should throw NotAdminException`() = runBlocking {
        // Given
        val state = TaskState(
            id = UUID.randomUUID(),
            name = "Valid",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<NotAdminException> {
            runBlocking { editStateUseCase(state, false) }
        }
        coVerify(exactly = 0) { stateRepository.updateTaskState(any()) }
    }
}