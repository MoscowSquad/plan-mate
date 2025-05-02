package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.NoExistProjectException
import utilities.NotAdminException
import java.util.UUID

class DeleteProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectsRepository)
    }

    @Test
    fun `should delete project successfully when user is admin and project exists`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectsRepository.delete(projectId) } returns true

        // When
        val result = deleteProjectUseCase.invoke(projectId, isAdmin = true)

        // Then
        assertTrue(result)
        verify(exactly = 1) { projectsRepository.delete(projectId) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectsRepository.delete(projectId) } returns false

        // When & Then
        assertThrows<NoExistProjectException> {
            deleteProjectUseCase.invoke(projectId, isAdmin = true)
        }
        verify(exactly = 1) { projectsRepository.delete(projectId) }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() {
        // Given
        val projectId = UUID.randomUUID()

        // When & Then
        assertThrows<NotAdminException> {
            deleteProjectUseCase.invoke(projectId, isAdmin = false)
        }
        verify(exactly = 0) { projectsRepository.delete(any()) }
    }

    @Test
    fun `should include project ID in NoExistProjectException message`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectsRepository.delete(projectId) } returns false

        // When
        val exception = assertThrows<NoExistProjectException> {
            deleteProjectUseCase.invoke(projectId, isAdmin = true)
        }

        // Then
        assertTrue(exception.message?.contains(projectId.toString()) == true)
    }

    @Test
    fun `should contain specific message in NotAdminException`() {
        // Given
        val projectId = UUID.randomUUID()

        // When
        val exception = assertThrows<NotAdminException> {
            deleteProjectUseCase.invoke(projectId, isAdmin = false)
        }

        // Then
        assertTrue(exception.message?.contains("Only administrators can delete projects") == true)
    }
}