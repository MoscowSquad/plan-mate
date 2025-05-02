package data.repositories

import logic.models.User
import logic.models.UserRole
import logic.repositoies.UserRepository
import java.util.*

class UserRepositoryImpl : UserRepository {
    override fun add(user: User): Boolean {
        return false
    }

    override fun delete(id: UUID): Boolean {
        return false
    }

    override fun assignToProject(projectId: UUID, userId: UUID): Boolean {
        return false
    }

    override fun revokeFromProject(projectId: UUID, userId: UUID): Boolean {
        return false
    }

    override fun getById(id: UUID): User {
        return User(
            id = UUID.randomUUID(),
            name = "",
            hashedPassword = "",
            role = UserRole.MATE,
            projectIds = listOf()
        )
    }

    override fun getAll(): List<User> {
        return listOf()
    }
}