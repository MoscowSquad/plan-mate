package logic.usecases

import logic.models.Exceptions.UnauthorizedException
import logic.models.Project
import logic.models.Role
import logic.repositories.ProjectsRepository
import logic.repositories.UsersRepository
import java.util.*

class AdminUseCase(
    private val projectsRepository: ProjectsRepository,
    private val usersRepository: UsersRepository
) {
    fun createProject(userId: UUID, projectName: String): Project {
        val user = usersRepository.getUserById(userId)
        if (user.role != Role.ADMIN) {
            throw UnauthorizedException("Only admins can create projects")
        }

        val project = Project(
            projectId = UUID.randomUUID(),
            name = projectName,
            states = mutableListOf(),
            tasks = mutableListOf()
        )

        projectsRepository.saveProject(project)
        return project
    }

    fun editProject(userId: UUID, projectId: UUID, newName: String): Project {
        val user = usersRepository.getUserById(userId)
        if (user.role != Role.ADMIN) {
            throw UnauthorizedException("Only admins can edit projects")
        }

        val project = projectsRepository.getProjectById(projectId)
        val updatedProject = project.copy(name = newName)
        projectsRepository.saveProject(updatedProject)
        return updatedProject
    }

    fun deleteProject(userId: UUID, projectId: UUID) {
        val user = usersRepository.getUserById(userId)
        if (user.role != Role.ADMIN) {
            throw UnauthorizedException("Only admins can delete projects")
        }

        projectsRepository.getProjectById(projectId)
        projectsRepository.deleteProject(projectId)
    }

    fun deleteState(userId: UUID, projectId: UUID, stateId: UUID) {
        val user = usersRepository.getUserById(userId)
        if (user.role != Role.ADMIN) {
            throw UnauthorizedException("Only admins can delete states")
        }

        val project = projectsRepository.getProjectById(projectId)
        project.states.removeIf { it.id == stateId }
        projectsRepository.saveProject(project)
    }
}