package logic.usecases.project

import logic.models.Project
import logic.repositoies.adminSpecificProjectManagmanetRepository.ProjectsRepository
import utilities.exception.ProjectException.ProjectNotFoundException
import utilities.ValidatorForASPM.ValidateProjectExists
import java.util.*

class ProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val validateProjectExists: ValidateProjectExists
) {
    fun getAllProjects(): List<Project> {
        validateProjectExists.validateProjectsExist()
        return projectRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project {
        validateProjectExists.validateProjectExists(projectId)
        return projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException(projectId.toString())
    }

}