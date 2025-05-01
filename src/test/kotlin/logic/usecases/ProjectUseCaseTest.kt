package logic.usecases
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import logic.models.Project
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
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

  every { projectExistenceValidator.isExist(any()) } returns true
 }

 @Test
 fun `getAllProjects should return all projects`() {
  val expectedProjects = listOf(testProject)
  every { projectExistenceValidator.isExist(any()) } returns true
  every { projectsRepository.getAll() } returns expectedProjects

  val result = projectUseCase.getAll()

  assertEquals(expectedProjects, result)
 }

 @Test
 fun `getProjectById should return project when exists`() {
  every { projectExistenceValidator.isExist(any()) } returns true
  every { projectsRepository.getById(testProject.id) } returns testProject

  val result = projectUseCase.getById(testProject.id)

  assertEquals(testProject, result)
  verify { projectExistenceValidator.isExist(testProject.id) }
 }

 @Test
 fun `getProjectById should throw when project not found`() {
  every { projectExistenceValidator.isExist(any()) } returns true
  every { projectsRepository.getById(testProject.id) } returns null
  every { projectExistenceValidator.isExist(testProject.id) } throws
          ProjectException.ProjectNotFoundException(testProject.id.toString())

  assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getById(testProject.id)
  }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when validation fails`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000010")
  val expectedMessage = "Project with ID $projectId was not found"

  every { projectExistenceValidator.isExist(any()) } returns true
  every { projectExistenceValidator.isExist(projectId) } throws
          ProjectException.ProjectNotFoundException(projectId.toString())

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify(exactly = 1) { projectExistenceValidator.isExist(projectId) }
  verify(exactly = 0) { projectsRepository.getById(any()) }
 }

 @Test
 fun `getProjectById should throw ProjectNotFoundException when repository returns null`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000011")
  val expectedMessage = "Project with ID $projectId was not found"

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { projectsRepository.getById(projectId) } returns null

  val exception = assertThrows<ProjectException.ProjectNotFoundException> {
   projectUseCase.getById(projectId)
  }

  assertEquals(expectedMessage, exception.message)
  verify { projectExistenceValidator.isExist(projectId) }
  verify { projectsRepository.getById(projectId) }
 }

 @Test
 fun `getProjectById should propagate unexpected repository exceptions`() {
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000012")
  val expectedException = RuntimeException("Database error")

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { projectsRepository.getById(projectId) } throws expectedException

  val exception = assertThrows<RuntimeException> {
   projectUseCase.getById(projectId)
  }

  assertEquals(expectedException, exception)
  verify { projectExistenceValidator.isExist(projectId) }
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

  every { projectExistenceValidator.isExist(zeroId) } throws
          ProjectException.ProjectValidationException("Invalid project ID")

  assertThrows<ProjectException.ProjectValidationException> {
   projectUseCase.getById(zeroId)
  }
 }

 @Test
 fun `getProjectById should validate before repository access`() {
  val projectId = UUID.randomUUID()
  val expectedProject = Project(projectId, "Test Project")

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { projectsRepository.getById(projectId) } returns expectedProject

  projectUseCase.getById(projectId)

  verifyOrder {
   projectExistenceValidator.isExist(projectId)
   projectsRepository.getById(projectId)
  }
 }

 @Test
 fun `getAll should return empty list when repository returns empty list`() {
  every { projectsRepository.getAll() } returns emptyList()

  val result = projectUseCase.getAll()

  assertTrue(result.isEmpty())
 }

 @Test
 fun `getAll should throw UserNotFoundException when repository returns null`() {
  every { projectsRepository.getAll() } returns null

  assertThrows<ProjectException.UserNotFoundException> {
   projectUseCase.getAll()
  }
 }

 @Test
 fun `getAll should call repository getAll exactly once`() {
  every { projectsRepository.getAll() } returns listOf(testProject)

  projectUseCase.getAll()

  verify(exactly = 1) { projectsRepository.getAll() }
 }

}