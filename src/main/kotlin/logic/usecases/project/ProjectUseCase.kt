package logic.usecases.project

import logic.models.Project
import logic.repositoies.project.ProjectsRepository
import utilities.exception.ValidateProjectExists
import java.util.*

class ProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val validateProjectExists: ValidateProjectExists
) {

    fun getAllProjects(): List<Project>
    {
        validateProjectExists.isValid()
        return projectRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project?{
        validateProjectExists.isValidById(projectId)
        return projectRepository.getProjectById(projectId)
    }
}