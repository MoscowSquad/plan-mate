package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verifySequence
import logic.models.UserRole
import logic.usecases.project.UpdateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.session.LoggedInUser
import presentation.session.SessionManager
import java.util.*


class UpdateProjectUITest {
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var updateProjectUI: UpdateProjectUI

    @BeforeEach
    fun setUp() {
        updateProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        updateProjectUI = UpdateProjectUI(updateProjectUseCase, consoleIO)

        mockkObject(SessionManager)
        SessionManager.currentUser = null
    }

    @Test
    fun `should update project successfully when valid inputs and regular user`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { updateProjectUseCase(projectId, projectName, false) } returns true

        // When
        updateProjectUI()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Enter the new project name:")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("Project updated successfully.")
        }
    }

    @Test
    fun `should update project successfully when valid inputs and admin user`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "admin", UserRole.ADMIN, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { updateProjectUseCase(projectId, projectName, true) } returns true

        // When
        updateProjectUI()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Enter the new project name:")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, true)
            consoleIO.write("Project updated successfully.")
        }
    }

    @Test
    fun `should handle error when update project fails`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        val errorMessage = "Project not found"
        SessionManager.currentUser = LoggedInUser(UUID.randomUUID(), "user", UserRole.MATE, listOf())

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { updateProjectUseCase(projectId, projectName, false) } throws IllegalArgumentException(errorMessage)

        // When
        updateProjectUI()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Enter the new project name:")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("Error updating project: $errorMessage")
        }
    }


    @Test
    fun `should handle null current user`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectName = "Updated Project Name"
        SessionManager.currentUser = null

        every { consoleIO.read() } returnsMany listOf(projectId.toString(), projectName)
        every { updateProjectUseCase(projectId, projectName, false) } returns true

        // When
        updateProjectUI()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to update:")
            consoleIO.read()
            consoleIO.write("Enter the new project name:")
            consoleIO.read()
            updateProjectUseCase(projectId, projectName, false)
            consoleIO.write("Project updated successfully.")
        }
    }
}