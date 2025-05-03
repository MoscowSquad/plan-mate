package data.repositories

import data.datasource.UserDataSource
import data.mappers.toDto
import data.mappers.toUser
import logic.util.toMD5Hash
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import logic.util.UserNotFoundException
import presentation.session.LoggedInUser
import presentation.session.SessionManager
import java.util.*

class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource
) : AuthenticationRepository {

    private val users = mutableListOf<User>()

    init {
        users.addAll(userDataSource.fetch().map { it.toUser() })
    }

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        val userWithHashedPassword = user.copy(hashedPassword = user.hashedPassword.toMD5Hash())
        users.add(userWithHashedPassword)
        userDataSource.save(users.map { it.toDto() })
        SessionManager.currentUser = LoggedInUser(user.id, user.name, user.role, user.projectIds)
        return userWithHashedPassword
    }

    override fun login(name: String, password: String): Boolean {
        val hashedPassword = password.toMD5Hash()
        val user = users.find { it.name == name && it.hashedPassword == hashedPassword }
            ?: throw UserNotFoundException(name)
        SessionManager.currentUser = LoggedInUser(user.id, user.name, user.role, user.projectIds)
        return true
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
