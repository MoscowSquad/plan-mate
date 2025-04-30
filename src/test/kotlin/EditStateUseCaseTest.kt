import com.google.common.truth.Truth.assertThat
import io.mockk.*
import logic.models.State
import logic.repositoies.ProjectsRepository
import logic.usecases.EditStateUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utilities.IllegalTitle
import java.util.*


class EditStateUseCaseTest {
    private lateinit var editStateUseCase: EditStateUseCase
    private lateinit var projectsRepository: ProjectsRepository

    @BeforeEach
    fun setup() {
        projectsRepository = mockk()
        editStateUseCase = EditStateUseCase(projectsRepository)
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

        every { projectsRepository.updateStateTitleForSpecificProjectById(any(), any()) }

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
            projectsRepository.updateStateTitleForSpecificProjectById(
                projectId = originalState.projectId,
                stateId = originalState.id
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
        val exception = assertThrows<IllegalTitle> {
            editStateUseCase(invalidState, invalidState.title)
        }
        assertEquals("State title cannot be blank", exception.message)

    }



}

