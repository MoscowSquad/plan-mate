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
            throw NotAdminException()
        }

        if (name.isBlank()) {
            throw InvalidProjectNameException()
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