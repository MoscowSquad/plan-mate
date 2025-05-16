package domain.usecases.user

import domain.models.User
import domain.repositories.UserRepository
import java.util.*

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: UUID): User {
        return userRepository.getUserById(id)
    }
}
