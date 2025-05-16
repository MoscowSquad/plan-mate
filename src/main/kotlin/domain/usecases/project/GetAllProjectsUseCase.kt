package domain.usecases.project

import data.session_manager.SessionManager
import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.NoUserLoginException

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(): List<Project> {
        SessionManager.currentUser?.let {
            return projectsRepository.getAllProjectsByUser(it.id)
        } ?: throw NoUserLoginException()
    }
}