package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.AuditLog
import logic.models.EntityType
import logic.models.Project
import logic.repositoies.AuditRepository
import logic.repositoies.project.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertFailsWith
/*
class AuditProjectUseCaseTest{

  private lateinit var  projectsRepository: ProjectsRepository
  private lateinit var auditRepository: AuditRepository
  private lateinit var auditProjectUseCase: AddAuditProjectUseCase

  @BeforeEach
  fun setup(){
   projectsRepository= mockk()
   auditRepository= mockk()
   addAuditProjectUseCase= AddAuditProjectUseCase(projectsRepository,auditRepository)
  }



  @Test
  fun `should throw exception when no project exists`() {

   val projectId=""
   val audit= mockk<AuditLog>()

   every{projectsRepository.getProject(any())} returns null

   val exception= assertFailsWith<IllegalArgumentException> {
    addAuditProjectUseCase.execute(projectId,audit)
   }

   assertEquals("Project  not found",exception.message)
  }


  @Test
  fun `should add auditLog to project when project exists`() {
   val projectId=""
   val project = mockk<Project>()

   val audit = AuditLog(
    id = UUID.randomUUID(),
    entityType = EntityType.PROJECT,
    action = "Created",
    timestamp = LocalDateTime.now(),
    entityId = UUID.randomUUID(),
    userId = UUID.randomUUID()
   )


   every { projectsRepository.getProject(any()) } returns project
   every { auditRepository.addAuditToProject(any(),any()) } returns Unit

   addAuditProjectUseCase.execute(projectId, audit)


   verify { auditRepository.addAuditToProject(projectId, audit) }
  }

 }

 */