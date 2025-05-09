package data.mongodb_data.repositories

import data.csv_data.mappers.toDto
import data.csv_data.mappers.toUser
import data.data_source.UserDataSource
import data.mongodb_data.util.executeInIO
import logic.models.User
import logic.repositories.AuthenticationRepository
import logic.repositories.UserRepository
import presentation.session.LoggedInUser
import presentation.session.SessionManager
import java.util.*

class UserRepositoryImpl(private val userDataSource: UserDataSource) :
    UserRepository, AuthenticationRepository {

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

    override fun register(user: User) =
        executeInIO { userDataSource.register(user.toDto()).toUser() }.also {
            SessionManager.currentUser = LoggedInUser(
                id = user.id,
                role = user.role,
                name = user.name,
                projectIds = listOf()
            )
        }

    override fun login(name: String, password: String) =
        executeInIO { userDataSource.login(name, password).toUser() }
}