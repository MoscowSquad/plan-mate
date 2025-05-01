import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.State
import logic.repositoies.StateRepository
import logic.usecases.EditStateUseCase
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
    fun `when valid title should return updated state `() {
        // Given
        val originalState = State(
            id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
            projectId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
            title = "Old State"
        )
        val newTitle = "Updated State"

        every { stateRepository.updateStateTitle(any() ,any(), any()) } returns true

        // When
        val result = editStateUseCase(originalState, newTitle)

        // Then
        assertThat(result).isEqualTo(
            State(
                id = originalState.id,
                projectId = originalState.projectId,
                title = newTitle
            )
        )

        verify {
            stateRepository.updateStateTitle(
                projectId = originalState.projectId,
                stateId = originalState.id,
                newTitle
            )
        }
    }

    @Test
    fun `when title is blank should throw IllegalStateTitle`() {
        // Given
        val invalidState = State(
            id = UUID.randomUUID(),
            title = "",
            projectId = UUID.randomUUID()
        )

        // When & Then
        val exception = assertThrows<IllegalStateTitle> {
            editStateUseCase(invalidState, invalidState.title)
        }
        assertThat("State title cannot be blank").isEqualTo(exception)

    }



}

