package logic.usecases

import io.mockk.*
import logic.models.Exceptions.UnauthorizedException
import logic.models.Project
import logic.models.Role
import logic.models.State
import logic.models.User
import logic.repositoies.ProjectsRepository
import logic.repositoies.UsersRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class AdminUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var adminUseCase: AdminUseCase
    private lateinit var adminUser: User
    private lateinit var mateUser: User

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        adminUseCase = AdminUseCase(projectsRepository, usersRepository)

        adminUser = User(UUID.randomUUID(), "admin", "hashedPassword", Role.ADMIN)
        mateUser = User(UUID.randomUUID(), "mate", "hashedPassword", Role.MATE)

        every { usersRepository.getUserById(adminUser.id) } returns adminUser
        every { usersRepository.getUserById(mateUser.id) } returns mateUser
    }

    @Test
    fun `admin should be able to create a new project`() {
        // Given
        val projectName = "New Project"
        every { projectsRepository.saveProject(any()) } just runs

        // When
        val project = adminUseCase.createProject(adminUser.id, projectName)

        // Then
        assertNotNull(project)
        assertEquals(projectName, project.name)
        verify { projectsRepository.saveProject(project) }
    }

    @Test
    fun `mate should not be able to create a new project`() {
        // Given
        val projectName = "Unauthorized Project"

        // When/Then
        assertFailsWith<UnauthorizedException> {
            adminUseCase.createProject(mateUser.id, projectName)
        }
        verify(exactly = 0) { projectsRepository.saveProject(any()) }
    }

    @Test
    fun `admin should be able to edit a project`() {
        // Given
        val projectId = UUID.randomUUID()
        val project = Project(projectId, "Old Name", mutableListOf(), mutableListOf())
        val newName = "Updated Project"

        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.saveProject(any()) } just runs

        // When
        val updatedProject = adminUseCase.editProject(adminUser.id, projectId, newName)

        // Then
        assertEquals(newName, updatedProject.name)
        verify { projectsRepository.saveProject(updatedProject) }
    }

    @Test
    fun `mate should not be able to edit a project`() {
        // Given
        val projectId = UUID.randomUUID()
        val newName = "Unauthorized Update"

        // When/Then
        assertFailsWith<UnauthorizedException> {
            adminUseCase.editProject(mateUser.id, projectId, newName)
        }
        verify(exactly = 0) { projectsRepository.saveProject(any()) }
    }

    @Test
    fun `admin should be able to delete a project`() {
        // Given
        val projectId = UUID.randomUUID()
        val project = Project(projectId, "Test Project", mutableListOf(), mutableListOf())

        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.deleteProject(projectId) } just runs

        // When
        adminUseCase.deleteProject(adminUser.id, projectId)

        // Then
        verify { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `mate should not be able to delete a project`() {
        // Given
        val projectId = UUID.randomUUID()

        // When/Then
        assertFailsWith<UnauthorizedException> {
            adminUseCase.deleteProject(mateUser.id, projectId)
        }
        verify(exactly = 0) { projectsRepository.deleteProject(any()) }
    }

    @Test
    fun `admin should be able to delete a state`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val state = State(stateId, "Test State", projectId)
        val project = Project(projectId, "Test Project", mutableListOf(state), mutableListOf())

        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.saveProject(any()) } just runs

        // When
        adminUseCase.deleteState(adminUser.id, projectId, stateId)

        // Then
        verify { projectsRepository.saveProject(project) }
        assertEquals(0, project.states.size)
    }

    @Test
    fun `mate should not be able to delete a state`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()

        // When/Then
        assertFailsWith<UnauthorizedException> {
            adminUseCase.deleteState(mateUser.id, projectId, stateId)
        }
        verify(exactly = 0) { projectsRepository.saveProject(any()) }
    }
}