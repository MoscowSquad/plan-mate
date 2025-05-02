package logic.usecases

import logic.models.User
import logic.models.UserRole
import logic.repositoies.AuthenticationRepository
import java.util.*

class RegisterUseCase(
    private val authRepository: AuthenticationRepository,
    private val passwordHasher: (String) -> String
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
                hashedPassword = passwordHasher(plainPassword),
                role = role,
                projectIds = projectIds
            )
        )
    }
}