package data.datasource

    import com.google.common.truth.Truth
    import data.csv_data.csv_parser.AuditLogCsvParser
    import data.csv_data.csv_parser.CsvHandler
    import data.csv_data.datasource.AuditLogDataSource
    import data.csv_data.dto.AuditLogDto
    import io.mockk.every
    import io.mockk.mockk
    import io.mockk.verify
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import java.util.*

    class AuditLogDataSourceImplTest {
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
            val auditLogs = listOf(
                AuditLogDto(UUID.randomUUID().toString(), "Add", "PROJECT", "2023-01-01T12:00:00", "projectId"),
                AuditLogDto(UUID.randomUUID().toString(), "Edit", "PROJECT", "2023-01-01T12:00:00", "projectId")
            )
            every { auditLogCsvParser.parse(any()) } returns auditLogs

            val result = dataSource.fetch()
            Truth.assertThat(result).isEqualTo(auditLogs)
        }

        @Test
        fun `fetch() should return empty list when there is no audit-logs returned by audit-log csv-parser`() {
            val auditLogs = listOf<AuditLogDto>()
            every { auditLogCsvParser.parse(any()) } returns auditLogs

            val result = dataSource.fetch()
            Truth.assertThat(result).isEqualTo(auditLogs)
        }

        @Test
        fun `save() should call CsvHandler to parse when save audit-log data`() {
            dataSource.save(emptyList())
            verify { csvHandler.write(any()) }
        }
    }