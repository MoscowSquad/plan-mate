package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import logic.models.Project
import logic.repositoies.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class AddProjectUseCaseTest {
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var addProjectUseCase: AddProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        addProjectUseCase = AddProjectUseCase(projectRepository)
    }

    @Test
    fun `should create project with given name and random UUID`() {
        // Given
        val projectName = "Test Project"
        val projectSlot = slot<Project>()
        val savedProject = Project(UUID.randomUUID(), projectName)

        every { projectRepository.save(capture(projectSlot)) } returns savedProject

        // When
        val result = addProjectUseCase(projectName)

        // Then
        verify(exactly = 1) { projectRepository.save(any()) }
        assertThat(projectSlot.captured.name).isEqualTo(projectName)
        assertThat(projectSlot.captured.id).isNotNull()
        assertThat(result).isEqualTo(savedProject)
    }

    @Test
    fun `should return project returned by repository`() {
        // Given
        val projectName = "Test Project"
        val savedProject = Project(UUID.randomUUID(), projectName)

        every { projectRepository.save(any()) } returns savedProject

        // When
        val result = addProjectUseCase(projectName)

        // Then
        assertThat(result).isEqualTo(savedProject)
    }

    @Test
    fun `should pass created project to repository`() {
        // Given
        val projectName = "Test Project"
        val projectSlot = slot<Project>()

        every { projectRepository.save(capture(projectSlot)) } returns Project(UUID.randomUUID(), projectName)

        // When
        addProjectUseCase(projectName)

        // Then
        verify(exactly = 1) { projectRepository.save(any()) }
        with(projectSlot.captured) {
            assertThat(name).isEqualTo(projectName)
            assertThat(id).isNotNull()
        }
    }

    @Test
    fun `should generate different IDs for different projects`() {
        // Given
        val projectSlot1 = slot<Project>()
        val projectSlot2 = slot<Project>()

        every { projectRepository.save(capture(projectSlot1)) } returns Project(UUID.randomUUID(), "Project 1")

        // When
        addProjectUseCase("Project 1")


        every { projectRepository.save(capture(projectSlot2)) } returns Project(UUID.randomUUID(), "Project 2")

        addProjectUseCase("Project 2")

        // Then
        assertThat(projectSlot1.captured.id).isNotEqualTo(projectSlot2.captured.id)
    }
}