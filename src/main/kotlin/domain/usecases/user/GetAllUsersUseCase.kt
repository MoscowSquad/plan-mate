package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException

class GetAllUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(role: UserRole): List<User> {
        if (role != UserRole.ADMIN) {
            throw UnauthorizedAccessException()
        }
        return userRepository.getAllUsers()
    }
}
