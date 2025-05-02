package logic.usecases

import logic.repositoies.AuthenticationRepository

class LoginUseCase(
    private val authRepository: AuthenticationRepository,
    private val passwordHasher: (String) -> String
) {
    operator fun invoke(
        name: String,
        plainPassword: String
    ): Boolean {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(plainPassword.isNotBlank()) { "Password cannot be blank" }

        return authRepository.login(name, passwordHasher(plainPassword))
    }
}