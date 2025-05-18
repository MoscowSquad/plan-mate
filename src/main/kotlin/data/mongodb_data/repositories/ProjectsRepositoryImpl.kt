package data.mongodb_data.repositories


import data.data_source.AuditLogDataSource
import data.data_source.ProjectsDataSource
import data.data_source.UserDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toProject
import data.mongodb_data.util.ensureAdminPrivileges
import data.mongodb_data.util.executeInIO
import data.session_manager.SessionManager
import domain.models.AuditLog.AuditType
import domain.models.Project
import domain.repositories.ProjectsRepository
import domain.util.NoExistProjectException
import domain.util.NoUserLoginException
import kotlinx.datetime.Clock
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val auditLogDataSource: AuditLogDataSource,
    private val userDataSource: UserDataSource
) : ProjectsRepository {

    override suspend fun addProject(project: Project) = executeInIO {
        ensureAdminPrivileges()
        val result = projectsDataSource.addProject(project.toDto())
        userDataSource.assignUserToProject(
            project.id,
            SessionManager.currentUser?.id ?: throw NoUserLoginException()
        )
        SessionManager.currentUser = SessionManager.currentUser?.copy(
            projectIds = SessionManager.currentUser?.projectIds?.plus(project.id) ?: listOf(project.id)
        )
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Project named ${project.name} with id ${project.id} Created",
                entityId = project.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.PROJECT.toString(),
            )
        )
        result
    }

    override suspend fun updateProject(project: Project) = executeInIO {
        ensureAdminPrivileges()
        val result = projectsDataSource.updateProject(project.toDto())
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Project named ${project.name} with id ${project.id} Updated",
                entityId = project.id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.PROJECT.toString(),
            )
        )
        result
    }

    override suspend fun deleteProject(id: UUID) = executeInIO {
        ensureAdminPrivileges()
        val result = projectsDataSource.deleteProject(id)
        auditLogDataSource.addLog(
            log = AuditLogDto(
                id = UUID.randomUUID().toString(),
                action = "Project with id $id Deleted",
                entityId = id.toString(),
                timestamp = Clock.System.now().toString(),
                auditType = AuditType.PROJECT.toString(),
            )
        )
        result
    }

    override suspend fun getAllProjectsByUser(userId: UUID) = executeInIO {
        val userProjects = SessionManager.currentUser?.projectIds ?: throw NoExistProjectException(userId)
        projectsDataSource.getAllProjects()
            .map { it.toProject() }
            .filter { project -> userProjects.contains(project.id) }
    }

    override suspend fun getProjectById(id: UUID) = executeInIO {
        projectsDataSource.getProjectById(id).toProject()
    }
}