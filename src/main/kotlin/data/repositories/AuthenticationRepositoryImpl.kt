package data.repositories
import logic.models.User
import logic.repositoies.AuthenticationRepository


class AuthenticationRepositoryImpl : AuthenticationRepository {
    override val users: MutableList<User> = mutableListOf()

    override fun addUser(user: User) {
        if (users.any { it.username.equals(user.username, ignoreCase = true) }) {
            throw IllegalStateException("Username '${user.username}' already exists")
        }
        users.add(user)
    }

    override fun findByUsername(username: String): User? {
        return users.find { it.username.equals(username, ignoreCase = true) }
    }
}