package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import java.util.*

class RemoveFromProjectUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(role: UserRole, projectId: UUID, userId: UUID): Boolean {
        if (role == UserRole.MATE) {
            throw UnauthorizedAccessException()
        }
        return userRepository.unassignUserFromProject(projectId, userId)
    }
}
