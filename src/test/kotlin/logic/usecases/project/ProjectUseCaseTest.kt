package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.models.Project
import logic.repositoies.project.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import utilities.exception.ValidateProjectExists
import java.util.*

class ProjectUseCaseTest {
 private lateinit var projectsRepository: ProjectsRepository
 private lateinit var validateProjectExists: ValidateProjectExists
 private lateinit var projectUseCase: ProjectUseCase

 private val testProject = Project(
  id = UUID.fromString("00000000-0000-0000-0000-000000000007"),
  name = "Test Project",
 )

 @BeforeEach
 fun setUp() {
  projectsRepository = mockk()
  validateProjectExists = mockk()
  projectUseCase = ProjectUseCase(projectsRepository, validateProjectExists)

  every { validateProjectExists.validateProjectExists(any()) } returns Unit
  every { validateProjectExists.validateProjectsExist() } returns Unit
 }

 @Test
 fun `getAllProjects should return all projects`() {
  val expectedProjects = listOf(testProject)
  every { projectsRepository.getAllProjects() } returns expectedProjects

  val result = projectUseCase.getAllProjects()

  assertEquals(expectedProjects, result)
  verify { validateProjectExists.validateProjectsExist() }
 }

 @Test
 fun `getProjectById should return project when exists`() {
  every { projectsRepository.getProjectById(testProject.id) } returns testProject

  val result = projectUseCase.getProjectById(testProject.id)

  assertEquals(testProject, result)
  verify { validateProjectExists.validateProjectExists(testProject.id) }
 }

 @Test
 fun `getProjectById should throw when project not found`() {
  every { projectsRepository.getProjectById(testProject.id) } returns null
  every { validateProjectExists.validateProjectExists(testProject.id) } throws
          ProjectException.ProjectNotFoundException(testProject.id.toString())

  assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(testProject.id)
  }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when validation fails`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000010")
  val expectedMessage = "Project with ID $projectId was not found"

  every { validateProjectExists.validateProjectExists(projectId) } throws
          ProjectException.ProjectNotFoundException(projectId.toString())

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify(exactly = 1) { validateProjectExists.validateProjectExists(projectId) }
  verify(exactly = 0) { projectsRepository.getProjectById(any()) }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when repository returns null`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000011")
  val expectedMessage = "Project with ID $projectId was not found"

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getProjectById(projectId) } returns null

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify { validateProjectExists.validateProjectExists(projectId) }
  verify { projectsRepository.getProjectById(projectId) }
 }

 @Test
 fun `getProjectById should propagate unexpected repository exceptions`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000012")
  val expectedException = RuntimeException("Database error")

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getProjectById(projectId) } throws expectedException

  val exception = assertThrows<RuntimeException> {
   projectUseCase.getProjectById(projectId)
  }

  assertEquals(expectedException, exception)
  verify { validateProjectExists.validateProjectExists(projectId) }
  verify { projectsRepository.getProjectById(projectId) }
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

  every { validateProjectExists.validateProjectExists(zeroId) } throws
          ProjectException.ProjectValidationException("Invalid project ID")

  assertThrows<ProjectException.ProjectValidationException> {
   projectUseCase.getProjectById(zeroId)
  }
 }

 @Test
 fun `getProjectById should validate before repository access`() {
  val projectId = UUID.randomUUID()
  val expectedProject = Project(projectId, "Test Project")

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { projectsRepository.getProjectById(projectId) } returns expectedProject

  projectUseCase.getProjectById(projectId)

  verifyOrder {
   validateProjectExists.validateProjectExists(projectId)
   projectsRepository.getProjectById(projectId)
  }
 }
}