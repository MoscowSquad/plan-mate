package logic.usecases.project

import logic.models.Project
import logic.repositories.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(): List<Project> {
        return projectsRepository.getAllProjects()
    }
}