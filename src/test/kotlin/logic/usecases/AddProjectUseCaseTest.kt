package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class AddProjectUseCaseTest {
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var addProjectUseCase: AddProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        addProjectUseCase = AddProjectUseCase(projectRepository)
    }

    @Test
    fun `should create project with given name and id`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = projectName)
        val savedProject = Project(projectId, projectName)

        every { projectRepository.save(expectedProject) } returns savedProject

        // When
        val result = addProjectUseCase(projectName, projectId)

        // Then
        verify(exactly = 1) { projectRepository.save(expectedProject) }
        assertThat(result).isEqualTo(savedProject)
    }

    @Test
    fun `should return project returned by repository`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = projectName)
        val savedProject = Project(projectId, projectName)

        every { projectRepository.save(expectedProject) } returns savedProject

        // When
        val result = addProjectUseCase(projectName, projectId)

        // Then
        assertThat(result).isEqualTo(savedProject)
    }

    @Test
    fun `should pass created project to repository`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = projectName)
        val savedProject = Project(projectId, projectName)

        every { projectRepository.save(expectedProject) } returns savedProject

        // When
        addProjectUseCase(projectName, projectId)

        // Then
        verify(exactly = 1) { projectRepository.save(expectedProject) }
    }

    @Test
    fun `should create project with correct properties`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = projectName)

        every { projectRepository.save(expectedProject) } returns expectedProject

        // When
        addProjectUseCase(projectName, projectId)

        // Then
        verify(exactly = 1) { projectRepository.save(expectedProject) }
    }

    @Test
    fun `should handle different project names`() {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        val expectedProject1 = Project(id = projectId1, name = "Project 1")
        val expectedProject2 = Project(id = projectId2, name = "Project 2")

        every { projectRepository.save(expectedProject1) } returns expectedProject1
        every { projectRepository.save(expectedProject2) } returns expectedProject2

        // When
        addProjectUseCase("Project 1", projectId1)
        addProjectUseCase("Project 2", projectId2)

        // Then
        verify(exactly = 1) { projectRepository.save(expectedProject1) }
        verify(exactly = 1) { projectRepository.save(expectedProject2) }
    }
}