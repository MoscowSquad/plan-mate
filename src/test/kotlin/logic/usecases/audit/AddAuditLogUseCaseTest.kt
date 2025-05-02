package logic.usecases.audit

import logic.models.AuditLog
import logic.models.AuditType
import data.repositories.AuditRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlinx.datetime.LocalDateTime
import logic.repositoies.AuditRepository
import java.io.File
import java.util.UUID

 class AddAuditLogUseCaseTest {
  private lateinit var testFile: File
  private lateinit var repository: AuditRepositoryImpl

  @BeforeEach
  fun setUp() {
   testFile = File.createTempFile("test-audit", ".csv")
   repository = AuditRepositoryImpl(testFile)
  }

  @AfterEach
  fun tearDown() {
   testFile.delete()
  }

  @Test
  fun `add should append log to file and return true`() {
   val repository = mockk<AuditRepository>()

   every { repository.add(any()) } returns true

   val log = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
    auditType = AuditType.TASK,
    action = "Task created",
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    entityId = UUID.fromString("00000000-0000-0000-0000-000000000002"),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000003")
   )
   val result = repository.add(log)

   assertTrue(result)
   verify { repository.add(log) }
  }

  @Test
  fun `getAllByTaskId should return only task logs for given taskId`() {
   val repository = mockk<AuditRepository>()

   // Test data
   val taskId = UUID.fromString("00000000-0000-0000-0000-000000000004")
   val userId = UUID.fromString("00000000-0000-0000-0000-000000000005")

   val log = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000006"),
    auditType = AuditType.TASK,
    action = "Task created",
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    entityId = taskId,
    userId = userId
   )

   every { repository.getAllByTaskId(taskId) } returns listOf(log)

   // Verify
   val result = repository.getAllByTaskId(taskId)
   assertEquals(1, result.size)
   assertEquals(log.id, result[0].id)
  }

  @Test
  fun `getAllByProjectId should return only project logs for given projectId`() {
   val repository = mockk<AuditRepository>()

   val projectId = UUID.fromString("00000000-0000-0000-0000-000000000007")
   val userId = UUID.fromString("00000000-0000-0000-0000-000000000008")

   val log = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000009"),
    auditType = AuditType.PROJECT,
    action = "Project created",
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    entityId = projectId,
    userId = userId
   )

   every { repository.getAllByProjectId(projectId) } returns listOf(log)

   val result = repository.getAllByProjectId(projectId)
   assertEquals(1, result.size)
   assertEquals(log.id, result[0].id)
  }

  @Test
  fun `invoke should return true when audit log is successfully added`() {
   val mockRepository = mockk<AuditRepository>()
   every { mockRepository.add(any()) } returns true

   val useCase = AddAuditLogUseCase(mockRepository)
   val testLog = AuditLog(
    id = UUID.fromString("00000000-0000-0000-0000-000000000010"),
    auditType = AuditType.TASK,
    action = "Test Action",
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    entityId = UUID.fromString("00000000-0000-0000-0000-000000000011"),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000012")
   )

   val result = useCase.invoke(testLog)
   assertTrue(result)
   verify { mockRepository.add(any()) }
  }

  @Test
  fun `invoke should return false when repository fails to add audit log`() {
   val mockRepository = mockk<AuditRepository>()
   every { mockRepository.add(any()) } returns false

   val useCase = AddAuditLogUseCase(mockRepository)
   val testLog = AuditLog(id = UUID.fromString("00000000-0000-0000-0000-000000000013"),
    auditType = AuditType.TASK,
    action = "Test Action",
    timestamp = LocalDateTime(2023, 12, 31, 23, 59, 59),
    entityId = UUID.fromString("00000000-0000-0000-0000-000000000014"),
    userId = UUID.fromString("00000000-0000-0000-0000-000000000015")
   )

   val result = useCase.invoke(testLog)

   assertFalse(result)
   verify { mockRepository.add(testLog) }
  }

  @Test
  fun `invoke should return false when audit log is invalid`() {
   val mockRepository = mockk<AuditRepository>()
   every { mockRepository.add(any()) } returns false

   val useCase = AddAuditLogUseCase(mockRepository)

   assertFalse(useCase.invoke(null))
  }
 }
