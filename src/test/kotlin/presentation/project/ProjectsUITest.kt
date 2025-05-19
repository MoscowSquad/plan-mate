package presentation.project

import domain.models.Project
import domain.usecases.project.GetAllProjectsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class ProjectsUITest {
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private lateinit var createProjectUI: CreateProjectUI
    private lateinit var updateProjectNameUI: UpdateProjectNameUI
    private lateinit var deleteProjectUI: DeleteProjectUI
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var projectsUI: ProjectsUI

    @BeforeEach
    fun setUp() {
        getAllProjectsUI = mockk(relaxed = true)
        createProjectUI = mockk(relaxed = true)
        updateProjectNameUI = mockk(relaxed = true)
        deleteProjectUI = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        projectsUI = ProjectsUI(
            getAllProjectsUI,
            createProjectUI,
            updateProjectNameUI,
            deleteProjectUI,
            getAllProjectsUseCase,
            consoleIO
        )
    }

    @Test
    fun `should redirect to create project when no projects exist`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns emptyList()

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            consoleIO.write("No projects found. You must create a project first.")
            createProjectUI.invoke()
        }

        coVerify(exactly = 0) {
            getAllProjectsUI.invoke()
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when checking if projects exist`() = runTest {
        // Given
        val errorMessage = "Database connection failed"
        coEvery { getAllProjectsUseCase() } throws RuntimeException(errorMessage)

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            consoleIO.write("Error checking projects: $errorMessage")
            consoleIO.write("No projects found. You must create a project first.")
            createProjectUI.invoke()
        }
    }

    @Test
    fun `should display menu and create project when option 1 is selected`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "1"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            createProjectUI.invoke()
        }
    }

    @Test
    fun `should display menu and update project when option 2 is selected`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "2"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            updateProjectNameUI.invoke()
        }
    }

    @Test
    fun `should display menu and delete project when option 3 is selected`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "3"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            deleteProjectUI.invoke()
        }
    }

    @Test
    fun `should display menu and return to main menu when option 4 is selected`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "4"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("Going back to the main menu...")
        }

        coVerify(exactly = 0) {
            createProjectUI.invoke()
            updateProjectNameUI.invoke()
            deleteProjectUI.invoke()
        }
    }

    @Test
    fun `should display error message when invalid input is entered`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "invalid"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }

    @Test
    fun `should display error message when out of range option is entered`() = runTest {
        // Given
        val mockProjects = listOf(mockk<Project>())
        coEvery { getAllProjectsUseCase() } returns mockProjects
        coEvery { consoleIO.read() } returns "5"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUseCase()
            getAllProjectsUI.invoke()
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}