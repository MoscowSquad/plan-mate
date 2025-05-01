package logic.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class AssignUserToProjectUseCaseTest {
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var assignUserToProjectUseCase: AssignUserToProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        assignUserToProjectUseCase = AssignUserToProjectUseCase(projectRepository)
    }

    @Test
    fun `should successfully assign user to project`() {
        // Given
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        every { projectRepository.assignUserToProject(projectId, userId) } returns true

        // When
        val result = assignUserToProjectUseCase(projectId, userId)

        // Then
        assertTrue(result)
        verify(exactly = 1) { projectRepository.assignUserToProject(projectId, userId) }
    }

    @Test
    fun `should return false when project assignment fails`() {
        // Given
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        every { projectRepository.assignUserToProject(projectId, userId) } returns false

        // When
        val result = assignUserToProjectUseCase(projectId, userId)

        // Then
        assertFalse(result)
        verify(exactly = 1) { projectRepository.assignUserToProject(projectId, userId) }
    }

    @Test
    fun `should return false when project does not exist`() {
        // Given
        val nonExistentProjectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        every { projectRepository.assignUserToProject(nonExistentProjectId, userId) } returns false

        // When
        val result = assignUserToProjectUseCase(nonExistentProjectId, userId)

        // Then
        assertFalse(result)
        verify(exactly = 1) { projectRepository.assignUserToProject(nonExistentProjectId, userId) }
    }

    @Test
    fun `should pass provided IDs to repository`() {
        // Given
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // When
        assignUserToProjectUseCase(projectId, userId)

        // Then
        verify(exactly = 1) { projectRepository.assignUserToProject(projectId, userId) }
    }
}