package presentation.project

import data.mongodb_data.mappers.toUUID
import di.LoggedInUser
import di.SessionManager
import io.mockk.*
import logic.models.UserRole
import logic.usecases.project.UpdateProjectUseCase
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
        mockkStatic("data.mongodb_data.mappers.MapperKt") // Mock the extension function
        SessionManager.currentUser = null
    }

    @Test
    fun `should update project name successfully when valid inputs and regular user`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        every { updateProjectUseCase(projectId, projectName, false) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should update project name successfully when valid inputs and admin user`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "admin", UserRole.ADMIN, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        every { updateProjectUseCase(projectId, projectName, true) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, true)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle error when update project name fails`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        val errorMessage = "Project not found"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        every { updateProjectUseCase(projectId, projectName, false) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("❌ Failed to update project: $errorMessage")
        }
    }

    @Test
    fun `should handle invalid project ID`() {
        val invalidId = "not-a-uuid"
        val validProjectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        // First attempt with invalid ID, then retry with valid ID
        every { consoleIO.read() } returnsMany listOf(invalidId, validProjectId.toString(), projectName)
        every { invalidId.toUUID() } throws IllegalArgumentException("Invalid UUID")
        every { validProjectId.toString().toUUID() } returns validProjectId
        every { updateProjectUseCase(validProjectId, projectName, false) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("❌ please enter correct ID ")
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(validProjectId, projectName, false)
            consoleIO.write("✅ Project updated successfully.")
        }
    }

    @Test
    fun `should handle null user in session`() {
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = null

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { any<String>().toUUID() } returns projectId
        every { updateProjectUseCase(projectId, projectName, false) } returns true

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("✅ Project updated successfully.")
        }
    }
    @Test
    fun `should handle empty project name`() {
        val projectId = UUID.randomUUID()
        val emptyName = ""
        val errorMessage = "Project name cannot be empty"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), emptyName)
        every { any<String>().toUUID() } returns projectId
        every { updateProjectUseCase(projectId, emptyName, false) } throws IllegalArgumentException(errorMessage)

        updateProjectNameUI()

        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Please Enter new project name: ")
            consoleIO.read()
            updateProjectUseCase(projectId, emptyName, false)
            consoleIO.write("❌ Failed to update project: $errorMessage")
        }
    }
}