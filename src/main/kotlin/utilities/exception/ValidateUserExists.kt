package utilities.exception

import logic.repositoies.project.TaskProjectRepository
import logic.repositoies.project.UserProjectRepository
import java.util.*

class ValidateUserExists(private val userProjectRepository: UserProjectRepository) {
    fun validateUserExists(projectId: UUID, taskId: UUID, userId: UUID) {
        if (!userProjectRepository(projectId, taskId)) {
            throw ProjectException.UserNotFoundException(userId)
        }
    }
}