package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.data_source.UserDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toUser
import data.mongodb_data.util.ensureAdminPrivileges
import data.mongodb_data.util.executeInIO
import data.session_manager.LoggedInUser
import data.session_manager.SessionManager
import domain.models.AuditLog.AuditType
import domain.models.User
import domain.repositories.AuthenticationRepository
import domain.repositories.UserRepository
import kotlinx.datetime.Clock
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val auditLogDataSource: AuditLogDataSource
) : UserRepository, AuthenticationRepository {

    override suspend fun addUser(user: User, hashedPassword: String): Boolean = executeInIO {
        ensureAdminPrivileges()
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

    override suspend fun deleteUser(id: UUID): Boolean = executeInIO {
        ensureAdminPrivileges()
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

    override suspend fun assignUserToProject(projectId: UUID, userId: UUID): Boolean =
        modifyUserAssignment(projectId, userId, assign = true)

    override suspend fun unassignUserFromProject(projectId: UUID, userId: UUID): Boolean =
        modifyUserAssignment(projectId, userId, assign = false)

    override suspend fun getUserById(id: UUID): User = executeInIO { userDataSource.getUserById(id).toUser() }

    override suspend fun getAllUsers(): List<User> = executeInIO {
        userDataSource.getAllUsers().toList().map {
            it.toUser()
        }
    }

    override suspend fun assignUserToTask(taskId: UUID, userId: UUID): Boolean = executeInIO {
        ensureAdminPrivileges()
        val result = userDataSource.assignUserToTask(taskId, userId)
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "User with id $userId Assigned to Task with id $taskId",
                entityId = userId.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.USER.toString(),
            )
        )
        result
    }

    override suspend fun register(user: User, hashedPassword: String) = executeInIO {
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

    override suspend fun login(name: String, password: String) = executeInIO {
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

    private suspend fun modifyUserAssignment(projectId: UUID, userId: UUID, assign: Boolean): Boolean = executeInIO {
        ensureAdminPrivileges()
        val result = if (assign) {
            userDataSource.assignUserToProject(projectId, userId)
        } else {
            userDataSource.unassignUserFromProject(projectId, userId)
        }

        val actionVerb = if (assign) "Assigned" else "Unassigned"

        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "User with id $userId $actionVerb to Project with id $projectId",
                entityId = userId.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.USER.toString(),
            )
        )

        result
    }

}