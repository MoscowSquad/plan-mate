package presentation.audit

import domain.models.AuditLog
import domain.models.AuditLog.AuditType
import domain.usecases.audit.ViewAuditLogsByProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByProjectUITest {
    private lateinit var viewAuditLogsByProjectUseCase: ViewAuditLogsByProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI
    private val projectId = UUID.randomUUID()
    private val projectIdString = projectId.toString()

    private val sampleAuditLogs = listOf(
        AuditLog(
            id = UUID.randomUUID(),
            action = "CREATE",
            auditType = AuditType.PROJECT,
            timestamp = Instant.parse("2023-01-01T10:00:00Z").toLocalDateTime(TimeZone.UTC),
            entityId = UUID.randomUUID()
        ),
        AuditLog(
            id = UUID.randomUUID(),
            action = "UPDATE",
            auditType = AuditType.PROJECT,
            timestamp = Instant.parse("2023-01-02T11:00:00Z").toLocalDateTime(TimeZone.UTC),
            entityId = UUID.randomUUID()
        )
    )

    @BeforeEach
    fun setUp() {
        viewAuditLogsByProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        viewAuditLogsByProjectUI = ViewAuditLogsByProjectUI(viewAuditLogsByProjectUseCase, consoleIO)
    }

    @Test
    fun `should display logs and return on exit command`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(projectIdString, "exit")
        every { viewAuditLogsByProjectUseCase(projectId) } returns sampleAuditLogs

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("📝 AUDIT LOGS (Project ID: $projectId)") })
            consoleIO.write("=========================================")
            consoleIO.write(match { it.contains("1. [") && it.contains("CREATE") })
            consoleIO.write(match { it.contains("2. [") && it.contains("UPDATE") })
            consoleIO.write("=========================================")
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle empty logs`() {
        // Given
        every { consoleIO.read() } returnsMany listOf(projectIdString, "exit")
        every { viewAuditLogsByProjectUseCase(projectId) } returns emptyList()

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("ℹ️ No audit logs found for Project ID: $projectId") })
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when retrieving logs`() {
        // Given
        val errorMessage = "Database connection error"
        every { consoleIO.read() } returnsMany listOf(projectIdString, "exit")
        every { viewAuditLogsByProjectUseCase(projectId) } throws RuntimeException(errorMessage)

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("❌ Error retrieving project logs: $errorMessage") })
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle invalid UUID input and retry`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid-uuid", projectIdString, "exit")
        every { viewAuditLogsByProjectUseCase(projectId) } returns sampleAuditLogs

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
            consoleIO.write(match { it.contains("❌ Invalid UUID format") })
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("📝 AUDIT LOGS") })
            consoleIO.write("=========================================")
            consoleIO.write(match { it.contains("1. [") })
            consoleIO.write(match { it.contains("2. [") })
            consoleIO.write("=========================================")
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should exit immediately when exit command is entered`() {
        // Given
        every { consoleIO.read() } returns "exit"

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write("Enter project ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
        verify(exactly = 0) {
            viewAuditLogsByProjectUseCase(any())
        }
    }

    @Test
    fun `should handle multiple valid queries before exiting`() {
        // Given
        val secondProjectId = UUID.randomUUID()
        every { consoleIO.read() } returnsMany listOf(
            projectIdString,
            secondProjectId.toString(),
            "exit"
        )
        every { viewAuditLogsByProjectUseCase(projectId) } returns sampleAuditLogs
        every { viewAuditLogsByProjectUseCase(secondProjectId) } returns listOf(sampleAuditLogs.first())

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verify(exactly = 3) { consoleIO.write("Enter project ID (or type 'exit' to quit): ") }
        verify(exactly = 3) { consoleIO.read() }
        verify(exactly = 1) { viewAuditLogsByProjectUseCase(projectId) }
        verify(exactly = 1) { viewAuditLogsByProjectUseCase(secondProjectId) }
    }
}