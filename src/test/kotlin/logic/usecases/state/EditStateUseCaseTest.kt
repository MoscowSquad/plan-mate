package logic.usecases.state

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.TaskState
import logic.repositoies.StateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.IllegalStateTitle
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
    fun `when valid title should return updated TaskState `() {
        // Given
        val originalState = TaskState(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
            title = "Old TaskState"
        )
        val newTask = TaskState(originalState.id, "Updated TaskState", originalState.projectId)

        every { stateRepository.update(any()) } returns true

        // When
        val result = editStateUseCase(originalState)

        // Then
        assertThat(result).isEqualTo(
            newTask
        )

        verify { stateRepository.update(newTask) }
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
        assertThat("TaskState title cannot be blank").isEqualTo(exception)

    }


}

