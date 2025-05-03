package logic.usecases.project

import logic.models.Project
import logic.repositories.ProjectsRepository
import logic.util.InvalidProjectNameException
import logic.util.NoExistProjectException
import logic.util.NotAdminException
import java.util.*

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(id: UUID, name: String, isAdmin: Boolean): Boolean {
        if (!isAdmin) {
            throw NotAdminException("Only administrators can update projects")
        }

        if (name.isBlank()) {
            throw InvalidProjectNameException("Project name cannot be empty")
        }

        val updatedProject = Project(
            id = id,
            name = name,
        )

        val success = projectsRepository.updateProject(updatedProject)
        if (!success) {
            throw NoExistProjectException(id)
        }

        return true
    }
}