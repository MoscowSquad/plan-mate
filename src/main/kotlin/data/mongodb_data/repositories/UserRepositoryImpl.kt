package data.mongodb_data.repositories

import data.mongodb_data.datasource.UserDataSource
import kotlinx.coroutines.*
import logic.models.User
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl (private val userDataSource: UserDataSource): UserRepository {
    private val scope = CoroutineScope(Dispatchers.IO)


    override fun addUser(user: User): Boolean {
        val deferred = scope.async {
            userDataSource.addUser(user) }
        return deferred.getCompleted()
    }

    override fun deleteUser(id: UUID): Boolean {
        val deferred = scope.async {
            userDataSource.deleteUser(id) }
        return deferred.getCompleted()
    }

    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean {
        val deferred = scope.async {
            userDataSource.assignUserToProject(projectId,userId) }
        return deferred.getCompleted()
    }

    override fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean {
        val deferred = scope.async {
            userDataSource.unassignUserFromProject(projectId,userId) }
        return deferred.getCompleted()
    }

    override fun getUserById(id: UUID): User {
        val deferred = scope.async {
            userDataSource.getUserById(id) }
        return deferred.getCompleted()
    }

    override fun getAllUsers(): List<User> {
        val deferred = scope.async {
            userDataSource.getAllUsers() }
        return deferred.getCompleted()
    }

}