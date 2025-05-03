package logic.usecases.user

import logic.models.User
import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException

class CreateUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, user: User): Boolean {
        if (role == UserRole.MATE) {
            throw UnauthorizedAccessException("Only admins can create users")
        }
        return userRepository.addUser(user)
    }
}
