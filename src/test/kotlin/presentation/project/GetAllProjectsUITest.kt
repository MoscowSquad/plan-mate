package presentation.project

import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.Project
import domain.models.User.UserRole
import domain.usecases.project.GetAllProjectsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class GetAllProjectsUITest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private val project1 = Project(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Project 1")
    private val project2 = Project(id = UUID.fromString("00000000-0000-0000-0000-000000000002"), name = "Project 2")
    private val projectsList = listOf(project1, project2)

    @BeforeEach
    fun setUp() {
        getAllProjectsUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getAllProjectsUI = GetAllProjectsUI(getAllProjectsUseCase, consoleIO)

        mockkObject(SessionManager)
    }

    @Test
    fun `should display projects when user is admin`() = runTest {
        // Given
        val adminUser = mockk<LoggedInUser>()
        coEvery { adminUser.role } returns UserRole.ADMIN
        coEvery { SessionManager.currentUser } returns adminUser
        coEvery { getAllProjectsUseCase() } returns projectsList

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify {
            getAllProjectsUseCase()
            consoleIO.write("Projects:")
            consoleIO.write(match { it.contains("Project ID : ${project1.id}") && it.contains("Name       : ${project1.name}") })
            consoleIO.write(match { it.contains("Project ID : ${project2.id}") && it.contains("Name       : ${project2.name}") })
        }
    }

    @Test
    fun `should display projects when user is not admin`() = runTest {
        // Given
        val regularUser = mockk<LoggedInUser>()
        coEvery { regularUser.role } returns UserRole.MATE
        coEvery { SessionManager.currentUser } returns regularUser
        coEvery { getAllProjectsUseCase() } returns projectsList

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify {
            getAllProjectsUseCase()
            consoleIO.write("Projects:")
            consoleIO.write(match { it.contains("Project ID : ${project1.id}") && it.contains("Name       : ${project1.name}") })
            consoleIO.write(match { it.contains("Project ID : ${project2.id}") && it.contains("Name       : ${project2.name}") })
        }
    }

    @Test
    fun `should display message when no projects are found`() = runTest {
        // Given
        coEvery { SessionManager.currentUser } returns null
        coEvery { getAllProjectsUseCase() } returns emptyList()

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify(exactly = 1) {
            getAllProjectsUseCase()
            consoleIO.write("No projects found.")
        }
    }

    @Test
    fun `should handle exception when retrieving projects`() = runTest {
        // Given
        val errorMessage = "Database connection failed"
        coEvery { SessionManager.currentUser } returns null
        coEvery { getAllProjectsUseCase() } throws RuntimeException(errorMessage)

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify(exactly = 1) {
            getAllProjectsUseCase()
            consoleIO.write("Error retrieving projects: $errorMessage")
        }
    }

    @Test
    fun `should handle null user in session`() = runTest {
        // Given
        coEvery { SessionManager.currentUser } returns null
        coEvery { getAllProjectsUseCase() } returns projectsList

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify {
            getAllProjectsUseCase()
            consoleIO.write("Projects:")
            consoleIO.write(match { it.contains("Project ID : ${project1.id}") && it.contains("Name       : ${project1.name}") })
            consoleIO.write(match { it.contains("Project ID : ${project2.id}") && it.contains("Name       : ${project2.name}") })
        }
    }
}