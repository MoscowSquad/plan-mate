package presentation.project

import data.csv_data.mappers.toUUID
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.DeleteProjectUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
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
    fun `should delete project successfully when user is admin`() = runTest {
        // Given
        val adminUser = mockk<LoggedInUser>()
        coEvery { adminUser.role } returns UserRole.ADMIN
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns adminUser

        // When
        deleteProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId)
            consoleIO.write("Project deleted successfully.")
        }
    }

    @Test
    fun `should delete project successfully when user is not admin`() = runTest {
        // Given
        val regularUser = mockk<LoggedInUser>()
        coEvery { regularUser.role } returns UserRole.MATE
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns regularUser

        // When
        deleteProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId)
            consoleIO.write("Project deleted successfully.")
        }
    }

    @Test
    fun `should handle exception when deleting project`() = runTest {
        // Given
        val errorMessage = "Project not found"

        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns null
        coEvery { deleteProjectUseCase(projectId) } throws RuntimeException(errorMessage)

        // When
        deleteProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId)
            consoleIO.write("Error deleting project: $errorMessage")
        }
    }

    @Test
    fun `should handle null user in session`() = runTest {
        // Given
        mockkObject(SessionManager)
        coEvery { SessionManager.currentUser } returns null

        // When
        deleteProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter the project ID to delete:")
            consoleIO.read()
            deleteProjectUseCase(projectId)
            consoleIO.write("Project deleted successfully.")
        }
    }
}
