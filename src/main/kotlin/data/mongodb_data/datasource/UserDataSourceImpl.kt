package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.UserDataSource
import data.mongodb_data.dto.UserDto
import domain.util.InvalidUserCreation
import domain.util.ProjectNotFoundException
import domain.util.UserNotFoundException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

class UserDataSourceImpl(
    private val collection: MongoCollection<UserDto>
) : UserDataSource {
    override suspend fun register(user: UserDto): UserDto {
        return if (collection.insertOne(user).wasAcknowledged()) user
        else throw InvalidUserCreation(user.name)
    }

    override suspend fun login(name: String, password: String): UserDto {
        val filter = Filters.and(
            Filters.eq(UserDto::name.name, name),
            Filters.eq(UserDto::hashedPassword.name, password)
        )
        return collection.find(filter).firstOrNull() ?: throw UserNotFoundException(name)
    }

    override suspend fun addUser(user: UserDto): Boolean {
        return collection.insertOne(user).wasAcknowledged()
    }

    override suspend fun deleteUser(id: UUID): Boolean {
        val filter = Filters.eq(UserDto::id.name, id.toString())
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
        val filter = Filters.eq(UserDto::id.name, userId.toString())
        val user = collection.find(filter).firstOrNull() ?: return false
        val updatedProjectIds = user.projectIds.toMutableList().apply { add(projectId.toString()) }
        val updatedUser = user.copy(projectIds = updatedProjectIds)

        return collection.replaceOne(filter, updatedUser).modifiedCount > 0
    }

    override suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean {
        val filter = Filters.eq(UserDto::id.name, userId)
        val user = collection.find(filter).firstOrNull() ?: throw UserNotFoundException("User is not found")

        if (!user.projectIds.contains(projectId.toString())) {
            throw ProjectNotFoundException(projectId)
        }

        val updatedProjectIds = user.projectIds.filterNot { it == projectId.toString() }
        val updatedUser = user.copy(projectIds = updatedProjectIds)

        return collection.replaceOne(filter, updatedUser).modifiedCount > 0

    }

    override suspend fun getUserById(id: UUID): UserDto {
        val filter = Filters.eq(UserDto::id.name, id.toString())
        return collection.find(filter).firstOrNull()
            ?: throw throw UserNotFoundException("User is not found")
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return collection.find().toList()
    }

    override suspend fun assignUserToTask(taskId: UUID, userId: UUID): Boolean {
        val filter = Filters.eq(UserDto::id.name, userId.toString())
        val user = collection.find(filter).firstOrNull() ?: return false
        val updatedTaskIds = user.projectIds.toMutableList().apply { add(taskId.toString()) }
        val updatedUser = user.copy(taskIds = updatedTaskIds)
        return collection.replaceOne(filter, updatedUser).modifiedCount > 0
    }

}