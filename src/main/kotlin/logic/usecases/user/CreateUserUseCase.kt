package logic.usecases.user

import logic.models.User
import logic.models.User.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException

class CreateUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, user: User): Boolean {
        return when (role) {
            UserRole.ADMIN -> userRepository.addUser(user)
            else -> throw UnauthorizedAccessException()
        }
    }
}
