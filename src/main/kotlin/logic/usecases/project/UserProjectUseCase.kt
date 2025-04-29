package logic.usecases.project

import logic.models.User
import logic.repositoies.project.UserProjectRepository
import logic.repositoies.project.exception.ProjectException.UserNotFoundException
import logic.repositoies.project.exception.validateProjectExists
import java.util.*
import kotlin.collections.List


class UserProjectUseCase(
    private val userProjectRepository: UserProjectRepository
) {
    fun getUserProjectById(projectId: UUID): List<User> {
        validateProjectExists(projectId)

        return userProjectRepository.getUsersByProject(projectId) ?:
                    throw UserNotFoundException(projectId.toString())
    }

}