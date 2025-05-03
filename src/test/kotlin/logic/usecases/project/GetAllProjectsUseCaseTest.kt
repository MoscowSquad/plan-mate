package logic.usecases.project

import io.mockk.every
import io.mockk.mockk
import logic.models.Project
import logic.repositories.ProjectsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAllProjectsUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectsRepository)
    }

    @Test
    fun `should return all projects from repository`() {
        // Given
        val projects = listOf(
            Project(UUID.randomUUID(), "Project 1"),
            Project(UUID.randomUUID(), "Project 2"),
            Project(UUID.randomUUID(), "Project 3")
        )
        every { projectsRepository.getAllProjects() } returns projects

        // When
        val result = getAllProjectsUseCase.invoke()

        // Then
        assertEquals(projects, result)
    }

    @Test
    fun `should return empty list when repository has no projects`() {
        // Given
        every { projectsRepository.getAllProjects() } returns emptyList()

        // When
        val result = getAllProjectsUseCase.invoke()

        // Then
        assertEquals(emptyList<Project>(), result)
    }
}