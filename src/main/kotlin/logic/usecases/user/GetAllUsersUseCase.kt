package logic.usecases.user

import logic.models.User
import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException

class GetAllUsersUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole): List<User> {
        if (role == UserRole.MATE) {
            throw UnauthorizedAccessException()
        }
        return userRepository.getAllUsers()
    }
}
