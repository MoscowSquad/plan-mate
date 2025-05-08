package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import logic.models.Project
import logic.usecases.project.GetAllProjectsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.UUID

class GetAllProjectsUITest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var getAllProjectsUI: GetAllProjectsUI

    @BeforeEach
    fun setUp() {
        getAllProjectsUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        getAllProjectsUI = GetAllProjectsUI(getAllProjectsUseCase, consoleIO)
    }

    @Test
    fun `should display projects when projects are available`() {
        // Given
        val project1 = createProject(id = "00000000-0000-0000-0000-000000000001", name = "Project 1")
        val project2 = createProject(id = "00000000-0000-0000-0000-000000000002", name = "Project 2")
        val projects = listOf(project1, project2)
        every { getAllProjectsUseCase() } returns projects

        // When
        getAllProjectsUI.invoke()

        // Then
        verifySequence {
            getAllProjectsUseCase()
            consoleIO.write("Projects:")
            consoleIO.write("Project ID: 00000000-0000-0000-0000-000000000001, Name: Project 1")
            consoleIO.write("Project ID: 00000000-0000-0000-0000-000000000002, Name: Project 2")
        }
    }

    @Test
    fun `should display message when no projects are found`() {
        // Given
        every { getAllProjectsUseCase() } returns emptyList()

        // When
        getAllProjectsUI.invoke()

        // Then
        verify(exactly = 1) {
            consoleIO.write("No projects found.")
        }
    }

    @Test
    fun `should display error message when exception is thrown`() {
        // Given
        val errorMessage = "Database connection failed"
        val exception = RuntimeException(errorMessage)
        every { getAllProjectsUseCase() } throws exception

        // When
        getAllProjectsUI.invoke()

        // Then
        verify(exactly = 1) {
            consoleIO.write("Error retrieving projects: $errorMessage")
        }
    }

    private fun createProject(id: String, name: String) = Project(id = UUID.fromString(id), name = name)
}