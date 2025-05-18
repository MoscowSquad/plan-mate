package domain.usecases.project

import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.InvalidProjectNameException
import domain.util.NoExistProjectException
import domain.util.NotAdminException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

class UpdateProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        updateProjectUseCase = UpdateProjectUseCase(projectsRepository)
    }

    @Test
    fun `should update project when valid data and user is admin`() = runTest {
        // Given
        val projectName = "Updated Project"
        coEvery { projectsRepository.updateProject(any()) } returns true

        // When
        val result = updateProjectUseCase(projectId, projectName)

        // Then
        assertTrue(result)
        coVerify {
            projectsRepository.updateProject(match {
                it.id == projectId && it.name == projectName
            })
        }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() = runTest {
        // Given
        val projectName = "Updated Project"

        // When & Then
        assertThrows<NotAdminException> {
            updateProjectUseCase(projectId, projectName)
        }
        coVerify(exactly = 0) { projectsRepository.updateProject(any()) }
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = ["  ", "\t", "\n"])
    fun `should throw InvalidProjectNameException when project name is blank`(name: String) = runTest {
        // When & Then
        assertThrows<InvalidProjectNameException> {
            updateProjectUseCase(projectId, name)
        }
        coVerify(exactly = 0) { projectsRepository.updateProject(any()) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`(): Unit = runTest {
        // Given
        val projectName = "Non-existent Project"
        coEvery { projectsRepository.updateProject(any()) } returns false

        // When & Then
        assertThrows<NoExistProjectException> {
            updateProjectUseCase(projectId, projectName)
        }
    }

    @Test
    fun `should update project with empty user list when valid data and admin`() = runTest {
        // Given
        val projectName = "Updated Project"
        coEvery { projectsRepository.updateProject(any()) } returns true

        // When
        val result = updateProjectUseCase(projectId, projectName)

        // Then
        assertTrue(result)
        coVerify {
            projectsRepository.updateProject(match {
                it.id == projectId && it.name == projectName
            })
        }
    }

    @Test
    fun `should pass correct Project object to repository`() = runTest {
        // Given
        val projectName = "Test Project"
        coEvery { projectsRepository.updateProject(any()) } returns true

        // When
        updateProjectUseCase(projectId, projectName)

        // Then
        coVerify {
            projectsRepository.updateProject(
                Project(
                    id = projectId,
                    name = projectName,
                )
            )
        }
    }
}