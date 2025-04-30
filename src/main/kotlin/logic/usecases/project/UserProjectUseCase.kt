package logic.usecases.project

import logic.models.User
import logic.repositoies.project.UserProjectRepository
import utilities.exception.ProjectException.UnauthorizedProjectAccessException
import utilities.exception.ValidateProjectExists
import utilities.exception.ValidateTaskProjectExists
import utilities.exception.ValidateUserExists
import java.util.*

class UserProjectUseCase(
    private val userProjectRepository: UserProjectRepository,
    private val validateProjectExists: ValidateProjectExists,
    private val validateTaskProjectExists: ValidateTaskProjectExists,
    private val validateUserExists: ValidateUserExists
) {
    fun getUserProjectById(userId: UUID, projectId: UUID, taskId: UUID): List<User>? {
        validateProjectExists.validateProjectExists(projectId)
        validateTaskProjectExists.validateTaskExists(projectId, taskId)
        validateUserExists.validateUserExists(projectId, taskId, userId)

        val users = userProjectRepository.getUsersByProjectId(projectId)

        // Check if requesting user has access to this project
        if (users?.none { it.id == userId } == true) {
            throw UnauthorizedProjectAccessException(userId, projectId)
        }

        return users
    }

    fun getUserByTaskId(projectId: UUID, taskId: UUID): User? {
        validateProjectExists.validateProjectExists(projectId)
        validateTaskProjectExists.validateTaskExists(projectId, taskId)

        return userProjectRepository.getUserByTaskId(projectId, taskId)
    }

    fun userExists(projectId: UUID, taskId: UUID, userId: UUID): Boolean {
        validateProjectExists.validateProjectExists(projectId)
        validateTaskProjectExists.validateTaskExists(projectId, taskId)

        return userProjectRepository.userExists(projectId, taskId, userId)
    }
}