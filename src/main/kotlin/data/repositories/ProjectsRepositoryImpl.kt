package data.repositories

import data.datasource.ProjectDataSource
import logic.models.Project
import logic.repositories.ProjectsRepository
import utilities.ProjectNotFoundException
import java.util.UUID

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
): ProjectsRepository {
    private val projects = mutableListOf<Project>()

    override fun add(project: Project): Boolean {
        return projects.add(project)
    }

    override fun update(project: Project): Boolean {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index == -1) return false

        projects[index] = project
        return true
    }

    override fun delete(id: UUID): Boolean {
        val initialSize = projects.size
        projects.removeIf { it.id == id }
        return projects.size < initialSize
    }

    override fun getAll(): List<Project> {
        return projects.toList()
    }

    override fun getById(id: UUID): Project {
        return projects.find { it.id == id } ?: throw ProjectNotFoundException(id)
    }
}