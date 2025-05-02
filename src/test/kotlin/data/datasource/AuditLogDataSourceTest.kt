package data.datasource

import com.google.common.truth.Truth
import data.csv_parser.AuditLogCsvParser
import data.csv_parser.CsvHandler
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.models.AuditType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class AuditLogDataSourceTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var auditLogCsvParser: AuditLogCsvParser
    private lateinit var dataSource: AuditLogDataSource

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        auditLogCsvParser = mockk(relaxed = true)

        dataSource = AuditLogDataSource(csvHandler, auditLogCsvParser)
    }

    @Test
    fun `fetch() should call AuditLogCsvParser to parse when return audit-logs data`() {
        dataSource.fetch()
        verify { auditLogCsvParser.parse(any()) }
    }

    @Test
    fun `fetch() should return parsed audit-logs when there is audit-logs returned by audit-log csv-parser`() {
        val userId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val timestamp = LocalDateTime.now()
        val auditLogs = listOf(
            AuditLog(UUID.randomUUID(), AuditType.PROJECT, "Add", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), AuditType.PROJECT, "Edit", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), AuditType.PROJECT, "Delete", timestamp, projectId, userId),
            AuditLog(UUID.randomUUID(), AuditType.TASK, "Change State", timestamp, taskId, userId),
        )
        every { auditLogCsvParser.parse(any()) } returns auditLogs

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `fetch() should return empty list when there is no audit-logs returned by audit-log csv-parser`() {
        val auditLogs = listOf<AuditLog>()
        every {
            auditLogCsvParser.parse(any())
        } returns auditLogs

        val result = dataSource.fetch()
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `save() should call CsvHandler to parse when save audit-log data`() {
        dataSource.save(emptyList())
        verify { csvHandler.write(any(), any()) }
    }
}