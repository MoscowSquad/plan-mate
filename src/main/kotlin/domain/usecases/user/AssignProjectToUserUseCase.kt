package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import java.util.*

class AssignProjectToUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(role: UserRole, projectId: UUID, userId: UUID): Boolean {
        return when (role) {
            UserRole.ADMIN -> userRepository.assignUserToProject(projectId, userId)
            else -> throw UnauthorizedAccessException()
        }
    }
}
