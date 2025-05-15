package logic.usecases.auth

import logic.models.User
import logic.models.User.UserRole
import logic.repositories.AuthenticationRepository
import logic.util.isValidPasswordFormat
import logic.util.toMD5Hash
import java.util.*

class RegisterUseCase(
    private val authRepository: AuthenticationRepository
) {
    operator fun invoke(
        name: String,
        plainPassword: String,
        role: UserRole,
        projectIds: List<UUID> = emptyList(),
        taskIds: List<UUID> = emptyList()
    ): User {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }
        require(plainPassword.isValidPasswordFormat()) {
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
            plainPassword.toMD5Hash()
        )
    }
}