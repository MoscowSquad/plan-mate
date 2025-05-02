package logic.usecases.auth

import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import utilities.toMD5Hash
import java.util.*

class RegisterUseCase(
    private val authRepository: AuthenticationRepository
) {
    operator fun invoke(
        name: String,
        plainPassword: String,
        role: UserRole,
        projectIds: List<UUID> = emptyList()
    ): User {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }
        require(plainPassword.length >= 8) { "Password must be at least 8 characters" }

        return authRepository.register(
            User(
                id = UUID.randomUUID(),
                name = name,
                hashedPassword = plainPassword.toMD5Hash(),
                role = role,
                projectIds = projectIds
            )
        )
    }
}