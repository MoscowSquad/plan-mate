package domain.usecases.project

import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.InvalidProjectNameException
import domain.util.ProjectCreationFailedException
import java.util.*

class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(name: String): UUID {

        if (name.isBlank()) {
            throw InvalidProjectNameException()
        }

        val projectId = UUID.randomUUID()
        val project = Project(
            id = projectId,
            name = name,
        )

        val success = projectsRepository.addProject(project)
        if (!success) {
            throw ProjectCreationFailedException()
        }

        return projectId
    }
}