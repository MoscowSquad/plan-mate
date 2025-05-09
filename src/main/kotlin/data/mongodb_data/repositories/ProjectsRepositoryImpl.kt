package data.mongodb_data.repositories


import data.data_source.ProjectsDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toProject
import data.mongodb_data.util.executeInIO
import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {

    override fun addProject(project: Project) =
        executeInIO { projectsDataSource.addProject(project.toDto()) }

    override fun updateProject(project: Project) =
        executeInIO { projectsDataSource.updateProject(project.toDto()) }

    override fun deleteProject(id: UUID) =
        executeInIO { projectsDataSource.deleteProject(id) }

    override fun getAllProjects() =
        executeInIO {
            projectsDataSource.getAllProjects().map {
                it.toProject()
            }
        }

    override fun getProjectById(id: UUID) =
        executeInIO { projectsDataSource.getProjectById(id).toProject() }
}