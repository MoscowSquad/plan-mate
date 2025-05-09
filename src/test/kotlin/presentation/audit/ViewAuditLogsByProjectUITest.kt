package presentation.audit

import io.mockk.*
import logic.models.AuditLog
import logic.models.AuditType
import logic.usecases.audit.ViewAuditLogsByProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ViewAuditLogsByProjectUITest {
    private lateinit var viewAuditLogsByProjectUseCase: ViewAuditLogsByProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var viewAuditLogsByProjectUI: ViewAuditLogsByProjectUI
    private val projectId = UUID.randomUUID()
    private val projectIdString = projectId.toString()
    private val allProjectsId = UUID(0, 0)

    private val sampleAuditLogs = listOf(
        AuditLog(
            id = UUID.randomUUID(),
            action = "CREATE",
            auditType = AuditType.PROJECT,
            timestamp = Instant.parse("2023-01-01T10:00:00Z").toLocalDateTime(TimeZone.UTC),
            entityId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        ),
        AuditLog(
            id = UUID.randomUUID(),
            action = "UPDATE",
            auditType = AuditType.PROJECT,
            timestamp = Instant.parse("2023-01-02T11:00:00Z").toLocalDateTime(TimeZone.UTC),
            entityId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )
    )

    @BeforeEach
    fun setUp() {
        viewAuditLogsByProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        viewAuditLogsByProjectUI = ViewAuditLogsByProjectUI(viewAuditLogsByProjectUseCase, consoleIO)
    }

    @Test
    fun `should display menu options and exit when option 3 is selected`() {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verify {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
        verify(exactly = 0) {
            viewAuditLogsByProjectUseCase(any())
        }
    }

    @Test
    fun `should handle invalid menu option`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "3")

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("❌ Invalid input") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should view specific project logs successfully`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", projectIdString, "3")
        every { viewAuditLogsByProjectUseCase(projectId) } returns sampleAuditLogs

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ENTER PROJECT DETAILS") })
            consoleIO.write("Enter project ID: ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("AUDIT LOGS (Project ID: $projectId)") })
            consoleIO.write(match { it.contains("1. [") && it.contains("CREATE") })
            consoleIO.write(match { it.contains("2. [") && it.contains("UPDATE") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should view all project logs successfully`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByProjectUseCase(allProjectsId) } returns sampleAuditLogs

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ALL PROJECT LOGS") })
            viewAuditLogsByProjectUseCase(allProjectsId)
            consoleIO.write(match { it.contains("AUDIT LOGS (All Projects)") })
            consoleIO.write(match { it.contains("1. [") && it.contains("CREATE") })
            consoleIO.write(match { it.contains("2. [") && it.contains("UPDATE") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should handle empty logs when viewing specific project`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", projectIdString, "3")
        every { viewAuditLogsByProjectUseCase(projectId) } returns emptyList()

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ENTER PROJECT DETAILS") })
            consoleIO.write("Enter project ID: ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("No audit logs found") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should handle empty logs when viewing all project logs`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByProjectUseCase(allProjectsId) } returns emptyList()

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ALL PROJECT LOGS") })
            viewAuditLogsByProjectUseCase(allProjectsId)
            consoleIO.write(match { it.contains("No audit logs found") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when viewing specific project logs`() {
        // Given
        val errorMessage = "Database connection error"
        every { consoleIO.read() } returnsMany listOf("1", projectIdString, "3")
        every { viewAuditLogsByProjectUseCase(projectId) } throws RuntimeException(errorMessage)

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ENTER PROJECT DETAILS") })
            consoleIO.write("Enter project ID: ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("❌ Error retrieving project logs: $errorMessage") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when viewing all project logs`() {
        // Given
        val errorMessage = "Database connection error"
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByProjectUseCase(allProjectsId) } throws RuntimeException(errorMessage)

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ALL PROJECT LOGS") })
            viewAuditLogsByProjectUseCase(allProjectsId)
            consoleIO.write(match { it.contains("❌ Error retrieving all project logs: $errorMessage") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }

    @Test
    fun `should handle invalid UUID input and retry`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", "invalid-uuid", projectIdString, "3")
        every { viewAuditLogsByProjectUseCase(projectId) } returns sampleAuditLogs

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("ENTER PROJECT DETAILS") })
            consoleIO.write("Enter project ID: ")
            consoleIO.read()
            consoleIO.write(match { it.contains("❌ Invalid UUID format") })
            consoleIO.write("Enter project ID: ")
            consoleIO.read()
            viewAuditLogsByProjectUseCase(projectId)
            consoleIO.write(match { it.contains("AUDIT LOGS") })
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }
    @Test
    fun `should handle invalid numeric menu option`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("4", "3")

        // When
        viewAuditLogsByProjectUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
            consoleIO.write(match { it.contains("❌ Invalid input") })
            consoleIO.write(match { it.contains("VIEW AUDIT LOGS BY PROJECT") })
            consoleIO.read()
        }
    }
}