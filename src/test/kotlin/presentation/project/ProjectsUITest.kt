package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class ProjectsUITest {
    private lateinit var getAllProjectsUI: GetAllProjectsUI
    private lateinit var createProjectUI: CreateProjectUI
    private lateinit var updateProjectNameUI: UpdateProjectNameUI
    private lateinit var deleteProjectUI: DeleteProjectUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var projectsUI: ProjectsUI

    @BeforeEach
    fun setUp() {
        getAllProjectsUI = mockk(relaxed = true)
        createProjectUI = mockk(relaxed = true)
        updateProjectNameUI = mockk(relaxed = true)
        deleteProjectUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        projectsUI = ProjectsUI(
            getAllProjectsUI,
            createProjectUI,
            updateProjectNameUI,
            deleteProjectUI,
            consoleIO
        )
    }

    @Test
    fun `should display menu and call getAllProjectsUI`() {
        // Given
        every { consoleIO.read() } returns "4"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            consoleIO.write("Going back to the main menu...")
        }
    }

    @Test
    fun `should call createProjectUI when option 1 is selected`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            createProjectUI.invoke()
        }
    }

    @Test
    fun `should call updateProjectUI when option 2 is selected`() {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            updateProjectNameUI.invoke()
        }
    }

    @Test
    fun `should call deleteProjectUI when option 3 is selected`() {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            deleteProjectUI.invoke()
        }
    }

    @Test
    fun `should return to main menu when option 4 is selected`() {
        // Given
        every { consoleIO.read() } returns "4"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            consoleIO.write("Going back to the main menu...")
        }

        verify(exactly = 0) {
            createProjectUI.invoke()
            updateProjectNameUI.invoke()
            deleteProjectUI.invoke()
        }
    }

    @Test
    fun `should display error message when invalid option is entered`() {
        // Given
        every { consoleIO.read() } returns "invalid"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }

    @Test
    fun `should display error message when out of range option is entered`() {
        // Given
        every { consoleIO.read() } returns "5"

        // When
        projectsUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any<String>()) // Menu display
            consoleIO.read()
            getAllProjectsUI.invoke()
            consoleIO.write("\nInvalid input. Please enter a number between 1 and 4.")
        }
    }
}