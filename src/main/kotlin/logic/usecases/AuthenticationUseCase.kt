package logic.usecases
import logic.models.Role
import logic.repositoies.AuthenticationRepository

class AuthenticationUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun validateUsername(username: String): Boolean {
        return authenticationRepository.users.none {
            it.username == username
        }
    }

    fun validateLogin(username: String, password: String): Role? {
        return authenticationRepository.users.find { user ->
            user.username == username &&
                    user.hashedPassword == password.hashCode().toString() // Note: Use proper hashing
        }?.role
    }
}