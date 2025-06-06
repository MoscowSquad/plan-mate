package domain.usecases.project

import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.InvalidProjectNameException
import domain.util.NoExistProjectException
import java.util.*

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(id: UUID, name: String): Boolean {

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