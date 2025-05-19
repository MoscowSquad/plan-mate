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
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private lateinit var assignProjectToUserUI: AssignProjectToUserUI

    private val validProjectId = UUID.randomUUID()
    private val validProjectIdString = validProjectId.toString()
    private val validUserId = UUID.randomUUID()
    private val validUserIdString = validUserId.toString()

    @BeforeEach
    fun setUp() {
        assignProjectToUserUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getAllProjectsUI = mockk(relaxed = true)
        assignProjectToUserUI = AssignProjectToUserUI(assignProjectToUserUseCase, consoleIO, getAllProjectsUI)

        mockkStatic(String::toUUID)
        mockkObject(SessionManager)
    }

    @Test
    fun `should assign project to user successfully when user is admin`() = runTest {
        // Given
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)

        every { validProjectIdString.toUUID() } returns validProjectId
        every { validUserIdString.toUUID() } returns validUserId

        coEvery { assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId) } returns true

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId)
            consoleIO.write("User successfully assigned to the project.")
        }
    }

    @Test
    fun `should handle invalid project ID format`() = runTest {
        // Given
        val invalidProjectId = "not-a-uuid"
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { consoleIO.read() } returns invalidProjectId
        every { invalidProjectId.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
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
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, invalidUserId)

        every { validProjectIdString.toUUID() } returns validProjectId
        every { invalidUserId.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
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
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.MATE
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)

        every { validProjectIdString.toUUID() } returns validProjectId
        every { validUserIdString.toUUID() } returns validUserId

        coEvery { assignProjectToUserUseCase(UserRole.MATE, validProjectId, validUserId) } throws
            IllegalStateException("Only admins can assign users to projects.")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            assignProjectToUserUseCase(UserRole.MATE, validProjectId, validUserId)
            consoleIO.write("Failed to assign user. Only admins can assign users to projects.")
        }
    }

    @Test
    fun `should handle project not found exception`() = runTest {
        // Given
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)

        every { validProjectIdString.toUUID() } returns validProjectId
        every { validUserIdString.toUUID() } returns validUserId

        coEvery { assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId) } throws
            IllegalArgumentException("Project not found")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId)
            consoleIO.write("Failed to assign user. Project not found")
        }
    }

    @Test
    fun `should handle user not found exception`() = runTest {
        // Given
        coEvery { SessionManager.getCurrentUserRole() } returns UserRole.ADMIN
        coEvery { consoleIO.read() } returnsMany listOf(validProjectIdString, validUserIdString)

        every { validProjectIdString.toUUID() } returns validProjectId
        every { validUserIdString.toUUID() } returns validUserId

        coEvery { assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId) } throws
            IllegalArgumentException("User not found")

        // When
        assignProjectToUserUI.invoke()

        // Then
        coVerifySequence {
            SessionManager.getCurrentUserRole()
            getAllProjectsUI.invoke()
            consoleIO.write("\n=== Assign Project to User ===")
            consoleIO.write("Enter Project ID:")
            consoleIO.read()
            consoleIO.write("Enter User ID:")
            consoleIO.read()
            assignProjectToUserUseCase(UserRole.ADMIN, validProjectId, validUserId)
            consoleIO.write("Failed to assign user. User not found")
        }
    }
}