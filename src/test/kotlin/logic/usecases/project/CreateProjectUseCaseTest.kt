package logic.usecases.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.InvalidProjectNameException
import utilities.NotAdminException
import utilities.ProjectCreationFailedException
import java.util.*

class CreateProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectsRepository)
    }

    @Test
    fun `should create project with when user is admin`() {
        // Given
        val projectName = "Test Project"
        val userIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val isAdmin = true
        every { projectsRepository.add(any()) } returns true

        // When
        val result = createProjectUseCase.invoke(projectName, userIds, isAdmin)

        // Then
        assertThat(result).isNotNull()
        verify {
            projectsRepository.add(match { project ->
                project.name == projectName && project.userIds == userIds
            })
        }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`() {
        // Given
        val projectName = "Test Project"
        val userIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val isAdmin = false

        // When & Then
        assertThrows<NotAdminException> {
            createProjectUseCase.invoke(projectName, userIds, isAdmin)
        }
    }

    @Test
    fun `should throw InvalidProjectNameException when project name is blank`() {
        // Given
        val projectName = "  "
        val userIds = listOf(UUID.randomUUID())
        val isAdmin = true

        // When & Then
        assertThrows<InvalidProjectNameException> {
            createProjectUseCase.invoke(projectName, userIds, isAdmin)
        }
    }

    @Test
    fun `should throw InvalidProjectNameException when project name is empty`() {
        // Given
        val projectName = ""
        val userIds = listOf(UUID.randomUUID())
        val isAdmin = true

        // When & Then
        assertThrows<InvalidProjectNameException> {
            createProjectUseCase.invoke(projectName, userIds, isAdmin)
        }
    }

    @Test
    fun `should throw ProjectCreationFailedException when repository fails to add project`() {
        // Given
        val projectName = "Test Project"
        val userIds = listOf(UUID.randomUUID())
        val isAdmin = true
        every { projectsRepository.add(any()) } returns false

        // When & Then
        assertThrows<ProjectCreationFailedException> {
            createProjectUseCase.invoke(projectName, userIds, isAdmin)
        }
    }

    @Test
    fun `should create project with empty user list`() {
        // Given
        val projectName = "Test Project"
        val userIds = emptyList<UUID>()
        val isAdmin = true
        every { projectsRepository.add(any()) } returns true

        // When
        val result = createProjectUseCase.invoke(projectName, userIds, isAdmin)

        // Then
        assertThat(result).isNotNull()
        verify {
            projectsRepository.add(match { project ->
                project.name == projectName && project.userIds.isEmpty()
            })
        }
    }

    @Test
    fun `should return generated UUID when project is created successfully`() {
        // Given
        val projectName = "Test Project"
        val userIds = listOf(UUID.randomUUID())
        val isAdmin = true
        every { projectsRepository.add(any()) } returns true

        // When
        val result = createProjectUseCase.invoke(projectName, userIds, isAdmin)

        // Then
        assertThat(result).isInstanceOf(UUID::class.java)
    }
}