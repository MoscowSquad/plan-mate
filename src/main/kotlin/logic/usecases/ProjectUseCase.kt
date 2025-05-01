package logic.usecases

import logic.models.Project
import logic.repositoies.ProjectsRepository
import utilities.exception.ProjectException.ProjectNotFoundException
import utilities.exception.ProjectException
import java.util.*

class ProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectExistenceValidator: ProjectExistenceValidator
) {
    fun getAllProjects(): List<Project> {
        return projectRepository.getAll() ?: throw ProjectException.UserNotFoundException()
    }

    fun getProjectById(projectId: UUID): Project {
        projectExistenceValidator.isExist(projectId)
        return projectRepository.getById(projectId)
            ?: throw ProjectNotFoundException(projectId.toString())
    }

}