package data.csv_data.repositories

import data.csv_data.datasource.ProjectDataSource
import data.csv_data.mappers.toDto
import data.csv_data.mappers.toProject
import logic.models.Project
import logic.repositories.ProjectsRepository
import logic.util.ProjectNotFoundException
import java.util.*

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectsRepository {

    val projects = mutableListOf<Project>()

    init {
        projects.addAll(projectDataSource.fetch().map { it.toProject() })
    }

    override fun addProject(project: Project): Boolean {
        val added = projects.add(project)
        if (added) projectDataSource.save(projects.map { it.toDto() })
        return added
    }

    override fun updateProject(project: Project): Boolean {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index == -1) return false

        projects[index] = project
        projectDataSource.save(projects.map { it.toDto() })
        return true
    }

    override fun deleteProject(id: UUID): Boolean {
        val removed = projects.removeIf { it.id == id }
        if (removed) projectDataSource.save(projects.map { it.toDto() })
        return removed
    }

    override fun getAllProjects(): List<Project> {
        return projects.toList()
    }

    override fun getProjectById(id: UUID): Project {
        return projects.find { it.id == id } ?: throw ProjectNotFoundException(id)
    }
}
