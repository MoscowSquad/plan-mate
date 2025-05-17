package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import domain.models.Project
import domain.repositories.ProjectsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetProjectByIdUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectsRepository)
    }

    @Test
    fun `should return project when valid id is provided`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val expectedProject = Project(id = projectId, name = "Test Project")

        coEvery { projectsRepository.getProjectById(projectId) } returns expectedProject

        // When
        val result = getProjectByIdUseCase(projectId)

        // Then
        assertThat(result).isEqualTo(expectedProject)
        coVerify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should throw exception when project does not exist`() = runTest {
        // Given
        val projectId = UUID.randomUUID()

        coEvery { projectsRepository.getProjectById(projectId) } throws NoSuchElementException("Project not found")

        // When & Then
        assertThrows<NoSuchElementException> {
            getProjectByIdUseCase(projectId)
        }

        coVerify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should call repository with correct id`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val project = Project(id = projectId, name = "Test Project")

        coEvery { projectsRepository.getProjectById(projectId) } returns project

        // When
        getProjectByIdUseCase(projectId)

        // Then
        coVerify(exactly = 1) { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `should handle random UUID correctly`() = runTest {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        val project1 = Project(id = projectId1, name = "Project 1")
        val project2 = Project(id = projectId2, name = "Project 2")

        coEvery { projectsRepository.getProjectById(projectId1) } returns project1
        coEvery { projectsRepository.getProjectById(projectId2) } returns project2

        // When
        val result1 = getProjectByIdUseCase(projectId1)
        val result2 = getProjectByIdUseCase(projectId2)

        // Then
        assertThat(result1).isEqualTo(project1)
        assertThat(result2).isEqualTo(project2)
        coVerify(exactly = 1) { projectsRepository.getProjectById(projectId1) }
        coVerify(exactly = 1) { projectsRepository.getProjectById(projectId2) }
    }
}