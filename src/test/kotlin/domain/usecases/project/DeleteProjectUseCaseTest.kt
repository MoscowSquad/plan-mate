package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectsRepository
import domain.util.NoExistProjectException
import domain.util.NotAdminException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        deleteProjectUseCase = DeleteProjectUseCase(projectsRepository)
    }

    @Test
    fun `should delete project successfully when user is admin and project exists`() = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = true
        coEvery { projectsRepository.deleteProject(projectId) } returns true

        // When
        val result = deleteProjectUseCase(projectId, isAdmin)

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`(): Unit = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = true
        coEvery { projectsRepository.deleteProject(projectId) } returns false

        // When & Then
        assertThrows<NoExistProjectException> {
            deleteProjectUseCase(projectId, isAdmin)
        }
        coVerify(exactly = 1) { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`(): Unit = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = false

        // When & Then
        assertThrows<NotAdminException> {
            deleteProjectUseCase(projectId, isAdmin)
        }
        coVerify(exactly = 0) { projectsRepository.deleteProject(any()) }
    }

    @Test
    fun `should include project ID in NoExistProjectException message`(): Unit = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = true
        coEvery { projectsRepository.deleteProject(projectId) } returns false

        // When & Then
        val exception = assertThrows<NoExistProjectException> {
            deleteProjectUseCase(projectId, isAdmin)
        }
        assertThat(exception.message).contains(projectId.toString())
    }

    @Test
    fun `should not call repository when user is not admin`(): Unit = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = false

        // When & Then
        assertThrows<NotAdminException> {
            deleteProjectUseCase(projectId, isAdmin)
        }
        coVerify(exactly = 0) { projectsRepository.deleteProject(any()) }
    }

    @Test
    fun `should return true when project is successfully deleted`() = runBlocking {
        // Given
        val projectId = UUID.randomUUID()
        val isAdmin = true
        coEvery { projectsRepository.deleteProject(projectId) } returns true

        // When
        val result = deleteProjectUseCase(projectId, isAdmin)

        // Then
        assertThat(result).isTrue()
    }
}