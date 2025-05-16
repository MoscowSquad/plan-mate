package domain.usecases.project

import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.InvalidProjectNameException
import domain.util.NoExistProjectException
import domain.util.NotAdminException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        projectsRepository = mockk(relaxed = true)
        updateProjectUseCase = UpdateProjectUseCase(projectsRepository)
    }

    @Test
    fun `should update project when valid data and user is admin`() {
        // Given
        val projectName = "Updated Project"
        every { projectsRepository.updateProject(any()) } returns true

        // When
        val result = updateProjectUseCase.invoke(projectId, projectName, isAdmin = true)

        // Then
        assertTrue(result)
        verify {
            projectsRepository.updateProject(match {
                it.id == projectId && it.name == projectName
            })
        }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() {
        // Given
        val projectName = "Updated Project"

        // When & Then
        assertThrows<NotAdminException> {
            updateProjectUseCase.invoke(projectId, projectName, isAdmin = false)
        }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = ["  ", "\t", "\n"])
    fun `should throw InvalidProjectNameException when project name is blank`(name: String) {
        // When & Then
        assertThrows<InvalidProjectNameException> {
            updateProjectUseCase.invoke(projectId, name, isAdmin = true)
        }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`() {
        // Given
        val projectName = "Non-existent Project"
        every { projectsRepository.updateProject(any()) } returns false

        // When & Then
        assertThrows<NoExistProjectException> {
            updateProjectUseCase.invoke(projectId, projectName, isAdmin = true)
        }
    }

    @Test
    fun `should update project with empty user list when valid data and admin`() {
        // Given
        val projectName = "Updated Project"
        every { projectsRepository.updateProject(any()) } returns true

        // When
        val result = updateProjectUseCase.invoke(projectId, projectName, isAdmin = true)

        // Then
        assertTrue(result)
        verify {
            projectsRepository.updateProject(match {
                it.id == projectId && it.name == projectName
            })
        }
    }

    @Test
    fun `should pass correct Project object to repository`() {
        // Given
        val projectName = "Test Project"
        every { projectsRepository.updateProject(any()) } returns true

        // When
        updateProjectUseCase.invoke(projectId, projectName, isAdmin = true)

        // Then
        verify {
            projectsRepository.updateProject(
                Project(
                    id = projectId,
                    name = projectName,
                )
            )
        }
    }
}