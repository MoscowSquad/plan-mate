package data.repositories

import data.datasource.UserDataSource
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import utilities.toMD5Hash
import java.util.*

class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource
) : AuthenticationRepository {

    private val users = mutableListOf<User>()

    init {
        users.addAll(userDataSource.fetch())
    }

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        val userWithHashedPassword = user.copy(hashedPassword = user.hashedPassword.toMD5Hash())
        users.add(userWithHashedPassword)
        userDataSource.save(users)
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
                    hashedPassword = "admin123", // Will be hashed in register()
                    role = UserRole.ADMIN,
                    projectIds = emptyList()
                )
            )
        }
    }
}
