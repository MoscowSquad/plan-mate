package logic.usecases
/*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Role
import logic.models.User
import logic.repositoies.UserProjectRepository
import logic.usecases.UserProjectUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import utilities.Validator.ProjectExistenceValidator
import utilities.Validator.TaskProjectExistenceValidator
import utilities.Validator.UserProjectExistenceValidator
import java.util.*

class UserProjectUseCaseTest {
 private lateinit var userProjectRepository: UserProjectRepository
 private lateinit var projectExistenceValidator: ProjectExistenceValidator
 private lateinit var taskProjectExistenceValidator: TaskProjectExistenceValidator
 private lateinit var userProjectExistenceValidator: UserProjectExistenceValidator
 private lateinit var userProjectUseCase: UserProjectUseCase

 private val projectId = UUID.fromString("00000000-0000-0000-0000-000000000015")
 private val taskId = UUID.fromString("00000000-0000-0000-0000-000000000016")
 private val userId = UUID.fromString("00000000-0000-0000-0000-000000000017")
 private val otherUserId = UUID.fromString("00000000-0000-0000-0000-000000000018")
 private val user = User(
  id = userId, username = "User 1",
  hashedPassword = "2024",
  role = Role.ADMIN
 )
 private val otherUser = User(
  id = otherUserId, username = "User 2",
  hashedPassword = "2025",
  role = Role.MATE
 )
 private val users = listOf(user, otherUser)

 @BeforeEach
 fun setUp() {
  userProjectRepository = mockk()
  projectExistenceValidator = mockk()
  taskProjectExistenceValidator = mockk()
  userProjectExistenceValidator = mockk()

  userProjectUseCase = UserProjectUseCase(
   userProjectRepository,
   projectExistenceValidator,
   taskProjectExistenceValidator,
   userProjectExistenceValidator
  )
 }

 @Test
 fun `getUserProjectById returns users when authorized`() {
  every { userProjectRepository.getUsersByProjectId(projectId) } returns users
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit
  every { userProjectExistenceValidator.validateUserExists(projectId, taskId, userId) } returns Unit

  val result = userProjectUseCase.getUserProjectById(userId, projectId, taskId)

  assertEquals(users, result)
  verify {
   projectExistenceValidator.validateProjectExists(projectId)
   taskProjectExistenceValidator.validateTaskExists(projectId, taskId)
   userProjectExistenceValidator.validateUserExists(projectId, taskId, userId)
  }
 }

 @Test
 fun `getUserProjectById throws when user not authorized`() {
  every { userProjectRepository.getUsersByProjectId(projectId) } returns users
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit
  every { userProjectExistenceValidator.validateUserExists(projectId, taskId, any()) } returns Unit

  val unauthorizedUserId = UUID.randomUUID()

  assertThrows<ProjectException.UnauthorizedProjectAccessException> {
   userProjectUseCase.getUserProjectById(unauthorizedUserId, projectId, taskId)
  }
 }

 @Test
 fun `getUserByTaskId returns user when exists`() {
  every { userProjectRepository.getByTaskId(projectId, taskId) } returns user
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.getUserByTaskId(projectId, taskId)

  assertEquals(user, result)
  verify {
   projectExistenceValidator.validateProjectExists(projectId)
   taskProjectExistenceValidator.validateTaskExists(projectId, taskId)
  }
 }

 @Test
 fun `getUserByTaskId returns null when user not found`() {
  every { userProjectRepository.getByTaskId(projectId, taskId) } returns null
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.getUserByTaskId(projectId, taskId)

  assertNull(result)
 }

 @Test
 fun `userExists returns true when user exists`() {
  every { userProjectRepository.userExists(projectId, taskId, userId) } returns true
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.userExists(projectId, taskId, userId)

  assertTrue(result)
  verify {
   projectExistenceValidator.validateProjectExists(projectId)
   taskProjectExistenceValidator.validateTaskExists(projectId, taskId)
  }
 }

 @Test
 fun `userExists returns false when user does not exist`() {
  every { userProjectRepository.userExists(projectId, taskId, userId) } returns false
  every { projectExistenceValidator.validateProjectExists(projectId) } returns Unit
  every { taskProjectExistenceValidator.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.userExists(projectId, taskId, userId)

  assertFalse(result)
 }
}

 */