import io.mockk.every
import io.mockk.spyk
import logic.models.State
import logic.usecases.EditStateUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*


class EditStateUseCaseTest {
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setup() {
        editStateUseCase = spyk(EditStateUseCase())
    }

    @Test
    fun `invoke should return same state when validation passes`() {
        // Given
        val validState = State(
            id = UUID.randomUUID(),
            title = "Valid Title",
            projectId = UUID.randomUUID()
        )
        mockPrivateMethod("isProjectExist", validState.projectId, true)

        // When
        val result = editStateUseCase(validState)

        // Then
        assertEquals(validState, result)
    }

    @Test
    fun `invoke should throw when title is blank`() {
        // Given
        val invalidState = State(
            id = UUID.randomUUID(),
            title = "",
            projectId = UUID.randomUUID()
        )
        mockPrivateMethod("isProjectExist", invalidState.projectId, true)

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            editStateUseCase(invalidState)
        }
        assertEquals("State title cannot be blank", exception.message)
    }

    @Test
    fun `invoke should throw when project doesn't exist`() {
        // Given
        val state = State(
            id = UUID.randomUUID(),
            title = "Valid Title",
            projectId = UUID.randomUUID()
        )
        mockPrivateMethod("isProjectExist", state.projectId, false)

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            editStateUseCase(state)
        }
        assertEquals("State does not belong to a valid project", exception.message)
    }

    @Test
    fun `isValidState should pass with valid state`() {
        // Given
        val validState = State(
            id = UUID.randomUUID(),
            title = "Valid Title",
            projectId = UUID.randomUUID()
        )
        mockPrivateMethod("isProjectExist", validState.projectId, true)

        // When & Then
        val isValidState = editStateUseCase.javaClass.getDeclaredMethod("isValidState", State::class.java)
        isValidState.isAccessible = true
        isValidState.invoke(editStateUseCase, validState)
    }

    @Test
    fun `validateTitle should throw for blank title`() {
        // When & Then
        val validateTitle = editStateUseCase.javaClass.getDeclaredMethod("validateTitle", String::class.java)
        validateTitle.isAccessible = true

        val exception = assertThrows<IllegalArgumentException> {
            validateTitle.invoke(editStateUseCase, "")
        }
        assertEquals("State title cannot be blank", exception.message)
    }

    @Test
    fun `validateTitle should pass for non-blank title`() {
        // When & Then
        val validateTitle = editStateUseCase.javaClass.getDeclaredMethod("validateTitle", String::class.java)
        validateTitle.isAccessible = true
        validateTitle.invoke(editStateUseCase, "Valid Title")
    }

    @Test
    fun `validateProject should throw for non-existent project`() {
        // Given
        val projectId = UUID.randomUUID()
        mockPrivateMethod("isProjectExist", projectId, false)

        // When & Then
        val validateProject = editStateUseCase.javaClass.getDeclaredMethod("validateProject", UUID::class.java)
        validateProject.isAccessible = true

        val exception = assertThrows<IllegalArgumentException> {
            validateProject.invoke(editStateUseCase, projectId)
        }
        assertEquals("State does not belong to a valid project", exception.message)
    }

    @Test
    fun `validateProject should pass for existing project`() {
        // Given
        val projectId = UUID.randomUUID()
        mockPrivateMethod("isProjectExist", projectId, true)

        // When & Then
        val validateProject = editStateUseCase.javaClass.getDeclaredMethod("validateProject", UUID::class.java)
        validateProject.isAccessible = true
        validateProject.invoke(editStateUseCase, projectId)
    }

    @Test
    fun `isProjectExist should return false by default`() {
        // When
        val isProjectExist = editStateUseCase.javaClass.getDeclaredMethod("isProjectExist", UUID::class.java)
        isProjectExist.isAccessible = true

        // Given
        val result = isProjectExist.invoke(editStateUseCase, UUID.randomUUID()) as Boolean

        // Then
        assertFalse(result)
    }

    private fun mockPrivateMethod(methodName: String, arg: Any, returnValue: Any) {
        every { editStateUseCase[methodName](arg) } returns returnValue
    }
}

