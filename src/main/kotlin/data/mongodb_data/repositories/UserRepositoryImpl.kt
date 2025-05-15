package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.data_source.UserDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toUser
import data.mongodb_data.util.executeInIO
import data.mongodb_data.util.executeInIOAdminOnly
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import kotlinx.datetime.Clock
import logic.models.AuditLog.AuditType
import logic.models.User
import logic.repositories.AuthenticationRepository
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val auditLogDataSource: AuditLogDataSource
) : UserRepository, AuthenticationRepository {

    override fun addUser(user: User, hashedPassword: String): Boolean = executeInIOAdminOnly {
        val result = userDataSource.addUser(user.toDto(hashedPassword))
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "User with id ${user.id} and name ${user.name} Added",
                entityId = user.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.USER.toString(),
            )
        )
        result
    }

    override fun deleteUser(id: UUID): Boolean = executeInIOAdminOnly {
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
        result
    }

    override fun assignUserToProject(projectId: UUID, userId: UUID): Boolean = executeInIOAdminOnly {
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
        result
    }

    override fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean = executeInIOAdminOnly {
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
        result
    }

    override fun getUserById(id: UUID): User = executeInIO { userDataSource.getUserById(id).toUser() }

    override fun getAllUsers(): List<User> = executeInIO {
        userDataSource.getAllUsers().toList().map {
            it.toUser()
        }
    }

    override fun register(user: User, hashedPassword: String) = executeInIO {
        val result = userDataSource.register(user.toDto(hashedPassword)).toUser()
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
        result
    }

    override fun login(name: String, password: String) = executeInIO {
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
        result
    }

}