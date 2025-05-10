package logic.usecases.audit

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.repositories.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlinx.datetime.LocalDateTime
import logic.models.AuditType
import java.util.UUID

class ViewAuditLogsByTaskUseCaseTest {
    private lateinit var repository: AuditRepository
    private lateinit var useCase: ViewAuditLogsByTaskUseCase
    private val taskId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ViewAuditLogsByTaskUseCase(repository)
    }

    @Test
    fun `invoke should return empty list when no logs exist for task`() {
        // Given
        every { repository.getAllLogsByTaskId(taskId) } returns emptyList()

        // When
        val result = useCase(taskId)

        // Then
        assertTrue(result.isEmpty())
        verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
    }

    @Test
    fun `invoke should return logs for given task ID`() {
        // Given
        val expectedLog = AuditLog(
            id = UUID.randomUUID(),
            action = "Task created",
            auditType = AuditType.TASK,
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = taskId
        )

        every { repository.getAllLogsByTaskId(taskId) } returns listOf(expectedLog)

        // When
        val result = useCase(taskId)

        // Then
        assertEquals(1, result.size)
        assertEquals(taskId, result[0].entityId)
        assertEquals(AuditType.TASK, result[0].auditType)
        verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
    }

    @Test
    fun `invoke should return multiple logs when multiple logs exist`() {
        // Given
        val log1 = AuditLog(
            id = UUID.randomUUID(),
            action = "Task created",
            auditType = AuditType.TASK,
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = taskId
        )

        val log2 = AuditLog(
            id = UUID.randomUUID(),
            action = "Task updated",
            auditType = AuditType.TASK,
            timestamp = LocalDateTime(2024, 1, 1, 10, 30, 0),
            entityId = taskId
        )

        val expectedLogs = listOf(log1, log2)
        every { repository.getAllLogsByTaskId(taskId) } returns expectedLogs

        // When
        val result = useCase(taskId)

        // Then
        assertEquals(2, result.size)
        verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
    }
}