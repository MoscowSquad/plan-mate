package domain.usecases.user

import domain.models.User.UserRole
import domain.repositories.UserRepository
import domain.util.UnauthorizedAccessException
import java.util.*

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(role: UserRole, id: UUID) {
        if (role != UserRole.ADMIN) {
            throw UnauthorizedAccessException()
        }

        val success = userRepository.deleteUser(id)
        if (!success) {
            throw NoSuchElementException("User with ID $id not found.")
        }
    }
}
