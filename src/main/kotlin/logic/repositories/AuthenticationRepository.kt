package logic.repositories

import logic.models.User


interface AuthenticationRepository {
    fun register(user: User, hashedPassword: String): User
    fun login(name: String, password: String): User
}