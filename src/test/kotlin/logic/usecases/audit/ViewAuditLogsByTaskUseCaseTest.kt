package logic.usecases.audit

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.repositories.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlinx.datetime.LocalDateTime
import logic.models.AuditType
import java.util.UUID

 class ViewAuditLogsByTaskUseCaseTest{
  private  lateinit var repository: AuditRepository
  private lateinit var useCase: ViewAuditLogsByTaskUseCase
  @BeforeEach
  fun setUp() {
   repository = mockk()
   useCase = ViewAuditLogsByTaskUseCase(repository)
  }

  @Test
  fun `invoke should return empty list when no logs exist for task`() {
   // Given
   val taskId = UUID.fromString("00000000-0000-0000-0000-000000000020")
   every { repository.getAllLogsByTaskId(taskId) } returns emptyList()

   // When
   val result = useCase(taskId)

   // Then
   assertTrue(result.isEmpty())
   verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
  }

  @Test
  fun `invoke should return only task logs for given task ID`() {
   // Given
   val taskId = UUID.fromString("00000000-0000-0000-0000-000000000021")
   val expectedLogs = listOf(
    AuditLog(
     id = UUID.fromString("00000000-0000-0000-0000-000000000022"),
     auditType = AuditType.TASK,
     action = "Task created",
     timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
     entityId = taskId,
     userId = UUID.fromString("00000000-0000-0000-0000-000000000023")
    )
   )

   every { repository.getAllLogsByTaskId(taskId) } returns expectedLogs

   // When
   val result = useCase(taskId)

   // Then
   assertEquals(expectedLogs.size, result.size)
   assertEquals(taskId, result[0].entityId)
   assertEquals(AuditType.TASK, result[0].auditType)
   verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
  }

  @Test
  fun `invoke should not return project logs for given task ID`() {
   // Given
   val taskId = UUID.fromString("00000000-0000-0000-0000-000000000024")
   val projectId = UUID.fromString("00000000-0000-0000-0000-000000000025")
   val mixedLogs = listOf(
    AuditLog(
     id = UUID.fromString("00000000-0000-0000-0000-000000000026"),
     auditType = AuditType.TASK,
     action = "Task created",
     timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
     entityId = taskId,
     userId = UUID.fromString("00000000-0000-0000-0000-000000000027")
    ),
    AuditLog(
     id = UUID.fromString("00000000-0000-0000-0000-000000000028"),
     auditType = AuditType.PROJECT,
     action = "Project updated",
     timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
     entityId = projectId,
     userId = UUID.fromString("00000000-0000-0000-0000-000000000029")
    )
   )

   every { repository.getAllLogsByTaskId(taskId) } returns mixedLogs.filter { it.auditType == AuditType.TASK && it.entityId == taskId }

   // When
   val result = useCase(taskId)

   // Then
   assertEquals(1, result.size)
   assertEquals(AuditType.TASK, result[0].auditType)
   verify(exactly = 1) { repository.getAllLogsByTaskId(taskId) }
  }

 }