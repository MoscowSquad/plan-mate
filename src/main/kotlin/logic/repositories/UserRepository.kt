package logic.repositories

import logic.models.User
import java.util.*

interface UserRepository {
    fun addUser(user: User, hashedPassword: String): Boolean
    fun deleteUser(id: UUID): Boolean
    fun assignUserToProject(projectId: UUID, userId: UUID): Boolean
    fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean
    fun getUserById(id: UUID): User
    fun getAllUsers(): List<User>
}