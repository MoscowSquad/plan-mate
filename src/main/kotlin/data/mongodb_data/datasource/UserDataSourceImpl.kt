package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import logic.models.User
import java.util.*

class UserDataSourceImpl(private val collection: MongoCollection<User>):UserDataSource {

    override suspend fun addUser(user: User): Boolean {
        collection.insertOne(user)
        return true
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
        val filter = Filters.eq("id", id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw NoSuchElementException("User is not found")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

}