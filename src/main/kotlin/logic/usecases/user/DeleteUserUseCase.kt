package logic.usecases.user

import logic.models.UserRole
import logic.repositoies.UserRepository
import utilities.UnauthorizedAccessException
import java.util.*
import kotlin.NoSuchElementException

class DeleteUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(role: UserRole, id: UUID) {
        return
    }
}
