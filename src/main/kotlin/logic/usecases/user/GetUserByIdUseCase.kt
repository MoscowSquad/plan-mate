package logic.usecases.user

import logic.models.User
import logic.repositories.UserRepository
import java.util.*

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    operator fun invoke(id: UUID): User {
        return userRepository.getUserById(id)
    }
}
