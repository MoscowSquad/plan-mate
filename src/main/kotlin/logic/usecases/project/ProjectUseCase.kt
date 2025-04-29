package logic.usecases.project

import logic.models.Project
import logic.repositoies.project.ProjectsRepository
import java.util.*

class ProjectUseCase(
    private val projectRepository: ProjectsRepository
) {

    fun getAllProjects(): List<Project>
    {
       return projectRepository.getAllProjects() ?: throw IllegalStateException("All projects can't be null")
    }

    fun getProjectById(projectId: UUID): Project{
        return projectRepository.getProjectById(projectId) ?: throw IllegalStateException("Project with id $projectId can't be null")
    }
}