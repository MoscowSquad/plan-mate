package utilities.ValidatorForASPM

import logic.repositoies.adminSpecificProjectManagmanetRepository.ProjectsRepository
import utilities.exception.ProjectException.ProjectNotFoundException
import java.util.*

open class ValidateProjectExists(private val projectsRepository: ProjectsRepository) {

    open fun validateProjectExists(projectId: UUID) {
        if (!projectsRepository.projectExists(projectId)) {
            throw ProjectNotFoundException(projectId.toString())
        }
    }

    fun validateProjectsExist() {
        if (projectsRepository.getAllProjects().isEmpty()) {
            throw ProjectNotFoundException("No projects exist in the system")
        }
    }
}