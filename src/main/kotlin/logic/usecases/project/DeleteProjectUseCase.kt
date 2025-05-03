package logic.usecases.project

import logic.repositories.ProjectsRepository
import logic.util.NoExistProjectException
import logic.util.NotAdminException
import java.util.UUID

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
    operator fun invoke(id: UUID, isAdmin: Boolean): Boolean {
        if (!isAdmin) {
            throw NotAdminException("Only administrators can delete projects")
        }

        val success = projectsRepository.deleteProject(id)
        if (!success) {
            throw NoExistProjectException(id)
        }

        return true
    }
}