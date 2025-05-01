package logic.usecases

import logic.models.User
import logic.repositoies.UserProjectRepository
import utilities.exception.ProjectException
import java.util.*

class UserProjectUseCase(
    private val userProjectRepository: UserProjectRepository,
    private val projectExistenceValidator: ProjectExistenceValidator,
) {
    fun getById(userId: UUID, projectId: UUID, taskId: UUID): List<User>? {
        projectExistenceValidator.isExist(projectId)
        return userProjectRepository.getByProjectId(projectId)
            ?: throw ProjectException.UserNotFoundException()

    }

    fun getByTaskId(projectId: UUID, taskId: UUID): User? {
        projectExistenceValidator.isExist(projectId)

        return userProjectRepository.getByTaskId(projectId,taskId)
            ?: throw ProjectException.UserNotFoundException()
    }

}