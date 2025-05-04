package data.repositories

import data.datasource.UserDataSource
import data.mappers.toDto
import data.mappers.toUser
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import logic.util.toMD5Hash
import logic.util.UserNotFoundException
import presentation.session.LoggedInUser
import presentation.session.SessionManager
import java.util.*

class AuthenticationRepositoryImpl(
    private val dataSource: UserDataSource,
) : AuthenticationRepository {

    val users = mutableListOf<User>()

    init {
        users.addAll(dataSource.fetch().map { it.toUser() })
    }

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        users.add(user)
        dataSource.save(users.map { it.toDto() })
        SessionManager.currentUser = LoggedInUser(user.id, user.name, user.role, user.projectIds)
        return user
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
                    hashedPassword = "admin123".toMD5Hash(),
                    role = UserRole.ADMIN,
                    projectIds = emptyList()
                )
            )
        }
    }
}