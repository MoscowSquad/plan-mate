package logic.usecases.project

import data.session_manager.SessionManager
import logic.models.Project
import logic.repositories.ProjectsRepository
import logic.util.NoUserLoginException

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {

    operator fun invoke(): List<Project> {
        SessionManager.currentUser?.let {
            return projectsRepository.getAllProjectsByUser(it.id)
        } ?: throw NoUserLoginException()
    }
}