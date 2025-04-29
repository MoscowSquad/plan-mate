package utilities.exception

import logic.repositoies.project.ProjectsRepository
import utilities.exception.ProjectException.ProjectNotFoundException
import java.util.*

class ValidateProjectExists(private val projectsRepository: ProjectsRepository)
{
 fun isValidById(projectId: UUID) {
        projectsRepository.getAllProjects()
            ?: throw ProjectNotFoundException("Project with $projectId  was not found.")
    }
fun isValid() {
    projectsRepository.getAllProjects()
        ?: throw ProjectNotFoundException("Project with was not found.")
}}