package presentation.project

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
            consoleIO,
        )
    }

    @Test
    fun `should display menu and call getAllProjectsUI`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "4"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
            consoleIO.read()
            consoleIO.write("Going back to the main menu...")
        }
    }

    @Test
    fun `should call createProjectUI when option 1 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "1"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
            consoleIO.read()
            createProjectUI.invoke()
        }
    }

    @Test
    fun `should call updateProjectUI when option 2 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "2"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write(any<String>())
            consoleIO.read()
            updateProjectNameUI.invoke()
        }
    }

    @Test
    fun `should call deleteProjectUI when option 3 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "3"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
            consoleIO.read()
            deleteProjectUI.invoke()
        }
    }

    @Test
    fun `should return to main menu when option 4 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "4"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
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
    fun `should display error message when invalid option is entered`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "invalid"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }

    @Test
    fun `should display error message when out of range option is entered`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "5"

        // When
        projectsUI.invoke()

        // Then
        coVerifySequence {
            getAllProjectsUI.invoke()
            consoleIO.write(any<String>())
            consoleIO.read()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}
