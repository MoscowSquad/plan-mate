package data.mongodb_data.datasource

import logic.models.User
import java.util.*

class UserDataSourceImpl():UserDataSource {
    override suspend fun addUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: UUID): User {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

}