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

}