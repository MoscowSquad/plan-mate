package domain.usecases.auth

import domain.models.User
import domain.repositories.AuthenticationRepository
import domain.util.toMD5Hash

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(username: String, plainPassword: String): User {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }
        require(plainPassword.length >= 8) { "Password must be at least 8 characters" }
        val hashedPassword = plainPassword.toMD5Hash()
        return authenticationRepository.login(username, hashedPassword)
    }
}
