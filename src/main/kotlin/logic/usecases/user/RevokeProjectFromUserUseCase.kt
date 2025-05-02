package logic.usecases.user

import logic.models.UserRole
import logic.repositoies.UserRepository
import java.util.*

class RevokeProjectFromUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(role: UserRole, projectId: UUID, userId: UUID): Boolean {
        return false
    }
}