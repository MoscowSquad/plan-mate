package utilities.ValidatorForASPM

import logic.repositoies.adminSpecificProjectManagmanetRepository.UserProjectRepository
import utilities.exception.ProjectException
import java.util.*

class ValidateUserExists(private val userProjectRepository: UserProjectRepository) {
    fun validateUserExists(projectId: UUID, taskId: UUID, userId: UUID) {
        if (!userProjectRepository.userExists(projectId, taskId, userId)) {
            throw ProjectException.UserNotFoundException(userId)
        }
    }
}