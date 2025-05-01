package logic.usecases

import logic.models.Project
import logic.repositoies.ProjectsRepository
import java.util.UUID

class AddProjectUseCase(private val projectRepository: ProjectsRepository) {
    operator fun invoke(name: String): Project {
        val project = Project(
            id = UUID.randomUUID(),
            name = name
        )
        return projectRepository.save(project)
    }
}