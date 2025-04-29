package logic.repositoies

import logic.models.User
import java.util.UUID

interface UsersRepository {
    fun getUserById(userId: UUID): User
}