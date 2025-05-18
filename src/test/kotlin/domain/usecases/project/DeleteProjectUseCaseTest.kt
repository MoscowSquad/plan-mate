package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectsRepository
import domain.util.NoExistProjectException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `should delete project successfully when user is admin and project exists`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { projectsRepository.deleteProject(projectId) } returns true

        // When
        val result = deleteProjectUseCase(projectId)

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`(): Unit = runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { projectsRepository.deleteProject(projectId) } returns false

        // When & Then
        assertThrows<NoExistProjectException> {
            deleteProjectUseCase(projectId)
        }
        coVerify(exactly = 1) { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `should include project ID in NoExistProjectException message`(): Unit = runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { projectsRepository.deleteProject(projectId) } returns false

        // When & Then
        val exception = assertThrows<NoExistProjectException> {
            deleteProjectUseCase(projectId)
        }
        assertThat(exception.message).contains(projectId.toString())
    }

    @Test
    fun `should return true when project is successfully deleted`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { projectsRepository.deleteProject(projectId) } returns true

        // When
        val result = deleteProjectUseCase(projectId)

        // Then
        assertThat(result).isTrue()
    }
}