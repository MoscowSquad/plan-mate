package logic.repositoies.project.exception

import logic.repositoies.project.ProjectsRepository
import logic.repositoies.project.exception.ProjectException.ProjectNotFoundException
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