package data.repositories

import com.google.common.truth.Truth.assertThat
import data.csv_data.datasource.ProjectDataSource
import data.csv_data.mappers.toDto
import data.csv_data.repositories.ProjectsRepositoryImpl
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.Project
import domain.models.User.UserRole
import domain.util.ProjectNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProjectsRepositoryImplTest {
    private lateinit var dataSource: ProjectDataSource
    private lateinit var repository: ProjectsRepositoryImpl
    private lateinit var testProject1: Project
    private lateinit var testProject2: Project
    private lateinit var testProject3: Project
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)

        testProject1 = Project(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Project 1")
        testProject2 = Project(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Project 2")
        testProject3 = Project(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Project 3")

        every { dataSource.fetch() } returns emptyList()

        mockkObject(SessionManager)
        SessionManager.currentUser = LoggedInUser(
            userId,
            "testUser",
            UserRole.MATE,
            listOf(testProject1.id)
        )

        repository = ProjectsRepositoryImpl(dataSource)
    }

    @Test
    fun `init should fetch projects from data source`() {
        // Given
        val projectDtos = listOf(testProject1.toDto(), testProject2.toDto())
        every { dataSource.fetch() } returns projectDtos

        // When
        val repository = ProjectsRepositoryImpl(dataSource)

        // Then
        verify { dataSource.fetch() }
        assertThat(repository.projects.size).isEqualTo(2)
        assertThat(repository.projects).contains(testProject1)
        assertThat(repository.projects).contains(testProject2)
    }

    @Test
    fun `addProject should store project in repository`() = runBlocking {
        // When
        val result = repository.addProject(testProject1)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.projects).contains(testProject1)
        verify { dataSource.save(any()) }
    }

    @Test
    fun `addProject should handle multiple projects`() = runBlocking {
        // When
        repository.addProject(testProject1)
        repository.addProject(testProject2)

        // Then
        assertThat(repository.projects).contains(testProject1)
        assertThat(repository.projects).contains(testProject2)
        verify(exactly = 2) { dataSource.save(any()) }
    }

    @Test
    fun `updateProject should modify existing project`() = runBlocking {
        // Given
        repository.addProject(testProject1)
        val updatedProject = testProject1.copy(name = "Updated Project")

        // When
        val result = repository.updateProject(updatedProject)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.getProjectById(testProject1.id)).isEqualTo(updatedProject)
        verify(exactly = 2) { dataSource.save(any()) }
    }

    @Test
    fun `updateProject should return false when project does not exist`() = runBlocking {
        // Given
        val nonExistingProject = Project(UUID.randomUUID(), "Non-existing")

        // When
        val result = repository.updateProject(nonExistingProject)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `deleteProject should remove project from repository`() = runBlocking {
        // Given
        repository.addProject(testProject1)
        repository.addProject(testProject2)

        // When
        val result = repository.deleteProject(testProject1.id)

        // Then
        assertThat(result).isTrue()
        assertThat(repository.projects).doesNotContain(testProject1)
        assertThat(repository.projects).contains(testProject2)
        verify(exactly = 3) { dataSource.save(any()) }
    }

    @Test
    fun `deleteProject should return false when project does not exist`() = runBlocking {
        // When
        val result = repository.deleteProject(UUID.randomUUID())

        // Then
        assertThat(result).isFalse()
        verify(exactly = 0) { dataSource.save(any()) }
    }

    @Test
    fun `getAllProjectsByUser should return projects associated with user`() = runBlocking {
        // Given
        SessionManager.currentUser = LoggedInUser(
            userId,
            "testUser",
            UserRole.MATE,
            listOf(testProject1.id, testProject3.id)
        )
        repository.addProject(testProject1)
        repository.addProject(testProject2)
        repository.addProject(testProject3)

        // When
        val result = repository.getAllProjectsByUser(userId)

        // Then
        assertThat(result).containsExactly(testProject1, testProject3)
        assertThat(result).doesNotContain(testProject2)
    }

    @Test
    fun `getAllProjectsByUser should return empty list when user has no projects`() = runBlocking {
        // Given
        SessionManager.currentUser = LoggedInUser(
            userId,
            "testUser",
            UserRole.MATE,
            emptyList()
        )
        repository.addProject(testProject1)

        // When
        val result = repository.getAllProjectsByUser(userId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getProjectById should return project when it exists`() = runBlocking {
        // Given
        repository.addProject(testProject1)
        repository.addProject(testProject2)

        // When
        val result = repository.getProjectById(testProject2.id)

        // Then
        assertThat(result).isEqualTo(testProject2)
    }

    @Test
    fun `getProjectById should throw ProjectNotFoundException when project does not exist`(): Unit = runBlocking {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        assertThrows<ProjectNotFoundException> {
            repository.getProjectById(nonExistingId)
        }
    }

    @Test
    fun `addProject should not save to data source when project cannot be added`() = runBlocking {
        // Given
        val project = testProject1.copy()
        val mockList = mockk<MutableList<Project>>()
        every { mockList.add(any()) } returns false

        val field = repository.javaClass.getDeclaredField("projects")
        field.isAccessible = true
        field.set(repository, mockList)

        // When
        val result = repository.addProject(project)

        // Then
        assertThat(result).isFalse()
        verify(exactly = 0) { dataSource.save(any()) }
    }
}