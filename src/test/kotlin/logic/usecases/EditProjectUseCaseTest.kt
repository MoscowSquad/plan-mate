package logic.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var editProjectUseCase: EditProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `should update project name when project exists`() {
        // Given
        val projectId = UUID.randomUUID()
        val originalName = "Original Project"
        val updatedName = "Updated Project"
        val existingProject = Project(id = projectId, name = originalName)
        val updatedProject = existingProject.copy(name = updatedName)

        every { projectRepository.findById(projectId) } returns existingProject
        every { projectRepository.save(any()) } returns updatedProject

        // When
        val result = editProjectUseCase(projectId, updatedName)

        // Then
        assertNotNull(result)
        assertEquals(updatedName, result?.name)
        assertEquals(projectId, result?.id)
        verify { projectRepository.save(match { it.name == updatedName && it.id == projectId }) }
    }

    @Test
    fun `should return null when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        val newName = "New Project Name"

        every { projectRepository.findById(projectId) } returns null

        // When
        val result = editProjectUseCase(projectId, newName)

        // Then
        assertNull(result)
        verify(exactly = 0) { projectRepository.save(any()) }
    }

    @Test
    fun `should not update project if name is the same`() {
        // Given
        val projectId = UUID.randomUUID()
        val projectName = "Project Name"
        val existingProject = Project(id = projectId, name = projectName)

        every { projectRepository.findById(projectId) } returns existingProject
        every { projectRepository.save(any()) } returns existingProject

        // When
        val result = editProjectUseCase(projectId, projectName)

        // Then
        assertNotNull(result)
        assertEquals(projectName, result?.name)
        verify { projectRepository.save(match { it.name == projectName && it.id == projectId }) }
    }

    @Test
    fun `should preserve other project properties when updating name`() {
        // Given
        val projectId = UUID.randomUUID()
        val originalName = "Original Project"
        val updatedName = "Updated Project"

        val existingProject = createProject(id = projectId, name = originalName)
        val updatedProject = existingProject.copy(name = updatedName)

        every { projectRepository.findById(projectId) } returns existingProject
        every { projectRepository.save(any()) } returns updatedProject

        // When
        val result = editProjectUseCase(projectId, updatedName)

        // Then
        assertNotNull(result)
        assertEquals(updatedName, result?.name)
        assertEquals(projectId, result?.id)

    }


    private fun createProject(
        id: UUID,
        name: String
    ): Project {
        return Project(id = id, name = name)
    }
}