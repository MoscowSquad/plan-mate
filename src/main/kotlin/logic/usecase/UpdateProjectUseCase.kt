package logic.usecase

import logic.models.Project
import logic.repositoies.ProjectsRepository
import utilities.InvalidProjectNameException
import utilities.NoExistProjectException
import utilities.NotAdminException
import java.util.UUID

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {

    fun invoke(id: UUID, name: String, userIds: List<UUID>, isAdmin: Boolean): Boolean {
        if (!isAdmin) {
            throw NotAdminException("Only administrators can update projects")
        }

        if (name.isBlank()) {
            throw InvalidProjectNameException("Project name cannot be empty")
        }

        val updatedProject = Project(
            id = id,
            name = name,
            userIds = userIds
        )

        val success = projectsRepository.update(updatedProject)
        if (!success) {
            throw NoExistProjectException(id)
        }

        return true
    }
}