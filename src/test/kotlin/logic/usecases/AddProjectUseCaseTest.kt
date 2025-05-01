package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
        val projectSlot = slot<Project>()
        val savedProject = Project(projectId, projectName)

        every { projectRepository.save(capture(projectSlot)) } returns savedProject

        // When
        val result = addProjectUseCase(projectName, projectId)

        // Then
        verify(exactly = 1) { projectRepository.save(any()) }
        assertThat(projectSlot.captured.name).isEqualTo(projectName)
        assertThat(projectSlot.captured.id).isEqualTo(projectId)
        assertThat(result).isEqualTo(savedProject)
    }

    @Test
    fun `should return project returned by repository`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val savedProject = Project(projectId, projectName)

        every { projectRepository.save(any()) } returns savedProject

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
        val projectSlot = slot<Project>()

        every { projectRepository.save(capture(projectSlot)) } returns Project(projectId, projectName)

        // When
        addProjectUseCase(projectName, projectId)

        // Then
        verify(exactly = 1) { projectRepository.save(any()) }
        with(projectSlot.captured) {
            assertThat(name).isEqualTo(projectName)
            assertThat(id).isEqualTo(projectId)
        }
    }

    @Test
    fun `should create project with correct properties`() {
        // Given
        val projectName = "Test Project"
        val projectId = UUID.randomUUID()
        val projectSlot = slot<Project>()

        every { projectRepository.save(capture(projectSlot)) } returns Project(projectId, projectName)

        // When
        addProjectUseCase(projectName, projectId)

        // Then
        assertThat(projectSlot.captured.id).isEqualTo(projectId)
        assertThat(projectSlot.captured.name).isEqualTo(projectName)
    }

    @Test
    fun `should handle different project names`() {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        val projectSlot1 = slot<Project>()
        val projectSlot2 = slot<Project>()

        every { projectRepository.save(capture(projectSlot1)) } returns Project(projectId1, "Project 1")

        // When
        addProjectUseCase("Project 1", projectId1)

        every { projectRepository.save(capture(projectSlot2)) } returns Project(projectId2, "Project 2")

        addProjectUseCase("Project 2", projectId2)

        // Then
        assertThat(projectSlot1.captured.name).isEqualTo("Project 1")
        assertThat(projectSlot2.captured.name).isEqualTo("Project 2")
    }
}