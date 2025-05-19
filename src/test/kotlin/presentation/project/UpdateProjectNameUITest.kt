package presentation.project

import data.mongodb_data.mappers.toUUID
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.UpdateProjectUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class UpdateProjectNameUITest {
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var updateProjectNameUI: UpdateProjectNameUI
    private val projectId = UUID.randomUUID()
    private val projectIdString = projectId.toString()
    private val newProjectName = "Updated Project Name"

    @BeforeEach
    fun setUp() {
        updateProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        updateProjectNameUI = UpdateProjectNameUI(updateProjectUseCase, consoleIO)

        mockkStatic(String::toUUID)
        every { projectIdString.toUUID() } returns projectId
    }

    @Test
    fun `should update project name successfully when user is admin`() = runTest {
        // Given
        val adminUser = mockk<LoggedInUser>()
        coEvery { adminUser.role } returns UserRole.ADMIN
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns adminUser

        every { consoleIO.read() } returnsMany listOf(projectIdString, newProjectName)

        // When
        updateProjectNameUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, newProjectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should update project name successfully when user is not admin`() = runTest {
        // Given
        val regularUser = mockk<LoggedInUser>()
        coEvery { regularUser.role } returns UserRole.MATE
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns regularUser

        every { consoleIO.read() } returnsMany listOf(projectIdString, newProjectName)

        // When
        updateProjectNameUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, newProjectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle exception when updating project name`() = runTest {
        // Given
        val errorMessage = "Project not found"
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns null

        every { consoleIO.read() } returnsMany listOf(projectIdString, newProjectName)
        coEvery { updateProjectUseCase(projectId, newProjectName) } throws RuntimeException(errorMessage)

        // When
        updateProjectNameUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, newProjectName)
            consoleIO.write("❌ Failed to update project: $errorMessage")
        }
    }

    @Test
    fun `should handle invalid project ID`() = runTest {
        // Given
        val invalidIdString = "invalid-uuid"

        every { consoleIO.read() } returns invalidIdString
        every { invalidIdString.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        updateProjectNameUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("❌ please enter correct ID ")
        }

        verify(exactly = 0) {
            consoleIO.write("Please Enter new project name: ")
        }
    }

    @Test
    fun `should handle null user in session`() = runTest {
        // Given
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns null

        every { consoleIO.read() } returnsMany listOf(projectIdString, newProjectName)

        // When
        updateProjectNameUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, newProjectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }
}