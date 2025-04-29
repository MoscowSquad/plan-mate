package logic.repositoies.project.exception

import logic.repositoies.project.ProjectsRepository
import logic.repositoies.project.exception.ProjectException.ProjectNotFoundException
import java.util.*

private val projectsRepository: ProjectsRepository = ProjectsRepositoryImpl()

 fun validateProjectExists(projectId: UUID) {
        projectsRepository.getAllProjects()
            ?: throw ProjectNotFoundException("Project with $projectId  was not found.")
    }