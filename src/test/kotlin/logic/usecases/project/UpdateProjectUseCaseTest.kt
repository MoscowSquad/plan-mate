package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.models.Project
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource
import utilities.InvalidProjectNameException
import utilities.NoExistProjectException
import utilities.NotAdminException
import java.util.UUID

class UpdateProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private val projectId = UUID.randomUUID()
    private val userId1 = UUID.randomUUID()
    private val userId2 = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = true)
        updateProjectUseCase = UpdateProjectUseCase(projectsRepository)
    }

    @Test
    fun `should update project when valid data and user is admin`() {
        // Given
        val projectName = "Updated Project"
        val userIds = listOf(userId1, userId2)
        every { projectsRepository.update(any()) } returns true

        // When
        val result = updateProjectUseCase.invoke(projectId, projectName, userIds, isAdmin = true)

        // Then
        assertTrue(result)
        verify {
            projectsRepository.update(match {
                it.id == projectId && it.name == projectName && it.userIds == userIds
            })
        }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() {
        // Given
        val projectName = "Updated Project"
        val userIds = listOf(userId1, userId2)

        // When & Then
        val exception = assertThrows<NotAdminException> {
            updateProjectUseCase.invoke(projectId, projectName, userIds, isAdmin = false)
        }
        assertEquals("Only administrators can update projects", exception.message)
        verify(exactly = 0) { projectsRepository.update(any()) }
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = ["  ", "\t", "\n"])
    fun `should throw InvalidProjectNameException when project name is blank`(name: String) {
        // Given
        val userIds = listOf(userId1)

        // When & Then
        val exception = assertThrows<InvalidProjectNameException> {
            updateProjectUseCase.invoke(projectId, name, userIds, isAdmin = true)
        }
        assertEquals("Project name cannot be empty", exception.message)
        verify(exactly = 0) { projectsRepository.update(any()) }
    }

    @Test
    fun `should throw NoExistProjectException when project does not exist`() {
        // Given
        val projectName = "Non-existent Project"
        val userIds = listOf(userId1)
        every { projectsRepository.update(any()) } returns false

        // When & Then
        val exception = assertThrows<NoExistProjectException> {
            updateProjectUseCase.invoke(projectId, projectName, userIds, isAdmin = true)
        }
        assertEquals("Project '$projectId' does not exist", exception.message)
    }

    @Test
    fun `should update project with empty user list when valid data and admin`() {
        // Given
        val projectName = "Updated Project"
        val userIds = emptyList<UUID>()
        every { projectsRepository.update(any()) } returns true

        // When
        val result = updateProjectUseCase.invoke(projectId, projectName, userIds, isAdmin = true)

        // Then
        assertTrue(result)
        verify {
            projectsRepository.update(match {
                it.id == projectId && it.name == projectName && it.userIds.isEmpty()
            })
        }
    }

    @Test
    fun `should pass correct Project object to repository`() {
        // Given
        val projectName = "Test Project"
        val userIds = listOf(userId1, userId2)
        every { projectsRepository.update(any()) } returns true

        // When
        updateProjectUseCase.invoke(projectId, projectName, userIds, isAdmin = true)

        // Then
        verify {
            projectsRepository.update(
                Project(
                    id = projectId,
                    name = projectName,
                    userIds = userIds
                )
            )
        }
    }
}