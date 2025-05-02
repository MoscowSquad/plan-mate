package logic.usecases.user

import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import utilities.UnauthorizedAccessException

class GetAllUsersUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole): List<User> {
        return listOf()
    }
}
