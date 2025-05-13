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
            throw NotAdminException()
        }


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