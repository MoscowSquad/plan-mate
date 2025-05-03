package logic.usecases.user

import logic.models.UserRole
import logic.repositories.UserRepository
import logic.util.UnauthorizedAccessException
import java.util.*
import kotlin.NoSuchElementException

class DeleteUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, id: UUID) {
        if (role == UserRole.MATE) {
            throw UnauthorizedAccessException("Only admins can delete users")
        }
        val success = userRepository.deleteUser(id)
        if (!success) {
            throw NoSuchElementException("User with id $id not found")
        }
    }
}
