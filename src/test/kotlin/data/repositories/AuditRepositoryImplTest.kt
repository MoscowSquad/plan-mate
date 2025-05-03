package data.repositories

import data.datasource.AuditLogDataSource
import data.mappers.toDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDateTime
import logic.models.AuditLog
import logic.models.AuditType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

 class AuditRepositoryImplTest{
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
   auditLogDataSource = mockk()
   every { auditLogDataSource.fetch() } returns initialLogs.map { it.toDto() }

   repository = AuditRepositoryImpl(auditLogDataSource)
  }

  @Test
  fun `init loads logs from data source`() {
   verify { auditLogDataSource.fetch() }
   assertEquals(initialLogs.size, repository.getAllLogs().size)
  }

  @Test
  fun `getAllLogsByTaskId returns empty list when no matching logs exist`() {
   val existingTaskId = UUID.fromString("00000000-0000-0000-0000-000000000108")
   val existingLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000109"),
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000110"),
    entityId = existingTaskId,
    auditType = AuditType.TASK,
    action = "Created"
   )
   repository.addLog(existingLog)
   val nonExistentTaskId = UUID.fromString("00000000-0000-0000-0000-000000000111")
   val result = repository.getAllLogsByTaskId(nonExistentTaskId)
   assertTrue(result.isEmpty(), "Expected empty list for non-existent task ID")
  }

  @Test
  fun `getAllLogsByTaskId returns only task logs for specified task ID`() {
   val additionalLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000112"),
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000113"),
    entityId = testTaskId,
    auditType = AuditType.TASK,
    action = "Updated"
   )
   repository.addLog(additionalLog)

   val result = repository.getAllLogsByTaskId(testTaskId)

   assertEquals(2, result.size)
   assertTrue(result.all { it.auditType == AuditType.TASK && it.entityId == testTaskId })
  }

  @Test
  fun `getAllLogsByTaskId does not return project logs`() {
   val projectLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000114"),
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000115"),
    entityId = testProjectId,
    auditType = AuditType.PROJECT,
    action = "Created"
   )
   repository.addLog(projectLog)

   val result = repository.getAllLogsByTaskId(testTaskId)

   assertTrue(result.none { it.auditType == AuditType.PROJECT })
  }

  @Test
  fun `getAllLogsByProjectId returns empty list when no matching logs exist`() {
   val nonExistentProjectId = UUID.randomUUID()
   val result = repository.getAllLogsByProjectId(nonExistentProjectId)
   assertTrue(result.isEmpty())
  }

  @Test
  fun `getAllLogsByProjectId returns only project logs for specified project ID`() {
   // Add another log for the same project
   val additionalLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000116"),
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000117"),
    entityId = testProjectId,
    auditType = AuditType.PROJECT,
    action = "Updated"
   )
   repository.addLog(additionalLog)

   val result = repository.getAllLogsByProjectId(testProjectId)

   assertEquals(2, result.size)
   assertTrue(result.all { it.auditType == AuditType.PROJECT && it.entityId == testProjectId })
  }

  @Test
  fun `getAllLogsByProjectId does not return task logs`() {
   val taskLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000118"),
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000119"),
    entityId = testTaskId,
    auditType = AuditType.TASK,
    action = "Created"
   )
   repository.addLog(taskLog)

   val result = repository.getAllLogsByProjectId(testProjectId)

   assertTrue(result.none { it.auditType == AuditType.TASK })
  }

  // Helper extension function to access all logs for testing
  private fun AuditRepositoryImpl.getAllLogs(): List<AuditLog> {
   val allTaskLogs = this.getAllLogsByTaskId(testTaskId)
   val allProjectLogs = this.getAllLogsByProjectId(testProjectId)
   return allTaskLogs + allProjectLogs
  }
 }