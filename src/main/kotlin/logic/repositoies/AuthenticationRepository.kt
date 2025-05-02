package logic.repositoies

import logic.models.User


interface AuthenticationRepository {
    fun register(user: User): User
    fun login(name: String, password: String): Boolean
}