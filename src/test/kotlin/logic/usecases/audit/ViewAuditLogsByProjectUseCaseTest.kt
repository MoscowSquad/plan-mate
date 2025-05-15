package logic.usecases.audit

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.repositories.AuditRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog.AuditType
import java.util.*

class ViewAuditLogsByProjectUseCaseTest {
    private lateinit var repository: AuditRepository
    private lateinit var useCase: ViewAuditLogsByProjectUseCase
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ViewAuditLogsByProjectUseCase(repository)
    }

    @Test
    fun `invoke should return empty list when no logs exist for project`() {
        // Given
        every { repository.getAllLogsByProjectId(projectId) } returns emptyList()

        // When
        val result = useCase(projectId)

        // Then
        assertTrue(result.isEmpty())
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should return logs for given project ID`() {
        // Given
        val expectedLog = AuditLog(
            id = UUID.randomUUID(),
            action = "Project created",
            auditType = AuditType.PROJECT,
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = projectId
        )

        every { repository.getAllLogsByProjectId(projectId) } returns listOf(expectedLog)

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(1, result.size)
        assertEquals(projectId, result[0].entityId)
        assertEquals(AuditType.PROJECT, result[0].auditType)
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should return both project and task logs for the project`() {
        // Given
        val taskId = UUID.randomUUID()
        val projectLog = AuditLog(
            id = UUID.randomUUID(),
            action = "Project updated",
            auditType = AuditType.PROJECT,
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = projectId
        )

        val taskLog = AuditLog(
            id = UUID.randomUUID(),
            action = "Task created",
            auditType = AuditType.TASK,
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = taskId
        )

        val mixedLogs = listOf(projectLog, taskLog)
        every { repository.getAllLogsByProjectId(projectId) } returns mixedLogs

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.auditType == AuditType.PROJECT })
        assertTrue(result.any { it.auditType == AuditType.TASK })
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should return multiple logs when multiple logs exist`() {
        // Given
        val log1 = AuditLog(
            id = UUID.randomUUID(),
            auditType = AuditType.PROJECT,
            action = "Project created",
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = projectId
        )

        val log2 = AuditLog(
            id = UUID.randomUUID(),
            auditType = AuditType.PROJECT,
            action = "Project updated",
            timestamp = LocalDateTime(2024, 1, 1, 10, 30, 0),
            entityId = projectId
        )

        val expectedLogs = listOf(log1, log2)
        every { repository.getAllLogsByProjectId(projectId) } returns expectedLogs

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(2, result.size)
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }
}