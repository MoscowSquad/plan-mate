package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.models.User
import java.util.*

class UserDataSourceImpl(private val collection: MongoCollection<User>):UserDataSource {

    override suspend fun addUser(user: User): Boolean {
        collection.insertOne(user)
        return true
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        val filter = Filters.eq("id", id.toString())
        collection.deleteOne(filter)
        return true
    }

    override suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
        val filter = Filters.eq("id", userId)
        val user = collection.find(filter).firstOrNull() ?: return false
        val updatedProjectIds = user.projectIds.toMutableList().apply { add(projectId) }

        val updatedUser = user.copy(projectIds = updatedProjectIds)

        val updateResult = collection.replaceOne(filter, updatedUser)
        return updateResult.modifiedCount > 0
    }

    override suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: UUID): User {
        val filter = Filters.eq("id", id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoSuchElementException("User is not found")
    }

    override suspend fun getAllUsers(): List<User> {
        return collection.find().toList()
    }

}