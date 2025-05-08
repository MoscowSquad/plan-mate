package data.mongodb_data.datasource

import logic.models.User
import java.util.*

interface UserDataSource {
    suspend fun addUser(user: User):Boolean
    suspend fun deleteUser(id: UUID):Boolean
    suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
    suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean
    suspend fun getUserById(id: UUID): User
    suspend fun getAllUsers(): List<User>
}