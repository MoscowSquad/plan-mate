package logic.usecases.user

import logic.models.User.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import java.util.*

class AssignTaskToUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(role: UserRole, taskId: UUID, userId: UUID): Boolean {
        return when (role) {
            UserRole.ADMIN -> userRepository.assignUserToTask(taskId, userId)
            else -> throw UnauthorizedAccessException()
        }
    }
}