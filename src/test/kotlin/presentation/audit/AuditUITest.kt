package presentation.audit

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class AuditUITest {
    private lateinit var viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI
    private lateinit var viewAuditLogsByTaskUI: ViewAuditLogsByTaskUI
    private lateinit var consoleIO: ConsoleIO
    private lateinit var auditUI: AuditUI

    @BeforeEach
    fun setUp() {
        viewAuditLogsByProjectUI = mockk(relaxed = true)
        viewAuditLogsByTaskUI = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        auditUI = AuditUI(
            viewAuditLogsByProjectUI,
            viewAuditLogsByTaskUI,
            consoleIO
        )
    }

    @Test
    fun `should call viewAuditLogsByProjectUI when user selects option 1`() {
        // Given
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns "1" andThen "3"
        every { viewAuditLogsByProjectUI.invoke() } just runs

        // When
        auditUI()

        // Then
        verify(exactly = 2) { consoleIO.write(any()) }
        verify(exactly = 2) { consoleIO.read() }
        verify(exactly = 1) { viewAuditLogsByProjectUI.invoke() }
    }

    @Test
    fun `should call viewAuditLogsByTaskUI when user selects option 2`() {
        // Given
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns "2" andThen "3"
        every { viewAuditLogsByTaskUI.invoke() } just runs

        // When
        auditUI()

        // Then
        verify(exactly = 2) { consoleIO.write(any()) }
        verify(exactly = 2) { consoleIO.read() }
        verify(exactly = 1) { viewAuditLogsByTaskUI.invoke() }
    }

    @Test
    fun `should exit when user selects option 3`() {
        // Given
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns "3"

        // When
        auditUI()

        // Then
        verify(exactly = 1) { consoleIO.write(any()) }
        verify(exactly = 1) { consoleIO.read() }
    }

    @Test
    fun `should show error message and continue when user enters invalid option`() {
        // Given
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns "invalid" andThen "3"

        // When
        auditUI()

        // Then
        verify(exactly = 3) { consoleIO.write(any()) } // Menu + Error message + Menu again
        verify(exactly = 2) { consoleIO.read() }
    }

    @Test
    fun `should show error message and continue when user enters out of range number`() {
        // Given
        every { consoleIO.write(any()) } just runs
        every { consoleIO.read() } returns "5" andThen "3"

        // When
        auditUI()

        // Then
        verify(exactly = 3) { consoleIO.write(any()) } // Menu + Error message + Menu again
        verify(exactly = 2) { consoleIO.read() }
    }

    @Test
    fun `should handle multiple valid inputs before exiting`() {
        // Given
        every { consoleIO.write(any()) } just runs

        val inputs = mutableListOf("1", "2", "3")
        every { consoleIO.read() } answers { inputs.removeAt(0) }

        every { viewAuditLogsByProjectUI.invoke() } just runs
        every { viewAuditLogsByTaskUI.invoke() } just runs

        // When
        auditUI()

        // Then
        verify { viewAuditLogsByProjectUI.invoke() }
        verify { viewAuditLogsByTaskUI.invoke() }
        verify(exactly = 3) { consoleIO.read() }
    }
}