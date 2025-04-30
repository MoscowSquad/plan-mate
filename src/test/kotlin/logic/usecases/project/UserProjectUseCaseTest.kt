package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.models.User
import logic.repositoies.project.UserProjectRepository
import logic.repositoies.project.ProjectsRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException.UnauthorizedProjectAccessException
import utilities.exception.ProjectException.ProjectNotFoundException
import utilities.exception.ValidateProjectExists
import java.util.*
import kotlin.test.assertEquals

class UserProjectUseCaseTest {
 // Test data
 private val testProjectId = UUID.randomUUID()
 private val authorizedUserId = UUID.randomUUID()
 private val unauthorizedUserId = UUID.randomUUID()
 private val testUsers = listOf(
  User(
      id = authorizedUserId, username = "User 1",
      hashedPassword = TODO(),
      role = TODO()
  ),
  User(
      id = UUID.randomUUID(), username = "User 2",
      hashedPassword = TODO(),
      role = TODO()
  )
 )
 private val testProject = Project(id = testProjectId, name = "Test Project")

 // Mocks
 private val mockUserProjectRepo: UserProjectRepository = mockk(relaxed = true) // Relaxed to avoid strict stubbing
 private val mockProjectsRepo: ProjectsRepository = mockk() // Strict mock (all methods must be stubbed)
 private lateinit var useCase: UserProjectUseCase

 @BeforeEach
 fun setup() {
  // Stub ALL methods of ProjectsRepository (crucial!)
  every { mockProjectsRepo.projectExists(any()) } returns true
  every { mockProjectsRepo.getAllProjects() } returns listOf(testProject)
  every { mockProjectsRepo.getProjectById(any()) } returns testProject

  // Initialize use case
  useCase = UserProjectUseCase(mockUserProjectRepo, ValidateProjectExists(mockProjectsRepo))
 }

 @Test
 fun `getUserProjectById returns users when user has access`() {
  // Given
  every { mockUserProjectRepo.getUsersByProject(testProjectId) } returns testUsers

  // When
  val result = useCase.getUserProjectById(authorizedUserId, testProjectId)

  // Then
  assertEquals(testUsers, result)
  verify {
   mockProjectsRepo.projectExists(testProjectId)
   mockUserProjectRepo.getUsersByProject(testProjectId)
  }
 }

 @Test
 fun `getUserProjectById throws when user is not in project`() {
  // Given
  every { mockUserProjectRepo.getUsersByProject(testProjectId) } returns testUsers

  // When / Then
  assertThrows<UnauthorizedProjectAccessException> {
   useCase.getUserProjectById(unauthorizedUserId, testProjectId)
  }
 }

 @Test
 fun `getUserProjectById throws when project does not exist`() {
  // Given
  every { mockProjectsRepo.projectExists(testProjectId) } returns false // Override default stub

  // When / Then
  assertThrows<ProjectNotFoundException> {
   useCase.getUserProjectById(authorizedUserId, testProjectId)
  }
 }
}