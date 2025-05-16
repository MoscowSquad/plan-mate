package presentation.project

import data.csv_data.mappers.toUUID
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.DeleteProjectUseCase
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class DeleteProjectUITest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var deleteProjectUI: DeleteProjectUI
    private val projectId = UUID.randomUUID()
    private val projectIdString = projectId.toString()

    @BeforeEach
    fun setUp() {
        deleteProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        deleteProjectUI = DeleteProjectUI(deleteProjectUseCase, consoleIO)

        mockkStatic(String::toUUID)
        every { projectIdString.toUUID() } returns projectId

        every { consoleIO.read() } returns projectIdString
    }

    @Test
    fun `should delete project successfully when user is admin`() {
        // Given
        val adminUser = mockk<LoggedInUser>()
        every { adminUser.role } returns UserRole.ADMIN
        mockkObject(SessionManager)
        every { SessionManager.currentUser } returns adminUser

        // When
        deleteProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId, true)
            consoleIO.write("Project deleted successfully.")
        }
    }

    @Test
    fun `should delete project successfully when user is not admin`() {
        // Given
        val regularUser = mockk<LoggedInUser>()
        every { regularUser.role } returns UserRole.MATE
        mockkObject(SessionManager)
        every { SessionManager.currentUser } returns regularUser

        // When
        deleteProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId, false)
            consoleIO.write("Project deleted successfully.")
        }
    }

    @Test
    fun `should handle exception when deleting project`() {
        // Given
        val errorMessage = "Project not found"

        mockkObject(SessionManager)
        every { SessionManager.currentUser } returns null
        every { deleteProjectUseCase(projectId, false) } throws RuntimeException(errorMessage)

        // When
        deleteProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId, false)
            consoleIO.write("Error deleting project: $errorMessage")
        }
    }

    @Test
    fun `should handle null user in session`() {
        // Given
        mockkObject(SessionManager)
        every { SessionManager.currentUser } returns null

        // When
        deleteProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId, false)
            consoleIO.write("Project deleted successfully.")
        }
    }
}