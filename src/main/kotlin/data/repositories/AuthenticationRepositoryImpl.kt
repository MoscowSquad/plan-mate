package data.repositories

import data.datasource.UserDataSource
import data.mappers.toDto
import data.mappers.toUser
import logic.models.User
import logic.models.UserRole
import logic.repositories.AuthenticationRepository
import logic.util.toMD5Hash
import java.util.*

class AuthenticationRepositoryImpl(
    private val dataSource: UserDataSource,
    private val passwordHasher: (String) -> String
) : AuthenticationRepository {

    val users = mutableListOf<User>()

    override fun register(user: User): User {
        require(users.none { it.name == user.name }) { "Username already exists" }
        users.add(user)
        dataSource.save(users.map { it.toDto() })
        return user
    }

    override fun login(name: String, password: String): Boolean {
        val hashedPassword = password.toMD5Hash() // Explicit MD5 hashing
        return users.any { user ->
            user.name == name && user.hashedPassword == hashedPassword
        }
    }

    fun loadUsersFromFile() {
        users.addAll(dataSource.fetch().map { it.toUser() })
    }

    fun createDefaultAdmin() {
        if (users.none { it.role == UserRole.ADMIN }) {
            register(
                User(
                    id = UUID.randomUUID(),
                    name = "admin",
                    hashedPassword = passwordHasher("admin123"),
                    role = UserRole.ADMIN,
                    projectIds = emptyList() // Added empty list for projectIds
                )
            )
        }
    }
}