package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.repositoies.project.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class ProjectUseCaseTest{
 private lateinit var projectsRepository: ProjectsRepository
 private lateinit var adminProjectManagement: ProjectUseCase

 @BeforeEach
 fun setUp() {
  var projectsRepository = mockk<Project>()
  var tasksRepository = mockk<Project>()
  var adminProjectManagement = ProjectUseCase(
      projectRepository = TODO(),
      validateProjectExists = TODO()
  )
 }

  @Test
  fun `getAllProjects should return all projects from repository`() {
   // Given
   val expectedProjects = listOf(
    Project(id = UUID.randomUUID(), name = "Project 1"),
    Project(id = UUID.randomUUID(), name = "Project 2")
   )
   every { projectsRepository.getAllProjects() } returns expectedProjects

   // When
   val result = adminProjectManagement.getAllProjects()

   // Then
   assertEquals(expectedProjects, result)
   verify { projectsRepository.getAllProjects() }
  }

  @Test
  fun `getProjectById should return project when it exists`() {
   // Given
   val projectId = UUID.randomUUID()
   val expectedProject = Project(id = projectId, name = "Project 1")
   every { projectsRepository.getProjectById(projectId) } returns expectedProject

   // When
   val result = adminProjectManagement.getProjectById(projectId)

   // Then
   assertEquals(expectedProject, result)
   verify { projectsRepository.getProjectById(projectId) }
  }

  @Test
  fun `getProjectById should return null when project does not exist`() {
   // Given
   val projectId = UUID.fromString("non-existent")
   every { projectsRepository.getProjectById(any()) } returns null

   // When
   val result = adminProjectManagement.getProjectById(projectId)

   // Then
   assertEquals(null, result)
   verify { projectsRepository.getProjectById(any()) }
  }
 }