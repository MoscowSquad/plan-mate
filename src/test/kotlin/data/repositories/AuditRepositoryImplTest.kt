package data.repositories

    import com.google.common.truth.Truth.assertThat
    import data.csv_data.datasource.AuditLogDataSource
    import data.csv_data.mappers.toDto
    import data.csv_data.repositories.AuditRepositoryImpl
    import domain.models.AuditLog
    import domain.models.AuditLog.AuditType
    import io.mockk.every
    import io.mockk.mockk
    import io.mockk.verify
    import kotlinx.datetime.LocalDateTime
    import org.junit.jupiter.api.Assertions.assertTrue
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import java.util.*

class AuditRepositoryImplTest {
        private lateinit var auditLogDataSource: AuditLogDataSource
        private lateinit var repository: AuditRepositoryImpl

        private val testTaskId = UUID.fromString("00000000-0000-0000-0000-000000000100")
        private val testProjectId = UUID.fromString("00000000-0000-0000-0000-000000000101")
        private val initialLogs = mutableListOf(
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
                action = "Created",
                auditType = AuditType.TASK,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = testTaskId,
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
                action = "Created",
                auditType = AuditType.PROJECT,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000011"),
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
                action = "Updated",
                auditType = AuditType.PROJECT,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000012"),
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000004"),
                action = "Updated",
                auditType = AuditType.TASK,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = testTaskId,
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000005"),
                action = "Deleted",
                auditType = AuditType.TASK,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000013"),
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000006"),
                action = "Created",
                auditType = AuditType.PROJECT,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = testProjectId,
            ),
            AuditLog(
                id = UUID.fromString("00000000-0000-0000-0000-000000000007"),
                action = "Updated",
                auditType = AuditType.PROJECT,
                timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000012"),
            )
        )

        @BeforeEach
        fun setUp() {
            auditLogDataSource = mockk(relaxed = true)
            every { auditLogDataSource.fetch() } returns initialLogs.map { it.toDto() }

            repository = AuditRepositoryImpl(auditLogDataSource)
        }

        @Test
        fun `should load logs from data source when creating an instance of the repository`() {
            verify { auditLogDataSource.fetch() }
        }

        @Test
        fun `getAllLogsByTaskId should return empty list when no matching logs exist`() {
            val nonExistentTaskId = UUID.fromString("00000000-0000-0000-0000-000000000111")
            val result = repository.getAllLogsByTaskId(nonExistentTaskId)

            assertTrue(result.isEmpty(), "Expected empty list for non-existent task ID")
        }

        @Test
        fun `getAllLogsByTaskId should return only logs for specified task ID`() {
            val result = repository.getAllLogsByTaskId(testTaskId)
            val resultIds = result.map { it.id.toString() }

            assertThat(resultIds).containsExactly(
                "00000000-0000-0000-0000-000000000001",
                "00000000-0000-0000-0000-000000000004"
            )
        }

        @Test
        fun `getAllLogsByTaskId should return only TASK type logs`() {
            val result = repository.getAllLogsByTaskId(testTaskId)
            val resultAuditTypes = result.map { it.auditType }.toSet()

            assertThat(resultAuditTypes).containsExactly(AuditType.TASK)
        }

        @Test
        fun `getAllLogsByProjectId should return empty list when no matching logs exist`() {
            val nonExistentProjectId = UUID.randomUUID()
            val result = repository.getAllLogsByProjectId(nonExistentProjectId)

            assertTrue(result.isEmpty())
        }

        @Test
        fun `getAllLogsByProjectId should return only logs for specified project ID`() {
            val result = repository.getAllLogsByProjectId(testProjectId)
            val resultIds = result.map { it.id.toString() }

            assertThat(resultIds).containsExactly("00000000-0000-0000-0000-000000000006")
        }

        @Test
        fun `getAllLogsByProjectId should return only PROJECT type logs`() {
            val result = repository.getAllLogsByProjectId(testProjectId)
            val resultAuditTypes = result.map { it.auditType }.toSet()

            assertThat(resultAuditTypes).containsExactly(AuditType.PROJECT)
        }
    }
