package logic.usecases.project
/*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.models.Project
import logic.repositoies.ProjectsRepository
import logic.usecases.ProjectExistenceValidator
import logic.usecases.ProjectUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import utilities.Validator.ProjectExistenceValidator
import java.util.*

class ProjectUseCaseTest {
 private lateinit var projectsRepository: ProjectsRepository
 private lateinit var projectExistenceValidator: ProjectExistenceValidator
 private lateinit var projectUseCase: ProjectUseCase

 private val testProject = Project(
  id = UUID.fromString("00000000-0000-0000-0000-000000000007"),
  name = "Test Project",
 )

 @BeforeEach
 fun setUp() {
  projectsRepository = mockk()
  projectExistenceValidator = mockk()
  projectUseCase = ProjectUseCase(projectsRepository, projectExistenceValidator)

  every { projectExistenceValidator.validateProjectExists(any()) } returns Unit
  every { projectExistenceValidator.validateProjectsExist() } returns Unit
 }

 @Test
 fun `getAllProjects should return all projects`() {
  val expectedProjects = listOf(testProject)
  every { projectsRepository.getAll() } returns expectedProjects

  val result = projectUseCase.getAllProjects()

  assertEquals(expectedProjects, result)
  verify { projectExistenceValidator.validateProjectsExist() }
 }

 @Test
 fun `getProjectById should return project when exists`() {
  every { projectsRepository.getById(testProject.id) } returns testProject

  val result = projectUseCase.getProjectById(testProject.id)

  assertEquals(testProject, result)
  verify { projectExistenceValidator.validateProjectExists(testProject.id) }
 }

 @Test
 fun `getProjectById should throw when project not found`() {
  every { projectsRepository.getById(testProject.id) } returns null
  every { projectExistenceValidator.validateProjectExists(testProject.id) } throws
          ProjectException.ProjectNotFoundException(testProject.id.toString())

  assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(testProject.id)
  }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when validation fails`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000010")
  val expectedMessage = "Project with ID $projectId was not found"

  every { projectExistenceValidator.validateProjectExists(projectId) } throws
          ProjectException.ProjectNotFoundException(projectId.toString())

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify(exactly = 1) { projectExistenceValidator.validateProjectExists(projectId) }
  verify(exactly = 0) { projectsRepository.getById(any()) }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when repository returns null`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000011")
  val expectedMessage = "Project with ID $projectId was not found"

  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getById(projectId) } returns null

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify { projectExistenceValidator.validateProjectExists(projectId) }
  verify { projectsRepository.getById(projectId) }
 }

 @Test
 fun `getProjectById should propagate unexpected repository exceptions`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000012")
  val expectedException = RuntimeException("Database error")

  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getById(projectId) } throws expectedException

  val exception = assertThrows<RuntimeException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedException, exception)
  verify { projectExistenceValidator.validateProjectExists(projectId) }
  verify { projectsRepository.getById(projectId) }
 }

 @Test
 fun `ProjectNotFoundException should handle empty ID string gracefully`() {
  val emptyId = ""
  val expectedMessage = "Project with ID $emptyId was not found"

  val exception = ProjectException.ProjectNotFoundException(emptyId)

  assertEquals(expectedMessage, exception.message)
 }

 @Test
 fun `getProjectById should reject zero UUID`() {
  val zeroId = UUID(0, 0)

  every { projectExistenceValidator.validateProjectExists(zeroId) } throws
          ProjectException.ProjectValidationException("Invalid project ID")

  assertThrows<ProjectException.ProjectValidationException> {
   projectUseCase.getProjectById(zeroId)
  }
 }

 @Test
 fun `getProjectById should validate before repository access`() {
  val projectId = UUID.randomUUID()
  val expectedProject = Project(projectId, "Test Project")

  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getById(projectId) } returns expectedProject

  projectUseCase.getProjectById(projectId)

  verifyOrder {
   projectExistenceValidator.validateProjectExists(projectId)
   projectsRepository.getById(projectId)
  }
 }
}

 */