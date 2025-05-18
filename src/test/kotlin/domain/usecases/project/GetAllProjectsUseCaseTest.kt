package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.NoUserLoginException
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetAllProjectsUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private val userId = UUID.randomUUID()
    private val mockUser = mockk<LoggedInUser> {
        every { id } returns userId
    }

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectsRepository)
        mockkObject(SessionManager)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(SessionManager)
    }

    @Test
    fun `should return all projects from repository when user is logged in`() = runTest {
        // Given
        val projects = listOf(
            Project(UUID.randomUUID(), "Project 1"),
            Project(UUID.randomUUID(), "Project 2"),
            Project(UUID.randomUUID(), "Project 3")
        )
        every { SessionManager.currentUser } returns mockUser
        coEvery { projectsRepository.getAllProjectsByUser(userId) } returns projects

        // When
        val result = getAllProjectsUseCase()

        // Then
        assertThat(result).isEqualTo(projects)
        coVerify(exactly = 1) { projectsRepository.getAllProjectsByUser(userId) }
    }

    @Test
    fun `should return empty list when repository has no projects`() = runTest {
        // Given
        every { SessionManager.currentUser } returns mockUser
        coEvery { projectsRepository.getAllProjectsByUser(userId) } returns emptyList()

        // When
        val result = getAllProjectsUseCase()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { projectsRepository.getAllProjectsByUser(userId) }
    }

    @Test
    fun `should throw NoUserLoginException when no user is logged in`() = runTest {
        // Given
        every { SessionManager.currentUser } returns null

        // When & Then
        assertThrows<NoUserLoginException> {
            getAllProjectsUseCase()
        }

        coVerify(exactly = 0) { projectsRepository.getAllProjectsByUser(any()) }
    }
}