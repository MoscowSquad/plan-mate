package logic.repositoies

import logic.models.User
import java.util.*

interface UserRepository {
    fun add(user: User): Boolean
    fun delete(id: UUID): Boolean
    fun assignToProject(projectId: UUID, userId: UUID): Boolean
    fun removeFromProject(projectId: UUID, userId: UUID): Boolean
    fun getById(id: UUID): User 
    fun getAll(): List<User>
}