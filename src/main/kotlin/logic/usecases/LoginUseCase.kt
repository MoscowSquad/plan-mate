package logic.usecases

import logic.repositoies.AuthenticationRepository
import utilities.toMD5Hash

class LoginUseCase(
    private val authRepository: AuthenticationRepository
) {
    operator fun invoke(
        name: String,
        plainPassword: String
    ): Boolean {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }

        return authRepository.login(name, plainPassword.toMD5Hash())
    }
}