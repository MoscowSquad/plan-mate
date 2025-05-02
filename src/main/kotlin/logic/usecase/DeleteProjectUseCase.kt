package logic.usecase

import logic.repositoies.ProjectsRepository
import utilities.NoExistProjectException
import utilities.NotAdminException
import java.util.UUID

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(id: UUID, isAdmin: Boolean): Boolean {
        if (!isAdmin) {
            throw NotAdminException("Only administrators can delete projects")
        }

        val success = projectsRepository.delete(id)
        if (!success) {
            throw NoExistProjectException(id)
        }

        return true
    }
}