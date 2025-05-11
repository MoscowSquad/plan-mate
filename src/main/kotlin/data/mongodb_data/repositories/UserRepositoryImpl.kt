package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.data_source.UserDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toUser
import data.mongodb_data.util.executeInIO
import di.LoggedInUser
import di.SessionManager
import kotlinx.datetime.Clock
import logic.models.AuditType
import logic.models.User
import logic.repositories.AuthenticationRepository
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val auditLogDataSource: AuditLogDataSource
) : UserRepository, AuthenticationRepository {

    override fun addUser(user: User): Boolean =
        executeInIO {
            val result = userDataSource.addUser(user.toDto())
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with id ${user.id} and name ${user.name} Added",
                    entityId = user.id.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            return@executeInIO result
        }

    override fun deleteUser(id: UUID): Boolean =
        executeInIO {
            val result = userDataSource.deleteUser(id)
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with id $id Deleted",
                    entityId = id.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            return@executeInIO result
        }

    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean =
        executeInIO {
            val result = userDataSource.assignUserToProject(projectId, userId)
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with id $userId Assigned to Project with id $projectId",
                    entityId = userId.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            return@executeInIO result
        }

    override fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean =
        executeInIO {
            val result = userDataSource.unassignUserFromProject(projectId, userId)
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with id $userId Unassigned from Project with id $projectId",
                    entityId = userId.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            return@executeInIO result
        }

    override fun getUserById(id: UUID): User =
        executeInIO { userDataSource.getUserById(id).toUser() }

    override fun getAllUsers(): List<User> =
        executeInIO {
            userDataSource.getAllUsers().toList().map {
                it.toUser()
            }
        }

    override fun register(user: User) =
        executeInIO {
            val result = userDataSource.register(user.toDto()).toUser()
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with id ${user.id} and name ${user.name} Registered",
                    entityId = user.id.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            SessionManager.currentUser = LoggedInUser(
                id = user.id,
                role = user.role,
                name = user.name,
                projectIds = listOf()
            )
            return@executeInIO result
        }

    override fun login(name: String, password: String) =
        executeInIO {
            val result = userDataSource.login(name, password).toUser()
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "User with name $name Logged In",
                    entityId = result.id.toString(),
                    timestamp = Clock.System.now().toString(),
                    auditType = AuditType.USER.toString(),
                )
            )
            SessionManager.currentUser = LoggedInUser(
                id = result.id,
                name = result.name,
                role = result.role,
                projectIds = result.projectIds
            )
            return@executeInIO result
        }
}