package domain.usecases.project

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectsRepository
import domain.util.InvalidProjectNameException
import domain.util.NotAdminException
import domain.util.ProjectCreationFailedException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun `should create project when user is admin`() = runTest {
        // Given
        val projectName = "Test Project"
        coEvery { projectsRepository.addProject(any()) } returns true

        // When
        val result = createProjectUseCase(projectName)

        // Then
        assertThat(result).isNotNull()
        coVerify {
            projectsRepository.addProject(match { project ->
                project.name == projectName
            })
        }
    }

    @Test
    fun `should throw NotAdminException when user is not admin`(): Unit = runTest {
        // Given
        val projectName = "Test Project"

        // When & Then
        assertThrows<NotAdminException> {
            createProjectUseCase(projectName)
        }
    }

    @Test
    fun `should throw InvalidProjectNameException when project name is blank`(): Unit = runTest {
        // Given
        val projectName = "  "

        // When & Then
        assertThrows<InvalidProjectNameException> {
            createProjectUseCase(projectName)
        }
    }

    @Test
    fun `should throw InvalidProjectNameException when project name is empty`(): Unit = runTest {
        // Given
        val projectName = ""

        // When & Then
        assertThrows<InvalidProjectNameException> {
            createProjectUseCase(projectName)
        }
    }

    @Test
    fun `should throw ProjectCreationFailedException when repository fails to add project`(): Unit = runTest {
        // Given
        val projectName = "Test Project"
        coEvery { projectsRepository.addProject(any()) } returns false

        // When & Then
        assertThrows<ProjectCreationFailedException> {
            createProjectUseCase(projectName)
        }
    }

    @Test
    fun `should create project with valid UUID`() = runTest {
        // Given
        val projectName = "Test Project"
        coEvery { projectsRepository.addProject(any()) } returns true

        // When
        val result = createProjectUseCase(projectName)

        // Then
        try {
            UUID.fromString(result.toString())
            assertThat(true).isTrue()
        } catch (e: IllegalArgumentException) {
            Truth.assertWithMessage("Generated ID is not a valid UUID").that(false).isTrue()        }
    }

    @Test
    fun `should pass project with correct name to repository`() = runTest {
        // Given
        val projectName = "Specific Project Name"
        coEvery { projectsRepository.addProject(any()) } returns true

        // When
        createProjectUseCase(projectName)

        // Then
        coVerify {
            projectsRepository.addProject(match { project ->
                project.name == projectName
            })
        }
    }

    @Test
    fun `should return generated UUID when project is created successfully`() = runTest {
        // Given
        val projectName = "Test Project"
        coEvery { projectsRepository.addProject(any()) } returns true

        // When
        val result = createProjectUseCase(projectName)

        // Then
        assertThat(result).isInstanceOf(UUID::class.java)
    }
}