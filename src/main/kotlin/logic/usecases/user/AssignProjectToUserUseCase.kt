package logic.usecases.user

import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import java.util.*

class AssignProjectToUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, projectId: UUID, userId: UUID): Boolean {
        if (role == UserRole.MATE) {
            throw UnauthorizedAccessException("Only admins can assign projects to users")
        }
        return userRepository.assignUserToProject(projectId, userId)
    }
}
