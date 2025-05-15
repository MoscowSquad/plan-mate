package logic.usecases.user

import logic.models.User
import logic.models.User.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import logic.util.toMD5Hash

class CreateUserUseCase(private val userRepository: UserRepository) {
    fun createNewUser(currentUserRole: UserRole, user: User, password: String): Boolean {
        return when (currentUserRole) {
            UserRole.ADMIN -> {
                val hashedPassword = password.toMD5Hash()
                userRepository.addUser(user, hashedPassword)
            }

            else -> throw UnauthorizedAccessException()
        }
    }
}
