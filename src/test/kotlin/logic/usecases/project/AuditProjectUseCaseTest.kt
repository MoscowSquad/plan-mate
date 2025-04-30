package logic.usecases.project

import io.mockk.mockk
import logic.models.AuditLog
import logic.models.EntityType
import logic.repositoies.adminSpecificProjectManagmanetRepository.InMemoryAuditProjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.ValidatorForASPM.ValidateProjectExists
import java.util.*

class AuditProjectUseCaseTest {

 private lateinit var auditProjectUseCase: AuditProjectUseCase
 private lateinit var auditRepository: InMemoryAuditProjectRepository
 private lateinit var validateProjectExists: ValidateProjectExists

 @BeforeEach
 fun setUp() {
  auditRepository = InMemoryAuditProjectRepository()
  validateProjectExists = mockk()
  auditProjectUseCase = AuditProjectUseCase(auditRepository, validateProjectExists)
 }

 @Test
 fun `getSpecificAuditByProject should return audit log when project and audit exist`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000020")
  val auditId = UUID.fromString("00000000-0000-0000-0000-000000000021")
  val expectedAudit = AuditLog(
      id = auditId, action = "Test action 1",
      entityType = EntityType.PROJECT,
      timestamp = TODO(),
      entityId = UUID.fromString("00000000-0000-0000-0000-000000000022"),
      userId = UUID.fromString("00000000-0000-0000-0000-000000000023")
  )
  auditRepository.addAuditToProject(projectId, expectedAudit)

  // When
  val result = auditProjectUseCase.getSpecificAuditByProject(projectId, auditId)

  // Then
  assertEquals(expectedAudit, result)
 }

 @Test
 fun `getSpecificAuditByProject should throw when audit does not exist`() {
  // Given
  val projectId = UUID.randomUUID()
  val auditId = UUID.randomUUID()

  // When & Then
  assertThrows<NoSuchElementException> {
   auditProjectUseCase.getSpecificAuditByProject(projectId, auditId)
  }
 }

 @Test
 fun `getAllAuditByProject should return all audits for project`() {
  // Given
  val projectId = UUID.randomUUID()
  val audits = listOf(
   AuditLog(
       id = UUID.randomUUID(), action = "Action 1",
       entityType = EntityType.PROJECT,
       timestamp = TODO(),
       entityId = UUID.fromString("00000000-0000-0000-0000-000000000024"),
       userId = UUID.fromString("00000000-0000-0000-0000-000000000025")
   ),
   AuditLog(
       id = UUID.randomUUID(), action = "Action 2",
       entityType = EntityType.PROJECT,
       timestamp = TODO(),
       entityId = UUID.fromString("00000000-0000-0000-0000-000000000026"),
       userId = UUID.fromString("00000000-0000-0000-0000-000000000027")
   )
  )
  audits.forEach { auditRepository.addAuditToProject(projectId, it) }

  // When
  val result = auditProjectUseCase.getAllAuditByProject(projectId)

  // Then
  assertEquals(audits, result)
 }

 @Test
 fun `getAllAuditByProject should return empty list when no audits exist`() {
  // Given
  val projectId = UUID.randomUUID()

  // When
  val result = auditProjectUseCase.getAllAuditByProject(projectId)

  // Then
  assertTrue(result.isEmpty())
 }

 @Test
 fun `addAuditToProject should store the audit for the project`() {
  // Given
  val projectId = UUID.randomUUID()
  val audit = AuditLog(
      id = UUID.randomUUID(), action = "New audit",
      entityType = EntityType.PROJECT,
      timestamp = TODO(),
      entityId = UUID.fromString("00000000-0000-0000-0000-000000000028"),
      userId = UUID.fromString("00000000-0000-0000-0000-000000000029")
  )

  // When
  auditProjectUseCase.addAuditToProject(projectId, audit)

  // Then
  val retrieved = auditProjectUseCase.getAllAuditByProject(projectId)
  assertEquals(listOf(audit), retrieved)
 }

 @Test
 fun `auditProjectExists should return true when audit exists in project`() {
  // Given
  val projectId = UUID.randomUUID()
  val taskId = UUID.randomUUID()
  val audit = AuditLog(
      id = UUID.randomUUID(), action = "Test audit",
      entityType = EntityType.PROJECT,
      timestamp = TODO(),
      entityId = UUID.fromString("00000000-0000-0000-0000-000000000022"),
      userId = UUID.fromString("00000000-0000-0000-0000-000000000022")
  )
  auditRepository.addAuditToProject(projectId, audit)

  // When
  val result = auditProjectUseCase.auditProjectExists(projectId, taskId, audit)

  // Then
  assertTrue(result)
 }

 @Test
 fun `auditProjectExists should return false when audit does not exist in project`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000030")
  val taskId = UUID.fromString("00000000-0000-0000-0000-000000000031")
  val audit = AuditLog(
      id = taskId, action = "Test audit",
      entityType = EntityType.PROJECT,
      timestamp = TODO(),
      entityId = UUID.fromString("00000000-0000-0000-0000-000000000032"),
      userId = UUID.fromString("00000000-0000-0000-0000-000000000033")
  )

  // When
  val result = auditProjectUseCase.auditProjectExists(projectId, taskId, audit)

  // Then
  assertFalse(result)
 }
}