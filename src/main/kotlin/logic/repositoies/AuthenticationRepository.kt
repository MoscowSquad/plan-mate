package logic.repositoies  // Fixed typo in package name

import logic.models.User

interface AuthenticationRepository {
    val users: MutableList<User>  // Fixed "HutableList" to "MutableList"
    fun addUser(user: User)  // Return type is Unit by default
    fun findByUsername(username: String): User?
}