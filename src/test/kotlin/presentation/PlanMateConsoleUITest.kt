package presentation

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.audit.AuditUI
import presentation.auth.AuthenticationUI
import presentation.io.ConsoleIO
import presentation.project.ProjectsUI
import presentation.state.TaskStateUI
import presentation.task.TasksUI
import presentation.user.UserUI


class PlanMateConsoleUITest {
    private lateinit var authenticationUI: AuthenticationUI
    private lateinit var projectsUI: ProjectsUI
    private lateinit var tasksUI: TasksUI
    private lateinit var taskStateUI: TaskStateUI
    private lateinit var userUI: UserUI
    private lateinit var auditUI: AuditUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var planMateConsoleUI: PlanMateConsoleUI

    @BeforeEach
    fun setUp() {
        authenticationUI = mockk(relaxed = true)
        projectsUI = mockk(relaxed = true)
        tasksUI = mockk(relaxed = true)
        taskStateUI = mockk(relaxed = true)
        userUI = mockk(relaxed = true)
        auditUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        planMateConsoleUI = mockk(relaxed = true)
    }

    @Test
    fun `should navigate to projects screen when option 1 is selected`() {
        // Given
        every { consoleIO.read() } returns "1"


        // Then
        coVerify {
            authenticationUI.invoke()
            projectsUI.invoke()
        }
    }

    @Test
    fun `should navigate to user management screen when option 2 is selected`() {
        // Given
        every { consoleIO.read() } returns "2"


        // Then
        coVerify {
            authenticationUI.invoke()
            userUI.invoke()
        }
    }

    @Test
    fun `should navigate to task management screen when option 3 is selected`() {
        // Given
        every { consoleIO.read() } returns "3"


        // Then
        coVerify {
            authenticationUI.invoke()
            tasksUI.invoke()
        }
    }

    @Test
    fun `should navigate to state management screen when option 4 is selected`() {
        // Given
        every { consoleIO.read() } returns "4"


        // Then
        coVerify {
            authenticationUI.invoke()
            taskStateUI.invoke()
        }
    }

    @Test
    fun `should navigate to audit log screen when option 5 is selected`() {
        // Given
        every { consoleIO.read() } returns "5"


        // Then
        coVerify {
            authenticationUI.invoke()
            auditUI.invoke()
        }
    }

    @Test
    fun `should show error message when invalid text input is provided`() {
        // Given
        val invalidOption = "invalid"
        every { consoleIO.read() } returns invalidOption


        // Then
        coVerify {
            authenticationUI.invoke()
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should show error message when numeric input is out of range`() {
        // Given
        val outOfRangeOption = "7"
        every { consoleIO.read() } returns outOfRangeOption


        // Then
        coVerify {
            authenticationUI.invoke()
            consoleIO.write(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should break menu loop after first iteration when isStopped is true`() {
        // Given
        every { consoleIO.read() } returns "1"


        // Then
        coVerify(exactly = 1) {
            consoleIO.read()
            projectsUI.invoke()
            consoleIO.write(match { it.contains("Main Menu") })
        }
    }
}