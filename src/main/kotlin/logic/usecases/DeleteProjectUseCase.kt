package logic.usecases

import logic.repositoies.ProjectsRepository
import java.util.UUID

class DeleteProjectUseCase(private val projectRepository: ProjectsRepository) {
    operator fun invoke(id: UUID): Boolean {
        return projectRepository.delete(id)
    }
}