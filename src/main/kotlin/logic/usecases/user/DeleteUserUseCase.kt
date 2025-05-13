package logic.usecases.user

import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import java.util.*

class DeleteUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, id: UUID) {
        if (role != UserRole.ADMIN) {
            throw UnauthorizedAccessException()
        }

        val success = userRepository.deleteUser(id)
        if (!success) {
            throw NoSuchElementException("User with ID $id not found.")
        }
    }
}
