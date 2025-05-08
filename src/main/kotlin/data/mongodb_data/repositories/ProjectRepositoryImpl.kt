package data.mongodb_data.repositories


import data.mongodb_data.datasource.ProjectsDataSource
import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {
    override fun addProject(project: Project): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateProject(project: Project): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteProject(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectById(id: UUID): Project {
        TODO("Not yet implemented")
    }


}