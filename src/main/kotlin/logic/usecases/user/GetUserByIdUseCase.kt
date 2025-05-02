package logic.usecases.user

import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import java.util.*

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(id: UUID): User {
        return User(
            id = UUID.randomUUID(),
            projectIds = listOf(),
            name = "",
            hashedPassword = "",
            role = UserRole.MATE
        )
    }
}