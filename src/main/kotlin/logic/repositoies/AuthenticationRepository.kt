package logic.repositoies

import logic.models.User

interface AuthenticationRepository {
    val users: MutableList<User>
    fun addUser(user: User)
    fun findByUsername(username: String): User?
}