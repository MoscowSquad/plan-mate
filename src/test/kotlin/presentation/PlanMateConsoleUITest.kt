package presentation

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.audit.AuditUI
import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI
import presentation.user.UserUI

class PlanMateConsoleUITest {
    private lateinit var authenticationUI: AuthenticationUI
    private lateinit var projectsUI: ProjectsUI
    private lateinit var userUI: UserUI
    private lateinit var auditUI: AuditUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var planMateConsoleUI: PlanMateConsoleUI

    @BeforeEach
    fun setUp() {
        authenticationUI = mockk(relaxed = true)
        projectsUI = mockk(relaxed = true)
        userUI = mockk(relaxed = true)
        auditUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        planMateConsoleUI = PlanMateConsoleUI(authenticationUI, projectsUI, userUI, auditUI, consoleIO)
    }

    @Test
    fun `should navigate to projects screen when option 1 is selected`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify {
            authenticationUI.invoke()
            projectsUI.invoke()
        }
    }

    @Test
    fun `should navigate to user management screen when option 2 is selected`() {
        // Given
        every { consoleIO.read() } returns "2"

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify {
            authenticationUI.invoke()
            userUI.invoke()
        }
    }

    @Test
    fun `should navigate to audit log screen when option 3 is selected`() {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify {
            authenticationUI.invoke()
            auditUI.invoke()
        }
    }

    @Test
    fun `should show error message when invalid text input is provided`() {
        // Given
        val invalidOption = "invalid"
        every { consoleIO.read() } returns invalidOption

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify {
            authenticationUI.invoke()
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should show error message when numeric input is out of range`() {
        // Given
        val outOfRangeOption = "5"
        every { consoleIO.read() } returns outOfRangeOption

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify {
            //اع
            authenticationUI.invoke()
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should break menu loop after first iteration when isStopped is true`() {
        // Given
        every { consoleIO.read() } returns "1"

        // When
        planMateConsoleUI.start(isStopped = true)

        // Then
        verify(exactly = 1) {
            consoleIO.read()
            projectsUI.invoke()
            consoleIO.write(match { it.contains("Main Menu") })
        }
    }

}