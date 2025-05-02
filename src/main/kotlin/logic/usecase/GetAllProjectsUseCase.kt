package logic.usecase

import logic.models.Project
import logic.repositoies.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(): List<Project> {
        return projectsRepository.getAll()
    }
}