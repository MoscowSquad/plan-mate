package data.mongodb_data.repositories


import data.data_source.ProjectsDataSource
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toProject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addProject(project: Project): Boolean {
        val deferred = scope.async {
            projectsDataSource.addProject(project.toDto())
        }
        return deferred.getCompleted()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun updateProject(project: Project): Boolean {
        val deferred = scope.async {
            projectsDataSource.updateProject(project.toDto())
        }
        return deferred.getCompleted()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun deleteProject(id: UUID): Boolean {
        val deferred = scope.async {
            projectsDataSource.deleteProject(id)
        }
        return deferred.getCompleted()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllProjects(): List<Project> {
        val deferred = scope.async {
            projectsDataSource.getAllProjects().map {
                it.toProject()
            }
        }
        return deferred.getCompleted()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProjectById(id: UUID): Project {
        val deferred = scope.async {
            projectsDataSource.getProjectById(id).toProject()
        }
        return deferred.getCompleted()
    }


}