package logic.usecases

import logic.models.Project
import logic.repositoies.ProjectsRepository
import java.util.UUID

class EditProjectUseCase(private val projectRepository: ProjectsRepository) {
    operator fun invoke(id: UUID, name: String): Project? {
        val existingProject = projectRepository.findById(id) ?: return null
        val updatedProject = existingProject.copy(name = name)
        return projectRepository.save(updatedProject)
    }
}