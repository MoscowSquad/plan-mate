package presentation.user

import data.mongodb_data.mappers.toUUID
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.user.AssignProjectToUserUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.project.GetAllProjectsUI
import java.util.*

class AssignProjectToUserUITest {
    private lateinit var assignProjectToUserUseCase: AssignProjectToUserUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllProjectsUI: GetAllProjectsUI
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
        getAllProjectsUI = mockk(relaxed = true)

        mockkStatic("data.mongodb_data.mappers.MapperKt")

        every { sessionManager.getCurrentUserRole() } returns currentUserRole

        assignProjectToUserUI = AssignProjectToUserUI(
            assignProjectToUserUseCase,
            consoleIO,
            getAllProjectsUI
        )
    }

    @Test
    fun `should assign project to user successfully with valid UUIDs`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)
        coEvery { validProjectIdString.toUUID() } returns validProjectId
        coEvery { validUserIdString.toUUID() } returns validUserId
        coEvery { assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId) } returns true

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId)
            consoleIO.write("User successfully assigned to the project.")
        }
    }

    @Test
    fun `should handle failed assignment`() = runTest {
        // Given
        val errorMessage = "Permission denied"
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)
        coEvery { validProjectIdString.toUUID() } returns validProjectId
        coEvery { validUserIdString.toUUID() } returns validUserId
        coEvery { assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId) } throws
                IllegalStateException(errorMessage)

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId)
            consoleIO.write("Failed to assign user. $errorMessage")
        }
    }

    @Test
    fun `should handle invalid project ID format`() = runTest {
        // Given
        val invalidProjectId = "not-a-uuid"
        coEvery { consoleIO.read() } returns invalidProjectId
        coEvery { invalidProjectId.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Invalid Project ID format. Please enter a valid UUID.")
        }

        coVerify(exactly = 0) {
            assignProjectToUserUseCase(any(), any(), any())
        }
    }

    @Test
    fun `should handle invalid user ID format`() = runTest {
        // Given
        val invalidUserId = "not-a-uuid"
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, invalidUserId)
        coEvery { validProjectIdString.toUUID() } returns validProjectId
        coEvery { invalidUserId.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            consoleIO.write("Failed to assign user. Invalid UUID format.")
        }
    }

    @Test
    fun `should deny access when user is not admin`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)
        coEvery { validProjectIdString.toUUID() } returns validProjectId
        coEvery { validUserIdString.toUUID() } returns validUserId
        coEvery { sessionManager.getCurrentUserRole() } returns UserRole.MATE

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            sessionManager.getCurrentUserRole()
            consoleIO.write("Failed to assign user. Only admins can assign users to projects.")
        }

        coVerify(exactly = 0) {
            assignProjectToUserUseCase(any(), any(), any())
        }
    }
}
