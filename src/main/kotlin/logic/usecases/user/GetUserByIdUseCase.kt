package logic.usecases.user

import logic.models.User
import logic.repositoies.UserRepository
import java.util.*

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    operator fun invoke(id: UUID): User {
        return userRepository.getById(id)
    }
}
