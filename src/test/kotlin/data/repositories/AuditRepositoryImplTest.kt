package data.repositories

import com.google.common.truth.Truth
import data.csv_data.datasource.AuditLogDataSource
import data.csv_data.mappers.toDto
import data.csv_data.repositories.AuditRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AuditRepositoryImplTest {
    private lateinit var auditLogDataSource: AuditLogDataSource
    private lateinit var repository: AuditRepositoryImpl

    private val testTaskId = UUID.fromString("00000000-0000-0000-0000-000000000100")
    private val testProjectId = UUID.fromString("00000000-0000-0000-0000-000000000101")
    private val initialLogs = listOf(
        AuditLog(
            id = UUID.fromString("00000000-0000-0000-0000-000000000102"),
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            userId = UUID.fromString("00000000-0000-0000-0000-000000000103"),
            entityId = testTaskId,
            auditType = AuditType.TASK,
            action = "Created",
        ),
        AuditLog(
            id = UUID.fromString("00000000-0000-0000-0000-000000000104"),
            timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
            userId = UUID.fromString("00000000-0000-0000-0000-000000000105"),
            entityId = testProjectId,
            auditType = AuditType.PROJECT,
            action = "Updated",
        )
    )

    @BeforeEach
    fun setUp() {
        auditLogDataSource = mockk(relaxed = true)
        every { auditLogDataSource.fetch() } returns initialLogs.map { it.toDto() }

        repository = AuditRepositoryImpl(auditLogDataSource)
    }

    @Test
    fun `should load logs from data source when create an instance of the repository`() {
        verify { auditLogDataSource.fetch() }
        Truth.assertThat(initialLogs).isEqualTo(repository.getAllLogs())
    }

    @Test
    fun `getAllLogsByTaskId() should return empty list when no matching logs exist`() {
        // Given
        val id = "00000000-0000-0000-0000-000000000109"
        val existingTaskId = UUID.fromString("00000000-0000-0000-0000-000000000108")
        val existingUserId = UUID.fromString("00000000-0000-0000-0000-000000000110")
        val existingLog = createLog(
            id, existingTaskId.toString(), existingUserId.toString(), AuditType.TASK
        )
        repository.addLog(existingLog)

        // When
        val nonExistentTaskId = UUID.fromString("00000000-0000-0000-0000-000000000111")
        val result = repository.getAllLogsByTaskId(nonExistentTaskId)

        // Then
        assertTrue(result.isEmpty(), "Expected empty list for non-existent task ID")
    }

    @Test
    fun `getAllLogsByTaskId() should return only task logs when get specified task ID log`() {
        val taskId = "00000000-0000-0000-0000-000000000010"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        val result = repository.getAllLogsByTaskId(UUID.fromString(taskId))
        val resultIds = result.map { it.id.toString() }

        Truth.assertThat(resultIds).isEqualTo(
            listOf(
                "00000000-0000-0000-0000-000000000001",
                "00000000-0000-0000-0000-000000000004",
            )
        )
    }

    @Test
    fun `getAllLogsByTaskId() should does not return project logs`() {
        val taskId = "00000000-0000-0000-0000-000000000010"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        val result = repository.getAllLogsByTaskId(UUID.fromString(taskId))
        val resultAuditTypes = result.map { it.auditType }.toSet()

        Truth.assertThat(resultAuditTypes).isEqualTo(setOf(AuditType.TASK))
    }

    @Test
    fun `getAllLogsByProjectId() returns empty list when no matching logs exist`() {
        val nonExistentProjectId = UUID.randomUUID()
        val result = repository.getAllLogsByProjectId(nonExistentProjectId)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllLogsByProjectId() should return only project logs when get specified project ID log`() {
        val projectId = "00000000-0000-0000-0000-000000000010"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        val result = repository.getAllLogsByProjectId(UUID.fromString(projectId))
        val resultIds = result.map { it.id.toString() }

        Truth.assertThat(resultIds).isEqualTo(
            listOf(
                "00000000-0000-0000-0000-000000000006",
            )
        )
    }

    @Test
    fun `getAllLogsByProjectId() should does not return task logs`() {
        val projectId = "00000000-0000-0000-0000-000000000010"
        val userId = "00000000-0000-0000-0000-000000000005"
        addLogs(userId)

        val result = repository.getAllLogsByProjectId(UUID.fromString(projectId))
        val resultAuditTypes = result.map { it.auditType }.toSet()

        Truth.assertThat(resultAuditTypes).isEqualTo(setOf(AuditType.PROJECT))
    }

    // Helper extension function to access all logs for testing
    private fun AuditRepositoryImpl.getAllLogs(): List<AuditLog> {
        val allTaskLogs = this.getAllLogsByTaskId(testTaskId)
        val allProjectLogs = this.getAllLogsByProjectId(testProjectId)
        return allTaskLogs + allProjectLogs
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