package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Role
import logic.models.User
import logic.repositoies.project.UserProjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import utilities.exception.ValidateProjectExists
import utilities.exception.ValidateTaskProjectExists
import utilities.exception.ValidateUserExists
import java.util.*

class UserProjectUseCaseTest {
 private lateinit var userProjectRepository: UserProjectRepository
 private lateinit var validateProjectExists: ValidateProjectExists
 private lateinit var validateTaskProjectExists: ValidateTaskProjectExists
 private lateinit var validateUserExists: ValidateUserExists
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
  validateProjectExists = mockk()
  validateTaskProjectExists = mockk()
  validateUserExists = mockk()

  userProjectUseCase = UserProjectUseCase(
   userProjectRepository,
   validateProjectExists,
   validateTaskProjectExists,
   validateUserExists
  )
 }

 @Test
 fun `getUserProjectById returns users when authorized`() {
  every { userProjectRepository.getUsersByProjectId(projectId) } returns users
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit
  every { validateUserExists.validateUserExists(projectId, taskId, userId) } returns Unit

  val result = userProjectUseCase.getUserProjectById(userId, projectId, taskId)

  assertEquals(users, result)
  verify {
   validateProjectExists.validateProjectExists(projectId)
   validateTaskProjectExists.validateTaskExists(projectId, taskId)
   validateUserExists.validateUserExists(projectId, taskId, userId)
  }
 }

 @Test
 fun `getUserProjectById throws when user not authorized`() {
  every { userProjectRepository.getUsersByProjectId(projectId) } returns users
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit
  every { validateUserExists.validateUserExists(projectId, taskId, any()) } returns Unit

  val unauthorizedUserId = UUID.randomUUID()

  assertThrows<ProjectException.UnauthorizedProjectAccessException> {
   userProjectUseCase.getUserProjectById(unauthorizedUserId, projectId, taskId)
  }
 }

 @Test
 fun `getUserByTaskId returns user when exists`() {
  every { userProjectRepository.getUserByTaskId(projectId, taskId) } returns user
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.getUserByTaskId(projectId, taskId)

  assertEquals(user, result)
  verify {
   validateProjectExists.validateProjectExists(projectId)
   validateTaskProjectExists.validateTaskExists(projectId, taskId)
  }
 }

 @Test
 fun `getUserByTaskId returns null when user not found`() {
  every { userProjectRepository.getUserByTaskId(projectId, taskId) } returns null
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.getUserByTaskId(projectId, taskId)

  assertNull(result)
 }

 @Test
 fun `userExists returns true when user exists`() {
  every { userProjectRepository.userExists(projectId, taskId, userId) } returns true
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.userExists(projectId, taskId, userId)

  assertTrue(result)
  verify {
   validateProjectExists.validateProjectExists(projectId)
   validateTaskProjectExists.validateTaskExists(projectId, taskId)
  }
 }

 @Test
 fun `userExists returns false when user does not exist`() {
  every { userProjectRepository.userExists(projectId, taskId, userId) } returns false
  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskProjectExists.validateTaskExists(projectId, taskId) } returns Unit

  val result = userProjectUseCase.userExists(projectId, taskId, userId)

  assertFalse(result)
 }
}