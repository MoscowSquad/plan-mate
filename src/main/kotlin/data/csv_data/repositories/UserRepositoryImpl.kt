package data.csv_data.repositories

import data.csv_data.datasource.UserDataSource
import data.csv_data.mappers.toUser
import logic.models.User
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    private val users = mutableListOf<User>()

    override fun addUser(user: User, hashedPassword: String): Boolean {
        if (users.any { it.id == user.id }) {
            throw IllegalArgumentException("User with id ${user.id} already exists")
        }
        return users.add(user)
    }

    override fun deleteUser(id: UUID): Boolean {
        val removed = users.removeIf { it.id == id }
        if (!removed) {
            throw NoSuchElementException("Cannot delete: User with id $id not found")
        }
        return true
    }

    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
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

    override fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean {
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

    override fun getUserById(id: UUID): User {
        return users.find { it.id == id }
            ?: throw NoSuchElementException("User with id $id not found")
    }

    override fun getAllUsers(): List<User> {
        return if (users.isEmpty()) {
            userDataSource.fetch()
                .map { it.toUser() }
                .also { users.addAll(it) }
        } else {
            users.toList()
        }
    }
}