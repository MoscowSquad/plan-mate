package presentation.project

import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.User.UserRole
import domain.usecases.project.CreateProjectUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import presentation.io.ConsoleIO
import presentation.state.CreateTaskStateUI
import java.util.*

@ExtendWith(MockKExtension::class)
class CreateProjectUITest {

    @MockK
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @MockK
    private lateinit var consoleIO: ConsoleIO

    @MockK
    private lateinit var createTaskStateUI: CreateTaskStateUI

    private lateinit var createProjectUI: CreateProjectUI

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createProjectUI = CreateProjectUI(createProjectUseCase, createTaskStateUI, consoleIO)

        mockkObject(SessionManager)
    }

    @Test
    fun `invoke should create project successfully for admin user`() = runTest {
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val adminUser = mockk<LoggedInUser>()

        coEvery { adminUser.role } returns UserRole.ADMIN
        coEvery { SessionManager.currentUser } returns adminUser
        coEvery { consoleIO.read() } returns projectName
        coEvery { consoleIO.write(any()) } just Runs
        coEvery { createProjectUseCase(projectName) } returns projectId

        createProjectUI.invoke()

        coVerify { consoleIO.write("Enter project name:") }
        coVerify { consoleIO.read() }
        coVerify { createProjectUseCase(projectName) }
        coVerify { consoleIO.write("Project $projectId created successfully!") }
    }

    @Test
    fun `invoke should create project successfully for regular user`() = runTest {
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val regularUser = mockk<LoggedInUser>()

        coEvery { regularUser.role } returns UserRole.MATE
        coEvery { SessionManager.currentUser } returns regularUser
        coEvery { consoleIO.read() } returns projectName
        coEvery { consoleIO.write(any()) } just Runs
        coEvery { createProjectUseCase(projectName) } returns projectId

        createProjectUI.invoke()

        // Assert
        coVerify { consoleIO.write("Enter project name:") }
        coVerify { consoleIO.read() }
        coVerify { createProjectUseCase(projectName) }
        coVerify { consoleIO.write("Project $projectId created successfully!") }
    }

    @Test
    fun `invoke should handle error when creating project fails`() = runTest {
        // Arrange
        val projectName = "Test Project"
        val errorMessage = "Invalid project name"
        val user = mockk<LoggedInUser>()

        coEvery { user.role } returns UserRole.MATE
        coEvery { SessionManager.currentUser } returns user
        coEvery { consoleIO.read() } returns projectName
        coEvery { consoleIO.write(any()) } just Runs
        coEvery { createProjectUseCase(projectName) } throws IllegalArgumentException(errorMessage)

        createProjectUI.invoke()

        coVerify { consoleIO.write("Enter project name:") }
        coVerify { consoleIO.read() }
        coVerify { createProjectUseCase(projectName) }
        coVerify { consoleIO.write("Error creating project: $errorMessage") }
    }

    @Test
    fun `invoke should handle null user in SessionManager`() = runTest {
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()

        coEvery { SessionManager.currentUser } returns null
        coEvery { consoleIO.read() } returns projectName
        coEvery { consoleIO.write(any()) } just Runs
        coEvery { createProjectUseCase(projectName) } returns projectId

        createProjectUI.invoke()

        coVerify { consoleIO.write("Enter project name:") }
        coVerify { consoleIO.read() }
        coVerify { createProjectUseCase(projectName) }
        coVerify { consoleIO.write("Project $projectId created successfully!") }
    }
}
