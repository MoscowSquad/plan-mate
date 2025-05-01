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

class DeleteProjectUseCaseTest {
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `should successfully delete project`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectRepository.delete(projectId) } returns true

        // When
        val result = deleteProjectUseCase(projectId)

        // Then
        assertTrue(result)
        verify(exactly = 1) { projectRepository.delete(projectId) }
    }

    @Test
    fun `should return false when project deletion fails`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectRepository.delete(projectId) } returns false

        // When
        val result = deleteProjectUseCase(projectId)

        // Then
        assertFalse(result)
        verify(exactly = 1) { projectRepository.delete(projectId) }
    }

    @Test
    fun `should return false when project does not exist`() {
        // Given
        val nonExistentProjectId = UUID.randomUUID()
        every { projectRepository.delete(nonExistentProjectId) } returns false

        // When
        val result = deleteProjectUseCase(nonExistentProjectId)

        // Then
        assertFalse(result)
        verify(exactly = 1) { projectRepository.delete(nonExistentProjectId) }
    }

    @Test
    fun `should pass provided ID to repository`() {
        // Given
        val projectId = UUID.randomUUID()

        // When
        deleteProjectUseCase(projectId)

        // Then
        verify(exactly = 1) { projectRepository.delete(projectId) }
    }
}