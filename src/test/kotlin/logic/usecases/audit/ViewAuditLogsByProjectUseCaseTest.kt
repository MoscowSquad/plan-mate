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
import logic.models.AuditType
import java.util.*

class ViewAuditLogsByProjectUseCaseTest {
    private lateinit var repository: AuditRepository
    private lateinit var useCase: ViewAuditLogsByProjectUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ViewAuditLogsByProjectUseCase(repository)
    }

    @Test
    fun `invoke should return empty list when no logs exist for project`() {
        // Given
        val projectId = UUID.randomUUID()
        every { repository.getAllLogsByProjectId(projectId) } returns emptyList()

        // When
        val result = useCase(projectId)

        // Then
        assertTrue(result.isEmpty())
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should return only project logs for given project ID`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000020")
        val expectedLogs = listOf(
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.PROJECT,
                action = "Project created",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = projectId,
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            )
        )

        every { repository.getAllLogsByProjectId(projectId) } returns expectedLogs

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(expectedLogs.size, result.size)
        assertEquals(projectId, result[0].entityId)
        assertEquals(AuditType.PROJECT, result[0].auditType)
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should not return task logs for given project ID`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000020")
        val taskId = UUID.fromString("00000000-0000-0000-0000-000000000020")
        val mixedLogs = listOf(
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.PROJECT,
                action = "Project updated",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = projectId,
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.TASK,
                action = "Task created",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = taskId,
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            )
        )

        every { repository.getAllLogsByProjectId(projectId) } returns mixedLogs.filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(1, result.size)
        assertEquals(AuditType.PROJECT, result[0].auditType)
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }

    @Test
    fun `invoke should return multiple logs when multiple project logs exist`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000020")
        val expectedLogs = listOf(
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.PROJECT,
                action = "Project created",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = projectId,
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000020"),
                auditType = AuditType.PROJECT,
                action = "Project updated",
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = projectId,
                userId = UUID.fromString("00000000-0000-0000-0000-000000000020")
            )
        )

        every { repository.getAllLogsByProjectId(projectId) } returns expectedLogs

        // When
        val result = useCase(projectId)

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.auditType == AuditType.PROJECT && it.entityId == projectId })
        verify(exactly = 1) { repository.getAllLogsByProjectId(projectId) }
    }
}