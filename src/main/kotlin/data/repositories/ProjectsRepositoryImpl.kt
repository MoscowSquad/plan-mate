package data.repositories

import data.datasource.ProjectDataSource
import logic.models.Project
import logic.repositories.ProjectsRepository
import utilities.ProjectNotFoundException
import java.util.UUID

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectsRepository {

    private val projects = mutableListOf<Project>()

    init {
        projects.addAll(projectDataSource.fetch())
    }

    override fun add(project: Project): Boolean {
        val added = projects.add(project)
        if (added) projectDataSource.save(projects)
        return added
    }

    override fun update(project: Project): Boolean {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index == -1) return false

        projects[index] = project
        projectDataSource.save(projects)
        return true
    }

    override fun delete(id: UUID): Boolean {
        val removed = projects.removeIf { it.id == id }
        if (removed) projectDataSource.save(projects)
        return removed
    }

    override fun getAll(): List<Project> {
        return projects.toList()
    }

    override fun getById(id: UUID): Project {
        return projects.find { it.id == id } ?: throw ProjectNotFoundException(id)
    }
}
