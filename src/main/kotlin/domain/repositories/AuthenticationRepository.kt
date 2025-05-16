package domain.repositories

import domain.models.User


interface AuthenticationRepository {
    suspend fun register(user: User, hashedPassword: String): User
    suspend fun login(name: String, password: String): User
}