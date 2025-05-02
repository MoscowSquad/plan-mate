package logic.usecase

import logic.models.Project
import logic.repositoies.ProjectsRepository
import java.util.UUID

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(id: UUID): Project {
        return projectsRepository.getById(id)
    }
}