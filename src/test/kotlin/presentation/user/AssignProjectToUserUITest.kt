package presentation.user

import di.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.UserRole
import logic.usecases.user.AssignProjectToUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class AssignProjectToUserUITest {
    private lateinit var assignProjectToUserUseCase: AssignProjectToUserUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var consoleIO: ConsoleIO
    private lateinit var assignProjectToUserUI: AssignProjectToUserUI
    private val currentUserRole = UserRole.ADMIN
    private val validProjectIdString = "123e4567-e89b-12d3-a456-426614174000"
    private val validProjectId = UUID.fromString(validProjectIdString)
    private val validUserIdString = "223e4567-e89b-12d3-a456-426614174001"
    private val validUserId = UUID.fromString(validUserIdString)

    @BeforeEach
    fun setUp() {
        assignProjectToUserUseCase = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        every { sessionManager.getCurrentUserRole() } returns currentUserRole

        assignProjectToUserUI = AssignProjectToUserUI(
            assignProjectToUserUseCase,
            sessionManager,
            consoleIO
        )
    }

    @Test
    fun `should assign project to user successfully with valid UUIDs`() {
        // Given
        every { consoleIO.read() } returns validProjectIdString andThen validUserIdString
        every { assignProjectToUserUseCase(currentUserRole, validProjectId, validUserId) } returns true

        // When
        assignProjectToUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            assignProjectToUserUseCase(currentUserRole, validProjectId, validUserId)
            consoleIO.write("User successfully assigned to the project.")
        }
    }

    @Test
    fun `should handle failed assignment`() {
        // Given
        val errorMessage = "Permission denied"
        every { consoleIO.read() } returns validProjectIdString andThen validUserIdString
        every { assignProjectToUserUseCase(currentUserRole, validProjectId, validUserId) } throws
                IllegalStateException(errorMessage)

        // When
        assignProjectToUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            assignProjectToUserUseCase(currentUserRole, validProjectId, validUserId)
            consoleIO.write("Failed to assign user. $errorMessage")
        }
    }

    @Test
    fun `should handle null user role from session manager`() {
        // Given
        every { consoleIO.read() } returns validProjectIdString andThen validUserIdString
        every { sessionManager.getCurrentUserRole() } returns null

        // When
        assignProjectToUserUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            consoleIO.write("User successfully assigned to the project.")
        }

        verify(exactly = 0) {
            assignProjectToUserUseCase(any(), any(), any())
        }
    }
}