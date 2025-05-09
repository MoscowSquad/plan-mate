package data.mongodb_data.repositories

import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import data.data_source.UserDataSource
import data.mongodb_data.mappers.toTaskState
import data.mongodb_data.util.executeInIO
import kotlinx.coroutines.*
import logic.models.User
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun addUser(user: User): Boolean =
        executeInIO { userDataSource.addUser(user.toDto()) }


    override fun deleteUser(id: UUID): Boolean =
        executeInIO { userDataSource.deleteUser(id) }


    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean =
        executeInIO { userDataSource.assignUserToProject(projectId, userId) }

    override fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean =
        executeInIO { userDataSource.unassignUserFromProject(projectId, userId) }

    override fun getUserById(id: UUID): User =
        executeInIO { userDataSource.getUserById(id).toUser() }


    override fun getAllUsers(): List<User> =
        executeInIO {
            userDataSource.getAllUsers().toList().map {
                it.toUser()
            }
        }

}