package data.csv_parser

import com.google.common.truth.Truth
import data.dto.AuditLogDto
import data.mappers.toDto
import data.util.PROJECT
import data.util.TASK
import logic.models.AuditLog
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
        val csvLines = listOf<String>()

        // When
        val result = parser.parse(csvLines)

        // Then
        val auditLogs = emptyList<AuditLog>()
        Truth.assertThat(result).isEqualTo(auditLogs)
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
        val auditLogs = emptyList<AuditLog>()
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `should return csv-data when serialize audit-log data`() {
        // Given
        val timestamp = "2025-04-29T07:26:51.781688100"
        val auditLogs = getAuditLogs(timestamp)

        // When
        val result = parser.serialize(auditLogs)

        // Then
        val csvLines = getCsvLines(timestamp)
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return audit-log header when serialize empty audit-log data`() {
        // Given
        val auditLogs = emptyList<AuditLog>()

        // When
        val result = parser.serialize(auditLogs.map { it.toDto() })

        // Then
        val csvLines = listOf("id,entityType,action,timestamp,entityId,userId")
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getAuditLogs(timestamp: String): List<AuditLogDto> {
        return listOf(
            createAuditLog(
                "12a6e381-379d-492e-a4a1-f367733f449d", "Add", PROJECT, timestamp,
                "12a6e381-379d-492e-a4a1-f367733f449d", "12a6e381-379d-492e-a4a1-f367733f449d"
            ),
            createAuditLog(
                "12a6e381-379d-492e-a4a1-f367733f449d", "Edit", PROJECT, timestamp,
                "12a6e381-379d-492e-a4a1-f367733f449d", "12a6e381-379d-492e-a4a1-f367733f449d"
            ),
            createAuditLog(
                "12a6e381-379d-492e-a4a1-f367733f449d", "Add", TASK, timestamp,
                "12a6e381-379d-492e-a4a1-f367733f449d", "12a6e381-379d-492e-a4a1-f367733f449d"
            ),
            createAuditLog(
                "12a6e381-379d-492e-a4a1-f367733f449d", "Edit", TASK, timestamp,
                "12a6e381-379d-492e-a4a1-f367733f449d", "12a6e381-379d-492e-a4a1-f367733f449d"
            ),
            createAuditLog(
                "12a6e381-379d-492e-a4a1-f367733f449d", "Delete", PROJECT, timestamp,
                "12a6e381-379d-492e-a4a1-f367733f449d", "12a6e381-379d-492e-a4a1-f367733f449d"
            ),
        )
    }

    private fun getCsvLines(timestamp: String): List<String> {
        return listOf(
            "id,entityType,action,timestamp,entityId,userId",
            "12a6e381-379d-492e-a4a1-f367733f449d,Add,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
            "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
            "12a6e381-379d-492e-a4a1-f367733f449d,Add,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
            "12a6e381-379d-492e-a4a1-f367733f449d,Edit,$TASK,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
            "12a6e381-379d-492e-a4a1-f367733f449d,Delete,$PROJECT,$timestamp,12a6e381-379d-492e-a4a1-f367733f449d,12a6e381-379d-492e-a4a1-f367733f449d",
        )
    }

    private fun createAuditLog(
        id: String,
        action: String,
        auditType: String,
        timestamp: String,
        entityId: String,
        userId: String,
    ): AuditLogDto {
        return AuditLogDto(id, action, auditType, timestamp, entityId, userId)
    }

}