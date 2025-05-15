package data.csv_parser

import com.google.common.truth.Truth
import data.csv_data.csv_parser.AuditLogCsvParser
import data.csv_data.csv_parser.CsvData
import data.csv_data.dto.AuditLogDto
import test_helper.getAuditLogs
import test_helper.getCsvLines
import test_helper.getSerializedCsvLines
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditLogCsvParserTest {
    private lateinit var parser: AuditLogCsvParser

    @BeforeEach
    fun setUp() {
        parser = AuditLogCsvParser()
    }

    @Test
    fun `should return audit-logs when parse data from audit-log file`() {
        // Given
        val timestamp = "2025-04-29T07:26:51.781688100"
        val csvLines = getCsvLines(timestamp)

        // When
        val result = parser.parse(csvLines)

        // Then
        val auditLogs = getAuditLogs(timestamp)
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when parse data from empty audit-log file`() {
        // Given
        val csvLines = listOf<CsvData>()

        // When
        val result = parser.parse(csvLines)

        // Then
        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when parse data from empty audit-log file with csv-header`() {
        // Given
        val csvLines = listOf(
            "id,entityType,action,timestamp,entityId,userId",
        )

        // When
        val result = parser.parse(csvLines)

        // Then
        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun `should return csv-data when serialize audit-log data`() {
        // Given
        val timestamp = "2025-04-29T07:26:51.781688100"
        val auditLogs = getAuditLogs(timestamp)

        // When
        val result = parser.serialize(auditLogs)

        // Then
        val expectedCsvLines = getSerializedCsvLines(timestamp)
        Truth.assertThat(result).isEqualTo(expectedCsvLines)
    }

    @Test
    fun `should return audit-log header when serialize empty audit-log data`() {
        // Given
        val auditLogs = emptyList<AuditLogDto>()

        // When
        val result = parser.serialize(auditLogs)

        // Then
        val csvLines = listOf("id,entityType,action,timestamp,entityId,userId")
        Truth.assertThat(result).isEqualTo(csvLines)
    }
}