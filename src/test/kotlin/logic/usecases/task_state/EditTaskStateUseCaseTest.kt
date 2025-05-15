package logic.usecases.task_state

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.TaskStateRepository
import logic.util.IllegalStateTitle
import logic.util.NoStateExistException
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
    fun `when valid title should return updated TaskState`() {
        // Given
        val updatedState = TaskState(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            name = "Updated TaskState",
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        every { stateRepository.updateTaskState(updatedState) } returns true

        // When
        val result = editStateUseCase(updatedState, true)

        // Then
        assertThat(result).isEqualTo(updatedState)
        verify(exactly = 1) { stateRepository.updateTaskState(updatedState) }
    }

    @Test
    fun `when title is blank should throw IllegalStateTitle`() {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            name = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        assertThrows<IllegalStateTitle> {
            editStateUseCase(invalidState, true)
        }
        verify(exactly = 0) { stateRepository.updateTaskState(any()) }
    }

    @Test
    fun `when state not found should throw NoStateExistException`() {
        // Given
        val state = TaskState(
            id = UUID.randomUUID(),
            name = "Valid",
            projectId = UUID.randomUUID()
        )

        every { stateRepository.updateTaskState(state) } returns false

        // When & Then
        assertThrows<NoStateExistException> {
            editStateUseCase(state, true)
        }
    }

}

