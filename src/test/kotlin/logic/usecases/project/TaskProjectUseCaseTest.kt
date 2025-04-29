package logic.usecases.project

import logic.models.Project
import logic.models.Task
import logic.repositoies.project.ProjectsRepository
import logic.repositoies.project.TaskProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import java.util.*
import org.junit.jupiter.api.Assertions.*
import utilities.exception.ValidateProjectExists

 class TaskProjectUseCaseTest {

  private lateinit var projectsRepository: ProjectsRepository
  private lateinit var tasksRepository: TaskProjectRepository
  private lateinit var adminProjectManagement: ProjectUseCase

  @BeforeEach
  fun setUp() {
   tasksRepository = mockk()
   projectsRepository = mockk()
   var usersRepository = mockk<Project>()
   var auditRepository = mockk<Project>()

   adminProjectManagement = ProjectUseCase(
       projectRepository = projectsRepository,
       validateProjectExists = ValidateProjectExists(projectsRepository)
   )
  }

//  @Test
//  fun `getTasksByProjectId should return tasks for given project`() {
//   // Given
//   val projectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
//   val expectedTasks = listOf(
//    Task(
//     id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
//     projectId = projectId,
//     title = "Task 1",
//     description = "Description 1",
//     stateId = UUID.fromString("00000000-0000-0000-0000-000000000003")
//    ),
//    Task(
//     id = UUID.fromString("00000000-0000-0000-0000-000000000004"),
//     projectId = projectId,
//     title = "Task 2",
//     description = "Description 2",
//     stateId = UUID.fromString("00000000-0000-0000-0000-000000000005")
//    )
//   )
//
//   // Mock the correct repository method
//   every { tasksRepository.getAllTasksByProjectId(projectId) } returns expectedTasks
//
//   // When - Call the correct method
//   val result = adminProjectManagement.getProjectById(projectId)
//
//   // Then
//   assertEquals(expectedTasks, result)
//   verify { tasksRepository.getAllTasksByProjectId(projectId) }
//  }

//  @Test
//  fun `getTasksByProjectId should return empty list when no tasks exist for project`() {
//   // Given
//   val projectId = UUID.fromString("00000000-0000-0000-0000-000000000006")
//   every { projectsRepository.getAllProjects() } returns listOf(
//    Project(id = projectId, name = "Test Project")
//    every { tasksRepository.getAllTasksByProjectId(projectId) } returns emptyList()
//
//   // When
//   val result = projectsRepository.getProjectById(projectId)
//
//   // Then
//   assertEquals(emptyList<Task>(), result)
//   verify { tasksRepository.getAllTasksByProjectId(projectId) }
//  }
}