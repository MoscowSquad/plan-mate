package utilities.csv_parser

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.models.EntityType.PROJECT
import logic.models.EntityType.TASK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test_helper.toCsvData
import java.time.LocalDateTime
import java.util.UUID

class AuditLogCsvParserTest {
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: AuditLogCsvParser

    @BeforeEach
    fun setUp() {
        csvHandler = mockk(relaxed = true)
        parser = AuditLogCsvParser(csvHandler)
    }

    @Test
    fun `should call AuditLogCsvHandler when parsing audit-logs data`() {
        parser.parse()
        verify { csvHandler.getLines() }
    }

    @Test
    fun `should call AuditLogCsvHandler when serialize audit-logs data`() {
        parser.serialize(emptyList())
        verify { csvHandler.write(emptyList()) }
    }


    @Test
    fun `should return audit-logs when parse data from audit-log file`() {
        // Given
        val timestamp = "2025-04-29T07:26:51.781688100"
        val csvLines = getCsvLines(timestamp)
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

        // Then
        val auditLogs = getAuditLogs(timestamp)
        Truth.assertThat(result).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when parse data from empty audit-log file`() {
        // Given
        val csvLines = listOf<String>()
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

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
        every { csvHandler.getLines() } returns csvLines

        // When
        val result = parser.parse()

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
        val csvLines = getCsvLines(timestamp).toCsvData()
        Truth.assertThat(result).isEqualTo(csvLines)
    }

    @Test
    fun `should return audit-log header when serialize empty audit-log data`() {
        // Given
        val auditLogs = emptyList<AuditLog>()

        // When
        val result = parser.serialize(auditLogs)

        // Then
        val csvLines = "id,entityType,action,timestamp,entityId,userId"
        Truth.assertThat(result).isEqualTo(csvLines)
    }


    private fun getAuditLogs(timestamp: String): List<AuditLog> {
        val taskType = TASK.value
        val projectType = PROJECT.value
        return listOf(
            createAuditLog(
                "82e16049-a9fb-4f69-b6f7-3336b68f2ae4", projectType, "Add", timestamp,
                "63112726-d5d9-4408-ab14-b3c96b4aacf5", "b1ca0006-8c10-43dd-8dbd-05dbe3c62d3d"
            ),
            createAuditLog(
                "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f", projectType, "Edit", timestamp,
                "63112726-d5d9-4408-ab14-b3c96b4aacf5", "b1ca0006-8c10-43dd-8dbd-05dbe3c62d3d"
            ),
            createAuditLog(
                "07f641d4-077e-4f08-978d-3b6c9587f4bf", projectType, "Delete", timestamp,
                "63112726-d5d9-4408-ab14-b3c96b4aacf5", "7771ecef-968f-4c32-86d7-2f81a8a44f3b"
            ),
            createAuditLog(
                "c93e2a50-fc82-47a3-970a-bae31bece454", projectType, "Add", timestamp,
                "63112726-d5d9-4408-ab14-b3c96b4aacf5", "7771ecef-968f-4c32-86d7-2f81a8a44f3b"
            ),
            createAuditLog(
                "9da65913-ee18-440f-8d60-58f5a1d0bdd3", taskType, "Add", timestamp,
                "4f81da36-c0ca-4521-a753-8054992796fe", "26553047-a4ae-4a6c-81ec-89b21aebb11c"
            ),
            createAuditLog(
                "49c2ba8e-1c6c-4184-9202-b29b64dadaba", taskType, "Add", timestamp,
                "4f81da36-c0ca-4521-a753-8054992796fe", "26553047-a4ae-4a6c-81ec-89b21aebb11c"
            ),
            createAuditLog(
                "6e0ca12f-7831-491b-ad8f-152039aea17f", taskType, "Delete", timestamp,
                "4f81da36-c0ca-4521-a753-8054992796fe", "34afe18e-9393-41e1-8aa5-17ba77d2f349"
            ),
            createAuditLog(
                "39cdfa5b-96fb-4aa6-9d7b-6937c9875c7d", taskType, "Edit", timestamp,
                "4f81da36-c0ca-4521-a753-8054992796fe", "34afe18e-9393-41e1-8aa5-17ba77d2f349"
            ),
        )
    }

    private fun getCsvLines(timestamp: String): List<String> {
        val taskType = TASK.value
        val projectType = PROJECT.value

        return listOf(
            "id,entityType,action,timestamp,entityId,userId",
            "82e16049-a9fb-4f69-b6f7-3336b68f2ae4,$projectType,Add,$timestamp,63112726-d5d9-4408-ab14-b3c96b4aa3f5,b1ca0006-8c10-43dd-8dbd-05dbe3c62d3d",
            "045e2ef6-a9f8-43d9-9c33-da8cf3a0ff2f,$projectType,Edit,$timestamp,63112726-d5d9-4408-ab14-b3c96b4a23f5,b1ca0006-8c10-43dd-8dbd-05dbe3c62d3d",
            "07f641d4-077e-4f08-978d-3b6c9587f4bf,$projectType,Delete,$timestamp,63112726-d5d9-4408-ab14-b3c96b4a4245,7771e324f-968f-4c32-86d7-2f81a8a44f3b",
            "c93e2a50-fc82-47a3-970a-bae31be532454,$projectType,Add,$timestamp,63112726-d5d9-4408-ab14-b3c96b4a234f5,7771e23f-968f-4c32-86d7-2f81a8a44f3b",
            "9da65913-ee18-440f-8d60-58f5a1d0bdd3,$taskType,Add,$timestamp,4f81da36-c0ca-4521-a753-8054992796fe,26553047-a4ae-4a6c-81ec-89b2123bb11c",
            "49c2ba8e-1c6c-4184-9202-b29b64da532ba,$taskType,Add,$timestamp,4f81da36-c0ca-4521-a753-8054992796fe,26553047-a4ae-4a6c-81ec-89b21335b11c",
            "6e0ca12f-7831-491b-ad8f-152039aea17f,$taskType,Delete,$timestamp,4f81da36-c0ca-4521-a753-8054992796fe,34afe18e-9393-41e1-8aa5-17ba77d2f349",
            "39cd345b-96fb-4aa6-9d7b-6937c9875c7d,$taskType,Edit,$timestamp,4f81da36-c0ca-4521-a753-8054992796fe,34afe18e-9393-41e1-8aa5-17ba77d2f349",
        )
    }

    private fun createAuditLog(
        id: String,
        entityType: Int,
        action: String,
        timestamp: String,
        entityId: String,
        userId: String
    ): AuditLog {
        val type = when (entityType) {
            1 -> PROJECT
            2 -> TASK
            else -> throw Exception("There in no entity with this type $entityType")
        }
        return AuditLog(
            UUID.fromString(id), type, action, LocalDateTime.parse(timestamp),
            UUID.fromString(entityId), UUID.fromString(userId)
        )
    }

}