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

    @BeforeEach
    fun setUp() {
        updateProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        updateProjectNameUI = UpdateProjectNameUI(updateProjectUseCase, consoleIO)

        mockkObject(SessionManager)
        mockkStatic("data.mongodb_data.mappers.MapperKt")
        SessionManager.currentUser = null
    }

    @Test
    fun `should update project name successfully when valid inputs and regular user`() = runTest {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        coEvery { updateProjectUseCase(projectId, projectName) } returns true

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should update project name successfully when valid inputs and admin user`() = runTest {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "admin", UserRole.ADMIN, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        coEvery { updateProjectUseCase(projectId, projectName) } returns true

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle error when update project name fails`() = runTest {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        val errorMessage = "Project not found"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        coEvery { updateProjectUseCase(projectId, projectName) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName)
            consoleIO.write("❌ Failed to update project: $errorMessage")
        }
    }

    @Test
    fun `should handle invalid project ID`() = runTest {
        val invalidId = "not-a-uuid"
        val validProjectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(invalidId, validProjectId.toString(), projectName)
        every { invalidId.toUUID() } throws IllegalArgumentException("Invalid UUID")
        every { validProjectId.toString().toUUID() } returns validProjectId
        coEvery { updateProjectUseCase(validProjectId, projectName) } returns true

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("❌ please enter correct ID ")
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(validProjectId, projectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle null user in session`() = runTest {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = null

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        coEvery { updateProjectUseCase(projectId, projectName) } returns true

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle empty project name`() = runTest {
        val projectId = UUID.randomUUID()
        val emptyName = ""
        val errorMessage = "Project name cannot be empty"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), emptyName)
        every { any<String>().toUUID() } returns projectId
        coEvery { updateProjectUseCase(projectId, emptyName) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        coVerifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, emptyName)
            consoleIO.write("❌ Failed to update project: $errorMessage")
        }
    }
}
