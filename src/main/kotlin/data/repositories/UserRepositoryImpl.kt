package data.repositories

import logic.models.User
import logic.repositoies.UserRepository
import java.util.*

class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf<User>()

    override fun add(user: User): Boolean {
        if (users.any { it.id == user.id }) {
            throw IllegalArgumentException("User with id ${user.id} already exists")
        }
        return users.add(user)
    }

    override fun delete(id: UUID): Boolean {
        val removed = users.removeIf { it.id == id }
        if (!removed) {
            throw NoSuchElementException("Cannot delete: User with id $id not found")
        }
        return true
    }

    override fun assignToProject(projectId: UUID, userId: UUID): Boolean {
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) {
            throw NoSuchElementException("User with id $userId not found")
        }

        val user = users[index]
        if (projectId in user.projectIds) {
            throw IllegalStateException("Project $projectId is already assigned to user $userId")
        }

        users[index] = user.copy(projectIds = user.projectIds + projectId)
        return true
    }

    override fun removeFromProject(projectId: UUID, userId: UUID): Boolean {
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) {
            throw NoSuchElementException("User with id $userId not found")
        }

        val user = users[index]
        if (projectId !in user.projectIds) {
            throw IllegalStateException("Project $projectId is not assigned to user $userId")
        }

        users[index] = user.copy(projectIds = user.projectIds - projectId)
        return true
    }

    override fun getById(id: UUID): User {
        return users.find { it.id == id }
            ?: throw NoSuchElementException("User with id $id not found")
    }

    override fun getAll(): List<User> {
        return users.toList()
    }
}