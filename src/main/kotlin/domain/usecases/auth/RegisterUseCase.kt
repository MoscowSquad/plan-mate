package domain.usecases.auth

import domain.models.User
import domain.models.User.UserRole
import domain.repositories.AuthenticationRepository
import domain.util.isValidPasswordFormat
import domain.util.toMD5Hash
import java.util.*

class RegisterUseCase(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke(
        name: String,
        plainPassword: String,
        role: UserRole,
        projectIds: List<UUID> = emptyList(),
        taskIds: List<UUID> = emptyList()
    ): User {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }
        require(isValidPasswordFormat(plainPassword)) {
            "Password must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter"
        }

        return authRepository.register(
            User(
                id = UUID.randomUUID(),
                name = name,
                role = role,
                projectIds = projectIds,
                taskIds = taskIds
            ),
            toMD5Hash(plainPassword)
        )
    }
}