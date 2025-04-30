package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import logic.repositoies.project.TaskProjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import utilities.exception.ValidateProjectExists
import utilities.exception.ValidateTaskProjectExists
import java.util.*

class TaskProjectUseCaseTest {
 private lateinit var taskRepository: TaskProjectRepository
 private lateinit var validateProjectExists: ValidateProjectExists
 private lateinit var validateTaskExists: ValidateTaskProjectExists
 private lateinit var taskUseCase: TaskProjectUseCase

 private val projectId = UUID.randomUUID()
 private val testTask = Task(
  id = UUID.randomUUID(),
  projectId = projectId,
  title = "Test Task",
  description = "Test Description",
  stateId = UUID.randomUUID()
 )

 @BeforeEach
 fun setUp() {
  taskRepository = mockk<TaskProjectRepository>()
  validateProjectExists = mockk()
  validateTaskExists = mockk<ValidateTaskProjectExists>()
  taskUseCase = TaskProjectUseCase(
   taskRepository,
   validateProjectExists,
   validateTaskExists
  )

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskExists.validateTaskExists(projectId, any()) } returns Unit
 }

 @Test
 fun `getTaskById should return task when exists`() {
  every { taskRepository.getSpecificTaskByProjectId(projectId, testTask.id) } returns testTask

  val result = taskUseCase.getSpecificTaskByProjectId(projectId, testTask.id)

  assertEquals(testTask, result)
  verify {
   validateProjectExists.validateProjectExists(projectId)
   validateTaskExists.validateTaskExists(projectId, testTask.id)
  }
 }

 @Test
 fun `getTaskById should throw when task not found`() {
  every { taskRepository.getSpecificTaskByProjectId(projectId, testTask.id) } returns null
  every { validateTaskExists.validateTaskExists(projectId, testTask.id) } throws
          ProjectException.TaskNotFoundException(testTask.id.toString())

  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getSpecificTaskByProjectId(projectId, testTask.id)
  }
 }

 @Test
 fun `getAllTasksByProjectId should return tasks`() {
  val expectedTasks = listOf(testTask)
  every { taskRepository.getAllTasksByProjectId(projectId) } returns expectedTasks

  val result = taskUseCase.getAllTasksByProjectId(projectId)

  assertEquals(expectedTasks, result)
  verify { validateProjectExists.validateProjectExists(projectId) }
 }

 @Test
 fun `getSpecificTaskByProjectId should return task when both project and task exist`() {
  // Given
  val projectId = UUID.randomUUID()
  val taskId = UUID.randomUUID()
  val expectedTask = Task(
      id = taskId, projectId = projectId, title = "Test Task",
      description = "This task for adding new project",
      stateId =UUID.fromString("00000000-0000-0000-0000-000000000009")
  )

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskExists.validateTaskExists(projectId, taskId) } returns Unit
  every { taskRepository.getSpecificTaskByProjectId(projectId, taskId) } returns expectedTask

  // When
  val result = taskUseCase.getSpecificTaskByProjectId(projectId, taskId)

  // Then
  assertEquals(expectedTask, result)
  verify(exactly = 1) { validateProjectExists.validateProjectExists(projectId) }
  verify(exactly = 1) { validateTaskExists.validateTaskExists(projectId, taskId) }
  verify(exactly = 1) { taskRepository.getSpecificTaskByProjectId(projectId, taskId) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when project exists but task doesn't`() {
  // Given
  val projectId = UUID.randomUUID()
  val taskId = UUID.randomUUID()

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskExists.validateTaskExists(projectId, taskId) } throws
          ProjectException.TaskNotFoundException(taskId.toString())
  every { taskRepository.getSpecificTaskByProjectId(projectId, taskId) } returns null

  // When/Then
  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getSpecificTaskByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { validateProjectExists.validateProjectExists(projectId) }
  verify(exactly = 1) { validateTaskExists.validateTaskExists(projectId, taskId) }
  verify(exactly = 0) { taskRepository.getSpecificTaskByProjectId(projectId, taskId) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when project doesn't exist`() {
  // Given
  val projectId = UUID.randomUUID()
  val taskId = UUID.randomUUID()

  every { validateProjectExists.validateProjectExists(projectId) } throws
          ProjectException.ProjectNotFoundException(projectId.toString())

  // When/Then
  assertThrows<ProjectException.ProjectNotFoundException> {
   taskUseCase.getSpecificTaskByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { validateProjectExists.validateProjectExists(projectId) }
  verify(exactly = 0) { validateTaskExists.validateTaskExists(any(), any()) }
  verify(exactly = 0) { taskRepository.getSpecificTaskByProjectId(any(), any()) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when repository returns null after validation`() {
  // Given
  val projectId = UUID.randomUUID()
  val taskId = UUID.randomUUID()

  every { validateProjectExists.validateProjectExists(projectId) } returns Unit
  every { validateTaskExists.validateTaskExists(projectId, taskId) } returns Unit
  every { taskRepository.getSpecificTaskByProjectId(projectId, taskId) } returns null

  // When/Then
  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getSpecificTaskByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { validateProjectExists.validateProjectExists(projectId) }
  verify(exactly = 1) { validateTaskExists.validateTaskExists(projectId, taskId) }
  verify(exactly = 1) { taskRepository.getSpecificTaskByProjectId(projectId, taskId) }
 }

}