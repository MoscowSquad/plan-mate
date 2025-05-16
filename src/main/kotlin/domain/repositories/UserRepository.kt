package domain.repositories

import domain.models.User
import java.util.*

interface UserRepository {
    suspend fun addUser(user: User, hashedPassword: String): Boolean
    suspend fun deleteUser(id: UUID): Boolean
    suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
    suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean
    suspend fun getUserById(id: UUID): User
    suspend fun getAllUsers(): List<User>
    suspend fun assignUserToTask(taskId: UUID, userId: UUID): Boolean
}