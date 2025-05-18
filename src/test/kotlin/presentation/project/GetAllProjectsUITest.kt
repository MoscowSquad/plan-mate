package presentation.project

import domain.models.Project
import domain.usecases.project.GetAllProjectsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

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
    fun `should display projects when projects are available`() = runTest {
        // Given
        val project1 = createProject(id = "00000000-0000-0000-0000-000000000001", name = "Project 1")
        val project2 = createProject(id = "00000000-0000-0000-0000-000000000002", name = "Project 2")
        val projects = listOf(project1, project2)
        coEvery { getAllProjectsUseCase() } returns projects

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify {
            getAllProjectsUseCase()
            consoleIO.write("Projects:")
            consoleIO.write(match { it.contains("Project ID: ${project1.id}") && it.contains("Name: ${project1.name}") })
            consoleIO.write(match { it.contains("Project ID: ${project2.id}") && it.contains("Name: ${project2.name}") })
        }
    }

    @Test
    fun `should display message when no projects are found`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns emptyList()

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify(exactly = 1) {
            consoleIO.write("No projects found.")
        }
    }

    @Test
    fun `should display error message when exception is thrown`() = runTest {
        // Given
        val errorMessage = "Database connection failed"
        coEvery { getAllProjectsUseCase() } throws RuntimeException(errorMessage)

        // When
        getAllProjectsUI.invoke()

        // Then
        coVerify(exactly = 1) {
            consoleIO.write("Error retrieving projects: $errorMessage")
        }
    }

    private fun createProject(id: String, name: String) = Project(id = UUID.fromString(id), name = name)
}
