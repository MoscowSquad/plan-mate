package logic.usecases
/*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.models.EntityType
import logic.usecases.AuditProjectUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.Validator.ProjectExistenceValidator
import java.time.Instant
import java.util.*

class AuditProjectUseCaseTest {

    private lateinit var auditProjectUseCase: AuditProjectUseCase
    private lateinit var auditRepository: InMemoryAuditProjectRepository
    private lateinit var projectExistenceValidator: ProjectExistenceValidator

    @BeforeEach
    fun setUp() {
        auditRepository = InMemoryAuditProjectRepository()
        projectExistenceValidator = mockk(relaxed = true) // Use relaxed mock to avoid missing calls
        auditProjectUseCase = AuditProjectUseCase(auditRepository, projectExistenceValidator)
    }

    @Test
    fun `getSpecificAuditByProject should return audit log when project and audit exist`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000020")
        val auditId = UUID.fromString("00000000-0000-0000-0000-000000000021")
        val expectedAudit = AuditLog(
            id = auditId,
            action = "Test action 1",
            entityType = EntityType.PROJECT,
            timestamp = Instant.now(),
            entityId = UUID.fromString("00000000-0000-0000-0000-000000000022"),
            userId = UUID.fromString("00000000-0000-0000-0000-000000000023")
        )
        auditRepository.addAuditToProject(projectId, expectedAudit)

        // Configure mock to do nothing when validate is called
        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When
        val result = auditProjectUseCase.getSpecificAuditByProject(projectId, auditId)

        // Then
        assertEquals(expectedAudit, result)
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }

    @Test
    fun `getSpecificAuditByProject should throw when audit does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        val auditId = UUID.randomUUID()

        // Configure mock to do nothing when validate is called
        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When & Then
        assertThrows<NoSuchElementException> {
            auditProjectUseCase.getSpecificAuditByProject(projectId, auditId)
        }
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }

    @Test
    fun `getAllAuditByProject should return all audits for project`() {
        // Given
        val projectId = UUID.randomUUID()
        val audits = listOf(
            AuditLog(
                id = UUID.randomUUID(),
                action = "Action 1",
                entityType = EntityType.PROJECT,
                timestamp = Instant.now(),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000024"),
                userId = UUID.fromString("00000000-0000-0000-0000-000000000025")
            ),
            AuditLog(
                id = UUID.randomUUID(),
                action = "Action 2",
                entityType = EntityType.PROJECT,
                timestamp = Instant.now(),
                entityId = UUID.fromString("00000000-0000-0000-0000-000000000026"),
                userId = UUID.fromString("00000000-0000-0000-0000-000000000027")
            )
        )
        audits.forEach { auditRepository.addAuditToProject(projectId, it) }

        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When
        val result = auditProjectUseCase.getAllAuditByProject(projectId)

        // Then
        assertEquals(audits, result)
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }

    @Test
    fun `getAllAuditByProject should return empty list when no audits exist`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When
        val result = auditProjectUseCase.getAllAuditByProject(projectId)

        // Then
        assertTrue(result.isEmpty())
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }

    @Test
    fun `addAuditToProject should store the audit for the project`() {
        // Given
        val projectId = UUID.randomUUID()
        val audit = AuditLog(
            id = UUID.randomUUID(),
            action = "New audit",
            entityType = EntityType.PROJECT,
            timestamp = Instant.now(),
            entityId = UUID.fromString("00000000-0000-0000-0000-000000000028"),
            userId = UUID.fromString("00000000-0000-0000-0000-000000000029")
        )
        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When
        auditProjectUseCase.addAuditToProject(projectId, audit)

        // Then
        val retrieved = auditProjectUseCase.getAllAuditByProject(projectId)
        assertEquals(listOf(audit), retrieved)
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }

    @Test
    fun `auditProjectExists should return false when audit does not exist in project`() {
        // Given
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000030")
        val taskId = UUID.fromString("00000000-0000-0000-0000-000000000031")
        val audit = AuditLog(
            id = taskId,
            action = "Test audit",
            entityType = EntityType.PROJECT,
            timestamp = Instant.now(),
            entityId = UUID.fromString("00000000-0000-0000-0000-000000000032"),
            userId = UUID.fromString("00000000-0000-0000-0000-000000000033")
        )
        every { projectExistenceValidator.validateProjectExists(any()) } returns Unit

        // When
        val result = auditProjectUseCase.auditProjectExists(projectId, taskId, audit)

        // Then
        assertFalse(result)
        verify { projectExistenceValidator.validateProjectExists(projectId) }
    }
}
*/