package logic.usecases

import logic.repositoies.ProjectsRepository
import java.util.UUID

class AssignUserToProjectUseCase(private val projectRepository: ProjectsRepository) {
    operator fun invoke(projectId: UUID, userId: UUID): Boolean {
        return projectRepository.assignUserToProject(projectId, userId)
    }
}