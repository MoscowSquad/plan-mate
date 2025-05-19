package presentation.project

import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.CreateProjectUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import presentation.state.CreateTaskStateUI
import java.util.*

class CreateProjectUITest {
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var createTaskStateUI: CreateTaskStateUI
    private lateinit var createProjectUI: CreateProjectUI
    private val projectId = UUID.randomUUID()
    private val projectName = "Test Project"

    @BeforeEach
    fun setUp() {
        createProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        createTaskStateUI = mockk(relaxed = true)
        createProjectUI = CreateProjectUI(createProjectUseCase, createTaskStateUI, consoleIO)

        mockkObject(SessionManager)

        every { consoleIO.read() } returnsMany listOf(projectName, "no")
        coEvery { createProjectUseCase(projectName) } returns projectId
    }

    @Test
    fun `should create project successfully when user is admin`() = runTest {
        // Given
        val adminUser = mockk<LoggedInUser>()
        coEvery { adminUser.role } returns UserRole.ADMIN
        coEvery { SessionManager.currentUser } returns adminUser

        // When
        createProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter project name:")
            consoleIO.read()
            createProjectUseCase(projectName)
            consoleIO.write("Project named $projectName with id $projectId created successfully!")
            consoleIO.write("Would you like to add a state to the project now? (yes/no):")
            consoleIO.read()
            consoleIO.write("⚠️ Warning: You won't be able to add tasks to this project unless you add at least one state.")
        }
    }

    @Test
    fun `should create project successfully when user is not admin`() = runTest {
        // Given
        val regularUser = mockk<LoggedInUser>()
        coEvery { regularUser.role } returns UserRole.MATE
        coEvery { SessionManager.currentUser } returns regularUser

        // When
        createProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter project name:")
            consoleIO.read()
            createProjectUseCase(projectName)
            consoleIO.write("Project named $projectName with id $projectId created successfully!")
            consoleIO.write("Would you like to add a state to the project now? (yes/no):")
            consoleIO.read()
            consoleIO.write("⚠️ Warning: You won't be able to add tasks to this project unless you add at least one state.")
        }
    }

    @Test
    fun `should handle exception when creating project`() = runTest {
        // Given
        val errorMessage = "Invalid project name"
        coEvery { SessionManager.currentUser } returns null
        coEvery { createProjectUseCase(projectName) } throws IllegalArgumentException(errorMessage)

        // When
        createProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter project name:")
            consoleIO.read()
            createProjectUseCase(projectName)
            consoleIO.write("Error creating project: $errorMessage")
        }
    }

    @Test
    fun `should handle null user in session`() = runTest {
        // Given
        coEvery { SessionManager.currentUser } returns null

        // When
        createProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter project name:")
            consoleIO.read()
            createProjectUseCase(projectName)
            consoleIO.write("Project named $projectName with id $projectId created successfully!")
            consoleIO.write("Would you like to add a state to the project now? (yes/no):")
            consoleIO.read()
            consoleIO.write("⚠️ Warning: You won't be able to add tasks to this project unless you add at least one state.")
        }
    }

    @Test
    fun `should create task state when user selects yes`() = runTest {
        // Given
        every { consoleIO.read() } returnsMany listOf(projectName, "yes")
        coEvery { SessionManager.currentUser } returns null

        // When
        createProjectUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter project name:")
            consoleIO.read()
            createProjectUseCase(projectName)
            consoleIO.write("Project named $projectName with id $projectId created successfully!")
            consoleIO.write("Would you like to add a state to the project now? (yes/no):")
            consoleIO.read()
            createTaskStateUI(projectId)
        }
    }
}