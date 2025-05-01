package logic.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Task
import logic.repositoies.TaskProjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.exception.ProjectException
import java.util.*

class TaskProjectUseCaseTest {
 private lateinit var taskRepository: TaskProjectRepository
 private lateinit var projectExistenceValidator: ProjectExistenceValidator
 private lateinit var taskUseCase: TaskProjectUseCase

 private val projectId = UUID.fromString("00000000-0000-0000-0000-000000000050")
 private val testTask = Task(
  id = UUID.fromString("00000000-0000-0000-0000-000000000051"),
  projectId = projectId,
  title = "Test Task",
  description = "Test Description",
  stateId = UUID.fromString("00000000-0000-0000-0000-000000000052")
 )

 @BeforeEach
 fun setUp() {
  taskRepository = mockk<TaskProjectRepository>()
  projectExistenceValidator = mockk()
  taskUseCase = TaskProjectUseCase(
   taskRepository,
   projectExistenceValidator,
  )
 }

 @Test
 fun `getTaskById should return task when exists`() {
  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getByProjectId(projectId, testTask.id) } returns testTask

  val result = taskUseCase.getByProjectId(projectId, testTask.id)

  assertEquals(testTask, result)
  verify {
   projectExistenceValidator.isExist(projectId)
  }
 }

 @Test
 fun `getTaskById should throw when task not found`() {
  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getByProjectId(projectId, testTask.id) } returns null

  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getByProjectId(projectId, testTask.id)
  }

  verify {
   projectExistenceValidator.isExist(projectId)
   taskRepository.getByProjectId(projectId, testTask.id)
  }
 }

 @Test
 fun `getAllTasksByProjectId should return tasks`() {
  val expectedTasks = listOf(testTask)
  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getAll(projectId) } returns expectedTasks

  val result = taskUseCase.getAllByProjectId(projectId)

  assertEquals(expectedTasks, result)
  verify {
   projectExistenceValidator.isExist(projectId)
   taskRepository.getAll(projectId)
  }
 }

 @Test
 fun `getSpecificTaskByProjectId should return task when both project and task exist`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000053")
  val taskId = UUID.fromString("00000000-0000-0000-0000-000000000054")
  val expectedTask = Task(
      id = taskId, projectId = projectId, title = "Test Task",
      description = "This task for adding new project",
      stateId =UUID.fromString("00000000-0000-0000-0000-000000000055")
  )

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getByProjectId(projectId, taskId) } returns expectedTask

  // When
  val result = taskUseCase.getByProjectId(projectId, taskId)

  // Then
  assertEquals(expectedTask, result)
  verify(exactly = 1) { projectExistenceValidator.isExist(projectId) }
  verify(exactly = 1) { taskRepository.getByProjectId(projectId, taskId) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when project exists but task doesn't`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000056")
  val taskId = UUID.fromString("00000000-0000-0000-0000-000000000057")

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getByProjectId(projectId, taskId) } returns null

  // When/Then
  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { projectExistenceValidator.isExist(projectId) }
  verify(exactly = 1) { taskRepository.getByProjectId(projectId, taskId) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when project doesn't exist`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000058")
  val taskId = UUID.fromString("00000000-0000-0000-0000-000000000059")

  every { projectExistenceValidator.isExist(projectId) } throws
          ProjectException.ProjectNotFoundException(projectId.toString())

  // When/Then
  assertThrows<ProjectException.ProjectNotFoundException> {
   taskUseCase.getByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { projectExistenceValidator.isExist(projectId) }
  verify(exactly = 0) { taskRepository.getByProjectId(any(), any()) }
 }

 @Test
 fun `getSpecificTaskByProjectId should throw when repository returns null after validation`() {
  // Given
  val projectId = UUID.fromString("00000000-0000-0000-0000-000000000060")
  val taskId = UUID.fromString("00000000-0000-0000-0000-000000000061")

  every { projectExistenceValidator.isExist(projectId) } returns true
  every { taskRepository.getByProjectId(projectId, taskId) } returns null

  // When/Then
  assertThrows<ProjectException.TaskNotFoundException> {
   taskUseCase.getByProjectId(projectId, taskId)
  }
  verify(exactly = 1) { projectExistenceValidator.isExist(projectId) }
  verify(exactly = 1) { taskRepository.getByProjectId(projectId, taskId) }
 }

}
