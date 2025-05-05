package presentation

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI

class PlanMateConsoleUITest {
    private lateinit var authenticationUI: AuthenticationUI
    private lateinit var projectsUI: ProjectsUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var planMateConsoleUI: PlanMateConsoleUI

    @BeforeEach
    fun setUp() {
        authenticationUI = mockk(relaxed = true)
        projectsUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        planMateConsoleUI = PlanMateConsoleUI(authenticationUI, projectsUI, consoleIO)
    }

    @Test
    fun `should display welcome message and authenticate user on start`() {
        // Given
        every { consoleIO.write(any()) } just runs

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verify {
            consoleIO.write(match { it.contains("Welcome to PlanMate") })
            authenticationUI.invoke()
        }
    }

    @Test
    fun `should display main menu options`() {
        // Given
        every { consoleIO.read() } returns "4" // Exit option

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("Welcome to PlanMate") })
            authenticationUI.invoke()
            consoleIO.write(match { it.contains("Main Menu") })
            consoleIO.read()
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should navigate to projects screen when option 1 is selected`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verify {
            projectsUI.invoke()
        }
    }

    @Test
    fun `should show error message when invalid input is provided`() {
        // Given
        val invalidOption = "invalid"
        every { consoleIO.read() } returns invalidOption

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verify {
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should continue menu loop until stopImminently is true`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verify(exactly = 1) {
            projectsUI.invoke()
        }
    }

    @Test
    fun `should handle numeric input that is out of range`() {
        // Given
        val outOfRangeOption = "5"
        every { consoleIO.read() } returns outOfRangeOption

        // When
        planMateConsoleUI.start(stopImminently = true)

        // Then
        verify {
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }
}