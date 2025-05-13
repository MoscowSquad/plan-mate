package logic.usecases.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.repositories.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetProjectByIdUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = true)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectsRepository)
    }

    @Test
    fun `should return project when valid id is provided`() {
        // Given
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = "Test Project")

        every { projectsRepository.getProjectById(projectId) } returns expectedProject

        // When
        val result = getProjectByIdUseCase.invoke(projectId)

        // Then
        assertThat(result).isEqualTo(expectedProject)
        verify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should throw exception when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()

        every { projectsRepository.getProjectById(projectId) } throws NoSuchElementException("Project not found")

        // When & Then
        assertThrows<NoSuchElementException> {
            getProjectByIdUseCase.invoke(projectId)
        }

        verify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should call repository with correct id`() {
        // Given
        val projectId = UUID.randomUUID()
        val project = Project(id = projectId, name = "Test Project")

        every { projectsRepository.getProjectById(projectId) } returns project

        // When
        getProjectByIdUseCase.invoke(projectId)

        // Then
        verify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should handle random UUID correctly`() {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        val project1 = Project(id = projectId1, name = "Project 1")
        val project2 = Project(id = projectId2, name = "Project 2")

        every { projectsRepository.getProjectById(projectId1) } returns project1
        every { projectsRepository.getProjectById(projectId2) } returns project2

        // When
        val result1 = getProjectByIdUseCase.invoke(projectId1)
        val result2 = getProjectByIdUseCase.invoke(projectId2)

        // Then
        assertThat(result1).isEqualTo(project1)
        assertThat(result2).isEqualTo(project2)
        verify(exactly = 1) { projectsRepository.getProjectById(projectId1) }
        verify(exactly = 1) { projectsRepository.getProjectById(projectId2) }
    }
}