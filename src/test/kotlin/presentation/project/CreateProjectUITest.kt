package presentation.project

import di.LoggedInUser
import di.SessionManager
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import logic.models.UserRole
import logic.usecases.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import presentation.io.ConsoleIO
import java.util.*

@ExtendWith(MockKExtension::class)
class CreateProjectUITest {

    @MockK
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @MockK
    private lateinit var consoleIO: ConsoleIO

    private lateinit var createProjectUI: CreateProjectUI

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createProjectUI = CreateProjectUI(createProjectUseCase, consoleIO)

        mockkObject(SessionManager)
    }

    @Test
    fun `invoke should create project successfully for admin user`() {
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val adminUser = mockk<LoggedInUser>()

        every { adminUser.role } returns UserRole.ADMIN
        every { SessionManager.currentUser } returns adminUser
        every { consoleIO.read() } returns projectName
        every { consoleIO.write(any()) } just Runs
        every { createProjectUseCase(projectName, true) } returns projectId

        createProjectUI.invoke()

        verify { consoleIO.write("Enter project name:") }
        verify { consoleIO.read() }
        verify { createProjectUseCase(projectName, true) }
        verify { consoleIO.write("Project $projectId created successfully!") }
    }

    @Test
    fun `invoke should create project successfully for regular user`() {
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val regularUser = mockk<LoggedInUser>()

        every { regularUser.role } returns UserRole.MATE
        every { SessionManager.currentUser } returns regularUser
        every { consoleIO.read() } returns projectName
        every { consoleIO.write(any()) } just Runs
        every { createProjectUseCase(projectName, false) } returns projectId

        createProjectUI.invoke()

        // Assert
        verify { consoleIO.write("Enter project name:") }
        verify { consoleIO.read() }
        verify { createProjectUseCase(projectName, false) }
        verify { consoleIO.write("Project $projectId created successfully!") }
    }

    @Test
    fun `invoke should handle error when creating project fails`() {
        // Arrange
        val projectName = "Test Project"
        val errorMessage = "Invalid project name"
        val user = mockk<LoggedInUser>()

        every { user.role } returns UserRole.MATE
        every { SessionManager.currentUser } returns user
        every { consoleIO.read() } returns projectName
        every { consoleIO.write(any()) } just Runs
        every { createProjectUseCase(projectName, false) } throws IllegalArgumentException(errorMessage)


        createProjectUI.invoke()

        verify { consoleIO.write("Enter project name:") }
        verify { consoleIO.read() }
        verify { createProjectUseCase(projectName, false) }
        verify { consoleIO.write("Error creating project: $errorMessage") }
    }

    @Test
    fun `invoke should handle null user in SessionManager`() {

        val projectName = "Test Project"
        val projectId = UUID.randomUUID()

        every { SessionManager.currentUser } returns null
        every { consoleIO.read() } returns projectName
        every { consoleIO.write(any()) } just Runs
        every { createProjectUseCase(projectName, false) } returns projectId

        createProjectUI.invoke()

        verify { consoleIO.write("Enter project name:") }
        verify { consoleIO.read() }
        verify { createProjectUseCase(projectName, false) }
        verify { consoleIO.write("Project $projectId created successfully!") }
    }
}