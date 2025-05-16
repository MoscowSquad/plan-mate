package domain.usecases.project

import domain.models.Project
import domain.repositories.ProjectsRepository
import java.util.*

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(id: UUID): Project {
        return projectsRepository.getProjectById(id)
    }
}