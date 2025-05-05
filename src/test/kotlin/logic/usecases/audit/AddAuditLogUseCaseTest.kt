package logic.usecases.audit

import com.google.common.truth.Truth
import data.csv_data.datasource.AuditLogDataSource
import data.csv_data.repositories.AuditRepositoryImpl
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class AddAuditLogUseCaseTest {
    private lateinit var testFile: File
    private lateinit var repository: AuditRepositoryImpl
    private lateinit var auditLogDataSource: AuditLogDataSource

    @BeforeEach
    fun setUp() {
        auditLogDataSource = mockk(relaxed = true)
        testFile = File.createTempFile("test-audit", ".csv")
        repository = AuditRepositoryImpl(auditLogDataSource)
    }

    @AfterEach
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun `addLog() should verify saving audit-log via AuditLogDataSource when add audit-log`() {
        // Given
        val log = createLog(auditType = AuditType.TASK)

        // When
        repository.addLog(log)

        // Then
        verify { auditLogDataSource.save(any()) }
    }

    @Test
    fun `getAllLogsByTaskId() should return only task logs for given taskId`() {
        // Given
        val taskId = "00000000-0000-0000-0000-000000000010"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        // When
        val result = repository.getAllLogsByTaskId(UUID.fromString(taskId))
        val resultIds = result.map { it.id.toString() }

        // Then
        Truth.assertThat(resultIds).isEqualTo(
            listOf(
                "00000000-0000-0000-0000-000000000001",
                "00000000-0000-0000-0000-000000000004",
            )
        )
    }


    @Test
    fun `getAllLogsByProjectId() should return only project logs for given projectId`() {
        // Given
        val projectId = "00000000-0000-0000-0000-000000000012"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        // When
        val result = repository.getAllLogsByProjectId(UUID.fromString(projectId))
        val resultIds = result.map { it.id.toString() }

        // Then
        Truth.assertThat(resultIds).isEqualTo(
            listOf(
                "00000000-0000-0000-0000-000000000003",
                "00000000-0000-0000-0000-000000000007"
            )
        )
    }

    private fun createLog(
        id: String = "00000000-0000-0000-0000-000000000001",
        entityId: String = "00000000-0000-0000-0000-000000000002",
        userId: String = "00000000-0000-0000-0000-000000000003",
        auditType: AuditType,
    ): AuditLog {
        return AuditLog(
            id = UUID.fromString(id),
            auditType = auditType,
            action = "Task created",
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            entityId = UUID.fromString(entityId),
            userId = UUID.fromString(userId)
        )
    }

    private fun addLogs(userId: String) {
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000001",
                "00000000-0000-0000-0000-000000000010",
                userId, AuditType.TASK
            )
        )
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000002",
                "00000000-0000-0000-0000-000000000011",
                userId, AuditType.PROJECT
            )
        )
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000003",
                "00000000-0000-0000-0000-000000000012",
                userId, AuditType.PROJECT
            )
        )
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000004",
                "00000000-0000-0000-0000-000000000010",
                userId, AuditType.TASK
            )
        )
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000005",
                "00000000-0000-0000-0000-000000000013",
                userId, AuditType.TASK
            )
        )
        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000006",
                "00000000-0000-0000-0000-000000000010",
                userId, AuditType.PROJECT
            )
        )

        repository.addLog(
            createLog(
                "00000000-0000-0000-0000-000000000007",
                "00000000-0000-0000-0000-000000000012",
                userId, AuditType.PROJECT
            )
        )
    }

}
