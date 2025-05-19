package data.mongodb_data.repositories

import com.google.common.truth.Truth.assertThat
import data.data_source.AuditLogDataSource
import data.data_source.ProjectsDataSource
import data.data_source.UserDataSource
import data.mongodb_data.dto.ProjectDto
import data.mongodb_data.util.ensureAdminPrivileges
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.AuditLog.AuditType
import domain.models.Project
import domain.models.User.UserRole
import domain.util.NoExistProjectException
import domain.util.NoUserLoginException
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProjectsRepositoryImplTest {
    private lateinit var projectsDataSource: ProjectsDataSource
    private lateinit var auditLogDataSource: AuditLogDataSource
    private lateinit var userDataSource: UserDataSource
    private lateinit var repository: ProjectsRepositoryImpl

    private val testProject1 = Project(
        UUID.fromString("11111111-1111-1111-1111-111111111111"),
        "Project 1"
    )
    private val testProject2 = Project(
        UUID.fromString("22222222-2222-2222-2222-222222222222"),
        "Project 2"
    )
    private val userId = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @BeforeEach
    fun setUp() {
        projectsDataSource = mockk(relaxed = true)
        auditLogDataSource = mockk(relaxed = true)
        userDataSource = mockk(relaxed = true)

        mockkObject(SessionManager)
        SessionManager.currentUser = LoggedInUser(
            userId,
            "testUser",
            UserRole.ADMIN,
            listOf(testProject1.id)
        )

        mockkStatic(::ensureAdminPrivileges)
        every { ensureAdminPrivileges() } just Runs

        repository = ProjectsRepositoryImpl(
            projectsDataSource,
            auditLogDataSource,
            userDataSource
        )

        val projectDto1 = ProjectDto(
            id = testProject1.id.toString(),
            name = testProject1.name
        )

        coEvery { projectsDataSource.addProject(any()) } returns true
        coEvery { projectsDataSource.updateProject(any()) } returns true
        coEvery { projectsDataSource.deleteProject(any()) } returns true
        coEvery { projectsDataSource.getAllProjects() } returns listOf(projectDto1)
        coEvery { projectsDataSource.getProjectById(testProject1.id) } returns projectDto1
    }

    @Test
    fun `addProject should add project and create audit log`() = runTest {
        // When
        val result = repository.addProject(testProject1)

        // Then
        assertThat(result).isTrue()
        coVerify { projectsDataSource.addProject(any()) }
        coVerify { userDataSource.assignUserToProject(testProject1.id, userId) }
        coVerify { auditLogDataSource.addLog(match {
            it.action.contains("Created") &&
            it.entityId == testProject1.id.toString() &&
            it.auditType == AuditType.PROJECT.toString()
        }) }
    }

    @Test
    fun `addProject should throw NoUserLoginException when no user is logged in`() = runTest {
        // Given
        SessionManager.currentUser = null

        // When & Then
        assertThrows<NoUserLoginException> {
            repository.addProject(testProject1)
        }
    }

    @Test
    fun `updateProject should update project and create audit log`() = runTest {
        // When
        val result = repository.updateProject(testProject1)

        // Then
        assertThat(result).isTrue()
        coVerify { projectsDataSource.updateProject(any()) }
        coVerify { auditLogDataSource.addLog(match {
            it.action.contains("Updated") &&
            it.entityId == testProject1.id.toString() &&
            it.auditType == AuditType.PROJECT.toString()
        }) }
    }

    @Test
    fun `updateProject should return false when update fails`() = runTest {
        // Given
        coEvery { projectsDataSource.updateProject(any()) } returns false

        // When
        val result = repository.updateProject(testProject1)

        // Then
        assertThat(result).isFalse()
        coVerify { auditLogDataSource.addLog(any()) }
    }

    @Test
    fun `deleteProject should delete project and create audit log`() = runTest {
        // When
        val result = repository.deleteProject(testProject1.id)

        // Then
        assertThat(result).isTrue()
        coVerify { projectsDataSource.deleteProject(testProject1.id) }
        coVerify { auditLogDataSource.addLog(match {
            it.action.contains("Deleted") &&
            it.entityId == testProject1.id.toString() &&
            it.auditType == AuditType.PROJECT.toString()
        }) }
    }

    @Test
    fun `deleteProject should return false when delete fails`() = runTest {
        // Given
        coEvery { projectsDataSource.deleteProject(any()) } returns false

        // When
        val result = repository.deleteProject(testProject1.id)

        // Then
        assertThat(result).isFalse()
        coVerify { auditLogDataSource.addLog(any()) }
    }

    @Test
    fun `getAllProjectsByUser should return projects for current user`() = runTest {
        // Given
        val projectDto1 = ProjectDto(id = testProject1.id.toString(), name = "Project 1")
        val projectDto2 = ProjectDto(id = testProject2.id.toString(), name = "Project 2")

        coEvery { projectsDataSource.getAllProjects() } returns listOf(projectDto1, projectDto2)
        SessionManager.currentUser = LoggedInUser(
            userId,
            "testUser",
            UserRole.ADMIN,
            listOf(testProject1.id)
        )

        // When
        val result = repository.getAllProjectsByUser(userId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(testProject1.id)
    }

    @Test
    fun `getAllProjectsByUser should throw NoExistProjectException when user is not logged in`() = runTest {
        // Given
        SessionManager.currentUser = null

        // When & Then
        assertThrows<NoExistProjectException> {
            repository.getAllProjectsByUser(userId)
        }
    }

    @Test
    fun `getProjectById should return project when it exists`() = runTest {
        // When
        val result = repository.getProjectById(testProject1.id)

        // Then
        assertThat(result.id).isEqualTo(testProject1.id)
        assertThat(result.name).isEqualTo(testProject1.name)
        coVerify { projectsDataSource.getProjectById(testProject1.id) }
    }

    @Test
    fun `admin privileges should be checked for critical operations`() = runTest {
        // When
        repository.addProject(testProject1)
        repository.updateProject(testProject1)
        repository.deleteProject(testProject1.id)

        // Then
        verify(exactly = 3) { ensureAdminPrivileges() }
    }
}