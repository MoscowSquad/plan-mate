package logic.usecases.project

import logic.models.Project
import logic.repositories.ProjectsRepository
import logic.util.InvalidProjectNameException
import logic.util.NotAdminException
import logic.util.ProjectCreationFailedException
import java.util.*

class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(name: String, isAdmin: Boolean): UUID {

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
        )


        val success = projectsRepository.addProject(project)
        if (!success) {
            throw ProjectCreationFailedException("Failed to create project")
        }

        return projectId
    }
}