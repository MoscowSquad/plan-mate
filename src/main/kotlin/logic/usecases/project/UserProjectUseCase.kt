package logic.usecases.project

import logic.models.Project
import logic.models.User
import logic.repositoies.project.UserProjectRepository
import utilities.exception.ProjectException.UnauthorizedProjectAccessException
import utilities.exception.ValidateProjectExists
import java.util.*
import kotlin.collections.List


class UserProjectUseCase(
    private val userProjectRepository: UserProjectRepository,
    private val validateProjectExists: ValidateProjectExists
) {
    fun getUserProjectById(userId: UUID, projectId: UUID): List<User>? {
        validateProjectExists.validateProjectExists(projectId)

        val users = userProjectRepository.getUsersByProject(projectId)

        // Check if requesting user has access to this project
        if (users?.none { it.id == userId } == true) {
            throw UnauthorizedProjectAccessException(userId, projectId)
        }

        return users
    }

}