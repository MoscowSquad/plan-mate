package data.mongodb_data.repositories

import com.google.common.truth.Truth.assertThat
import data.data_source.AuditLogDataSource
import data.mongodb_data.dto.AuditLogDto
import domain.models.AuditLog.AuditType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AuditLogRepositoryImplTest {
    private lateinit var auditLogDataSource: AuditLogDataSource
    private lateinit var repository: AuditLogRepositoryImpl

    private val testTaskId = UUID.fromString("00000000-0000-0000-0000-000000000100")
    private val testProjectId = UUID.fromString("00000000-0000-0000-0000-000000000101")
    private val timestamp = Instant.parse("2023-12-31T23:59:59.000Z")

    private val taskLogDtos = listOf(
        AuditLogDto(
            id = UUID.fromString("00000000-0000-0000-0000-000000000001").toString(),
            action = "Created",
            auditType = "TASK",
            timestamp = timestamp.toString(),
            entityId = testTaskId.toString()
        ),
        AuditLogDto(
            id = UUID.fromString("00000000-0000-0000-0000-000000000004").toString(),
            action = "Updated",
            auditType = "TASK",
            timestamp = timestamp.toString(),
            entityId = testTaskId.toString()
        )
    )

    private val projectLogDtos = listOf(
        AuditLogDto(
            id = UUID.fromString("00000000-0000-0000-0000-000000000006").toString(),
            action = "Created",
            auditType = "PROJECT",
            timestamp = timestamp.toString(),
            entityId = testProjectId.toString()
        )
    )

    @BeforeEach
    fun setUp() {
        auditLogDataSource = mockk(relaxed = true)
        repository = AuditLogRepositoryImpl(auditLogDataSource)

        coEvery { auditLogDataSource.getAllLogsByTaskId(testTaskId) } returns taskLogDtos
        coEvery { auditLogDataSource.getAllLogsByTaskId(not(eq(testTaskId))) } returns emptyList()
        coEvery { auditLogDataSource.getAllLogsByProjectId(testProjectId) } returns projectLogDtos
        coEvery { auditLogDataSource.getAllLogsByProjectId(not(eq(testProjectId))) } returns emptyList()
    }

    @Test
    fun `getAllLogsByTaskId should delegate to data source`() = runTest {
        repository.getAllLogsByTaskId(testTaskId)

        coVerify { auditLogDataSource.getAllLogsByTaskId(testTaskId) }
    }

    @Test
    fun `getAllLogsByTaskId should return empty list when no matching logs exist`() = runTest {
        val nonExistentTaskId = UUID.fromString("00000000-0000-0000-0000-000000000111")
        val result = repository.getAllLogsByTaskId(nonExistentTaskId)

        assertTrue(result.isEmpty(), "Expected empty list for non-existent task ID")
    }

    @Test
    fun `getAllLogsByTaskId should return mapped logs for specified task ID`() = runTest {
        val result = repository.getAllLogsByTaskId(testTaskId)
        val resultIds = result.map { it.id.toString() }

        assertThat(resultIds).containsExactly(
            "00000000-0000-0000-0000-000000000001",
            "00000000-0000-0000-0000-000000000004"
        )
    }

    @Test
    fun `getAllLogsByTaskId should maintain TASK type logs`() = runTest {
        val result = repository.getAllLogsByTaskId(testTaskId)
        val resultAuditTypes = result.map { it.auditType }.toSet()

        assertThat(resultAuditTypes).containsExactly(AuditType.TASK)
    }

    @Test
    fun `getAllLogsByProjectId should delegate to data source`() = runTest {
        repository.getAllLogsByProjectId(testProjectId)

        coVerify { auditLogDataSource.getAllLogsByProjectId(testProjectId) }
    }

    @Test
    fun `getAllLogsByProjectId should return empty list when no matching logs exist`() = runTest {
        val nonExistentProjectId = UUID.randomUUID()
        val result = repository.getAllLogsByProjectId(nonExistentProjectId)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllLogsByProjectId should return mapped logs for specified project ID`() = runTest {
        val result = repository.getAllLogsByProjectId(testProjectId)
        val resultIds = result.map { it.id.toString() }

        assertThat(resultIds).containsExactly("00000000-0000-0000-0000-000000000006")
    }

    @Test
    fun `getAllLogsByProjectId should maintain PROJECT type logs`() = runTest {
        val result = repository.getAllLogsByProjectId(testProjectId)
        val resultAuditTypes = result.map { it.auditType }.toSet()

        assertThat(resultAuditTypes).containsExactly(AuditType.PROJECT)
    }

}