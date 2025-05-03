package logic.usecases.state

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositories.StateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.IllegalStateTitle
import utilities.NoStateExistException
import java.util.*


class EditStateUseCaseTest {
    private lateinit var editStateUseCase: EditStateUseCase
    private lateinit var stateRepository: StateRepository

    @BeforeEach
    fun setup() {
        stateRepository = mockk()
        editStateUseCase = EditStateUseCase(stateRepository)
    }

    @Test
    fun `when valid title should return updated TaskState`() {
        // Given
        val updatedState = TaskState(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            title = "Updated TaskState",
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        every { stateRepository.update(updatedState) } returns true

        // When
        val result = editStateUseCase(updatedState)

        // Then
        assertThat(result).isEqualTo(updatedState)
        verify(exactly = 1) { stateRepository.update(updatedState) }
    }

    @Test
    fun `when title is blank should throw IllegalStateTitle`() {
        // Given
        val invalidState = TaskState(
            id = UUID.randomUUID(),
            title = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        val exception = assertThrows<IllegalStateTitle> {
            editStateUseCase(invalidState)
        }
        assertThat(exception.message).isEqualTo("TaskState title cannot be blank")
        verify(exactly = 0) { stateRepository.update(any()) }
    }

    @Test
    fun `when state not found should throw NoStateExistException`() {
        // Given
        val state = TaskState(
            id = UUID.randomUUID(),
            title = "Valid",
            projectId = UUID.randomUUID()
        )

        every { stateRepository.update(state) } returns false

        // When & Then
        val exception = assertThrows<NoStateExistException> {
            editStateUseCase(state)
        }
        assertThat(exception.message).contains("not found")
    }

}

