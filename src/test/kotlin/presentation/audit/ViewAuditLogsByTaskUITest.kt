package presentation.audit

import data.mongodb_data.mappers.toUUID
import domain.models.AuditLog
import domain.models.AuditLog.AuditType
import domain.usecases.audit.ViewAuditLogsByTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toLocalDateTime
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
    private val sampleLog = AuditLog(
        id = UUID.randomUUID(),
        action = "Task was created",
        auditType = AuditType.TASK,
        timestamp = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC),
        entityId = UUID.randomUUID()
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
    fun `should exit when exit command is entered`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "exit"

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
        coVerify(exactly = 0) {
            viewAuditLogsByTaskUseCase(any())
        }
    }

    @Test
    fun `should view specific task logs successfully`() = runTest {
        // Given
        val logs = listOf(sampleLog)
        coEvery { consoleIO.read() } returnsMany listOf(taskIdString, "exit")
        coEvery { viewAuditLogsByTaskUseCase(taskId) } returns logs

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\n📝 AUDIT LOGS (Task ID: $taskId) - 1 entries")
            consoleIO.write("=========================================")
            consoleIO.write(any())
            consoleIO.write("=========================================")
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle invalid UUID format`() = runTest {
        // Given
        val invalidUUID = "invalid-uuid"
        coEvery { consoleIO.read() } returnsMany listOf(invalidUUID, "exit")
        coEvery { invalidUUID.toUUID() } throws IllegalArgumentException("Invalid UUID format")

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
            consoleIO.write("❌ Invalid UUID format. Please try again.")
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
        coVerify(exactly = 0) {
            viewAuditLogsByTaskUseCase(any())
        }
    }

    @Test
    fun `should handle empty logs`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf(taskIdString, "exit")
        coEvery { viewAuditLogsByTaskUseCase(taskId) } returns emptyList()

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\nℹ️ No audit logs found for Task ID: $taskId")
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle exception when retrieving logs`() = runTest {
        // Given
        val errorMsg = "Database connection error"
        coEvery { consoleIO.read() } returnsMany listOf(taskIdString, "exit")
        coEvery { viewAuditLogsByTaskUseCase(taskId) } throws RuntimeException(errorMsg)

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerifySequence {
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
            viewAuditLogsByTaskUseCase(taskId)
            consoleIO.write("\n❌ Error retrieving task logs: $errorMsg")
            consoleIO.write("Enter task ID (or type 'exit' to quit): ")
            consoleIO.read()
        }
    }

    @Test
    fun `should handle multiple valid queries before exiting`() = runTest {
        // Given
        val secondTaskId = UUID.randomUUID()
        val secondTaskIdString = secondTaskId.toString()
        coEvery { secondTaskIdString.toUUID() } returns secondTaskId

        coEvery { consoleIO.read() } returnsMany listOf(
            taskIdString,
            secondTaskIdString,
            "exit"
        )
        coEvery { viewAuditLogsByTaskUseCase(taskId) } returns listOf(sampleLog, sampleLog)
        coEvery { viewAuditLogsByTaskUseCase(secondTaskId) } returns listOf(sampleLog)

        // When
        viewAuditLogsByTaskUI.invoke()

        // Then
        coVerify(exactly = 3) { consoleIO.write("Enter task ID (or type 'exit' to quit): ") }
        coVerify(exactly = 3) { consoleIO.read() }
        coVerify(exactly = 1) { viewAuditLogsByTaskUseCase(taskId) }
        coVerify(exactly = 1) { viewAuditLogsByTaskUseCase(secondTaskId) }
    }
}
