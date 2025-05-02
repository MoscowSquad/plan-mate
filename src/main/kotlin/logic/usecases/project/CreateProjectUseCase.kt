package logic.usecases.project

import logic.models.Project
import logic.repositories.ProjectsRepository
import utilities.InvalidProjectNameException
import utilities.NotAdminException
import utilities.ProjectCreationFailedException
import java.util.UUID

class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(name: String, userIds: List<UUID>, isAdmin: Boolean): UUID {

        if (!isAdmin) {
            throw NotAdminException("Only administrators can create projects")
        }


        if (name.isBlank()) {
            throw InvalidProjectNameException("Project name cannot be empty")
        }


        val projectId = UUID.randomUUID()
        val project = Project(
            id = projectId,
            name = name,
            userIds = userIds
        )


        val success = projectsRepository.add(project)
        if (!success) {
            throw ProjectCreationFailedException("Failed to create project")
        }

        return projectId
    }
}