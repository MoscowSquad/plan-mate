package logic.usecase

import logic.models.Project
import logic.repositoies.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {

    fun invoke(): List<Project> {
        return projectsRepository.getAll()
    }
}