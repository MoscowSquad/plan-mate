package logic.usecases.auth

import logic.models.User
import logic.repositories.AuthenticationRepository
import logic.util.toMD5Hash

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, plainPassword: String): User {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }
        val hashedPassword = plainPassword.toMD5Hash()
        return authenticationRepository.login(username, hashedPassword)
    }
}
