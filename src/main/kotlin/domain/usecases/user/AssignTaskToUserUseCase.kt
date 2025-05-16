package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import java.util.*

class AssignTaskToUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(role: UserRole, taskId: UUID, userId: UUID): Boolean {
        return when (role) {
            UserRole.ADMIN -> userRepository.assignUserToTask(taskId, userId)
            else -> throw UnauthorizedAccessException()
        }
    }
}