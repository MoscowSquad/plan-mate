package domain.usecases.user

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import domain.util.toMD5Hash

class CreateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(currentUserRole: UserRole, user: User, password: String): Boolean {
        return when (currentUserRole) {
            UserRole.ADMIN -> {
                val hashedPassword = password.toMD5Hash()
                userRepository.addUser(user, hashedPassword)
            }

            else -> throw UnauthorizedAccessException()
        }
    }
}
