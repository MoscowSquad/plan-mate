package presentation.audit

import data.mappers.toUUID
import io.mockk.*
import kotlinx.datetime.toLocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import logic.usecases.audit.ViewAuditLogsByTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO
import java.util.*

class ViewAuditLogsByTaskUITest {
    private lateinit var viewAuditLogsByTaskUseCase: ViewAuditLogsByTaskUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var viewAuditLogsByTaskUI: ViewAuditLogsByTaskUI
    private val taskId = UUID.randomUUID()
    private val taskIdString = taskId.toString()
    private val allTasksId = UUID(0, 0)
    private val sampleLog = AuditLog(
        id = UUID.randomUUID(),
        action = "Task was created",
        auditType = AuditType.TASK,
        timestamp = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        entityId = UUID.randomUUID(),
        userId = UUID.randomUUID()
    )

    @BeforeEach
    fun setUp() {
        viewAuditLogsByTaskUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        viewAuditLogsByTaskUI = ViewAuditLogsByTaskUI(viewAuditLogsByTaskUseCase, consoleIO)

        mockkStatic(String::toUUID)
        every { taskIdString.toUUID() } returns taskId
    }

    @Test
    fun `should return to audit menu when option 3 is selected`() {
        // Given
        every { consoleIO.read() } returns "3"

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verify(exactly = 1) {
            consoleIO.write(any())
            consoleIO.read()
        }
        verify(exactly = 0) {
            viewAuditLogsByTaskUseCase(any())
        }
    }

    @Test
    fun `should show error message for invalid option`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "3")

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n❌ Invalid input. Please enter 1, 2, or 3.")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should view specific task logs successfully`() {
        // Given
        val logs = listOf(sampleLog)
        every { consoleIO.read() } returnsMany listOf("1", taskIdString, "3")
        every { viewAuditLogsByTaskUseCase(taskId) } returns logs

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n🔍 ENTER TASK DETAILS")
            consoleIO.write("Enter task ID: ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\n📝 AUDIT LOGS (Task ID: $taskId) - 1 entries")
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle invalid UUID format when viewing specific task logs`() {
        // Given
        val invalidUUID = "invalid-uuid"
        val validUUID = taskIdString
        every { consoleIO.read() } returnsMany listOf("1", invalidUUID, validUUID, "3")
        every { invalidUUID.toUUID() } throws IllegalArgumentException("Invalid UUID format")
        every { viewAuditLogsByTaskUseCase(taskId) } returns emptyList()

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n🔍 ENTER TASK DETAILS")
            consoleIO.write("Enter task ID: ")
            consoleIO.read()
            consoleIO.write("❌ Invalid UUID format. Please try again.")
            consoleIO.write("Enter task ID: ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\nℹ️ No audit logs found for Task ID: $taskId")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle empty logs when viewing specific task logs`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("1", taskIdString, "3")
        every { viewAuditLogsByTaskUseCase(taskId) } returns emptyList()

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n🔍 ENTER TASK DETAILS")
            consoleIO.write("Enter task ID: ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\nℹ️ No audit logs found for Task ID: $taskId")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when viewing specific task logs`() {
        // Given
        val errorMsg = "Task not found"
        every { consoleIO.read() } returnsMany listOf("1", taskIdString, "3")
        every { viewAuditLogsByTaskUseCase(taskId) } throws RuntimeException(errorMsg)

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n🔍 ENTER TASK DETAILS")
            consoleIO.write("Enter task ID: ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\n❌ Error retrieving task logs: $errorMsg")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should view all task logs successfully`() {
        // Given
        val logs = listOf(sampleLog, sampleLog)
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByTaskUseCase(allTasksId) } returns logs

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n📋 ALL TASK LOGS")
            viewAuditLogsByTaskUseCase(allTasksId)
            consoleIO.write("\n📝 AUDIT LOGS (All Tasks) - 2 entries")
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle empty logs when viewing all task logs`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByTaskUseCase(allTasksId) } returns emptyList()

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n📋 ALL TASK LOGS")
            viewAuditLogsByTaskUseCase(allTasksId)
            consoleIO.write("\nℹ️ No audit logs found for All Tasks")
            consoleIO.write(any())
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when viewing all task logs`() {
        // Given
        val errorMsg = "Database connection error"
        every { consoleIO.read() } returnsMany listOf("2", "3")
        every { viewAuditLogsByTaskUseCase(allTasksId) } throws RuntimeException(errorMsg)

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(any())
            consoleIO.read()
            consoleIO.write("\n📋 ALL TASK LOGS")
            viewAuditLogsByTaskUseCase(allTasksId)
            consoleIO.write("\n❌ Error retrieving all task logs: $errorMsg")
            consoleIO.write(any())
            consoleIO.read()
        }
    }
    @Test
    fun `should handle invalid numeric menu option`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("4", "3")

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        verifySequence {
            consoleIO.write(match { it.contains("TASK AUDIT LOG VIEWER") })
            consoleIO.read()
            consoleIO.write("\n❌ Invalid input. Please enter 1, 2, or 3.")
            consoleIO.write(match { it.contains("TASK AUDIT LOG VIEWER") })
            consoleIO.read()
        }
    }
}