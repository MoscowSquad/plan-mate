package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

}