package data.repositories

import com.google.common.truth.Truth.assertThat
import logic.models.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utilities.ProjectNotFoundException
import java.util.UUID

class ProjectsRepositoryImplTest {

    private lateinit var projectsRepository: ProjectsRepositoryImpl
    private lateinit var testProject1: Project
    private lateinit var testProject2: Project
    private lateinit var testProject3: Project

    @BeforeEach
    fun setUp() {
        projectsRepository = ProjectsRepositoryImpl()

        // Create test projects with fixed UUIDs for consistency
        testProject1 = Project(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Project 1", listOf())
        testProject2 = Project(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Project 2", listOf())
        testProject3 = Project(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Project 3", listOf())
    }

    @Test
    fun `add should store project in repository`() {
        // When
        val result = projectsRepository.add(testProject1)

        // Then
        assertThat(result).isTrue()
        assertThat(projectsRepository.getAll()).containsExactly(testProject1)
    }

    @Test
    fun `add should handle multiple projects`() {
        // When
        projectsRepository.add(testProject1)
        projectsRepository.add(testProject2)

        // Then
        assertThat(projectsRepository.getAll()).containsExactly(testProject1, testProject2)
    }

    @Test
    fun `update should modify existing project`() {
        // Given
        projectsRepository.add(testProject1)
        val updatedProject = Project(testProject1.id, "Updated Project", listOf())

        // When
        val result = projectsRepository.update(updatedProject)

        // Then
        assertThat(result).isTrue()
        assertThat(projectsRepository.getById(testProject1.id)).isEqualTo(updatedProject)
    }

    @Test
    fun `update should return false when project does not exist`() {
        // Given
        val nonExistingProject = Project(UUID.randomUUID(), "Non-existing", listOf())

        // When
        val result = projectsRepository.update(nonExistingProject)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `delete should remove project from repository`() {
        // Given
        projectsRepository.add(testProject1)
        projectsRepository.add(testProject2)

        // When
        val result = projectsRepository.delete(testProject1.id)

        // Then
        assertThat(result).isTrue()
        assertThat(projectsRepository.getAll()).containsExactly(testProject2)
    }

    @Test
    fun `delete should return false when project does not exist`() {
        // When
        val result = projectsRepository.delete(UUID.randomUUID())

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `getAll should return empty list when repository is empty`() {
        // When
        val result = projectsRepository.getAll()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAll should return all projects in repository`() {
        // Given
        projectsRepository.add(testProject1)
        projectsRepository.add(testProject2)
        projectsRepository.add(testProject3)

        // When
        val result = projectsRepository.getAll()

        // Then
        assertThat(result).containsExactly(testProject1, testProject2, testProject3)
    }

    @Test
    fun `getById should return project when it exists`() {
        // Given
        projectsRepository.add(testProject1)
        projectsRepository.add(testProject2)

        // When
        val result = projectsRepository.getById(testProject2.id)

        // Then
        assertThat(result).isEqualTo(testProject2)
    }

    @Test
    fun `getById should throw ProjectNotFoundException when project does not exist`() {
        // Given
        val nonExistingId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<ProjectNotFoundException> {
            projectsRepository.getById(nonExistingId)
        }

        assertThat(exception.message).contains(nonExistingId.toString())
    }

    @Test
    fun `getAll should return a copy of the internal list`() {
        // Given
        projectsRepository.add(testProject1)

        // When - modify the returned list
        val projects = projectsRepository.getAll()
        projects.toMutableList().add(testProject2)

        // Then - internal repository should be unchanged
        assertThat(projectsRepository.getAll()).containsExactly(testProject1)
    }
}