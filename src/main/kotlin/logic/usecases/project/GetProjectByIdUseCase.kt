package logic.usecases.project

import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.UUID

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(id: UUID): Project {
        return projectsRepository.getById(id)
    }
}