package domain.usecases.project

import domain.repositories.ProjectsRepository
import domain.util.NoExistProjectException
import domain.util.NotAdminException
import java.util.*

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
    suspend operator fun invoke(id: UUID, isAdmin: Boolean): Boolean {
        if (!isAdmin) {
            throw NotAdminException()
        }

        val success = projectsRepository.deleteProject(id)
        if (!success) {
            throw NoExistProjectException(id)
        }

        return true
    }
}