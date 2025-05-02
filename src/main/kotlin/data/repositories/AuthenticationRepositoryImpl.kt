package data.repositories

import data.datasource.UserDataSource
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import utilities.toMD5Hash
import java.io.File
import java.util.*

class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource
) : AuthenticationRepository {

    val users = mutableListOf<User>()

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        val userWithHashedPassword = user.copy(hashedPassword = user.hashedPassword.toMD5Hash())
        users.add(userWithHashedPassword)
        return userWithHashedPassword
    }

    override fun login(name: String, password: String): Boolean {
        val hashedPassword = password.toMD5Hash()
        return users.any { it.name == name && it.hashedPassword == hashedPassword }
    }

    fun createDefaultAdmin() {
        if (users.none { it.role == UserRole.ADMIN }) {
            register(
                User(
                    id = UUID.randomUUID(),
                    name = "admin",
                    hashedPassword = "admin123", // This will be hashed in register()
                    role = UserRole.ADMIN,
                    projectIds = emptyList()
                )
            )
        }
    }
}