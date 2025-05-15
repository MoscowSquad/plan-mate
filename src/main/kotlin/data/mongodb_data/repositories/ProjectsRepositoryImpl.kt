package data.mongodb_data.repositories


import data.data_source.AuditLogDataSource
import data.data_source.ProjectsDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toProject
import data.mongodb_data.util.executeInIO
import data.mongodb_data.util.executeInIOAdminOnly
import data.session_manager.SessionManager
import kotlinx.datetime.Clock
import logic.models.AuditLog.AuditType
import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val auditLogDataSource: AuditLogDataSource,
) : ProjectsRepository {

    override fun addProject(project: Project) = executeInIOAdminOnly {
        val result = projectsDataSource.addProject(project.toDto())
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

    override fun updateProject(project: Project) = executeInIOAdminOnly {
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

    override fun deleteProject(id: UUID) = executeInIOAdminOnly {
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

    override fun getAllProjectsByUser(userId: UUID) = executeInIO {
        val userProjects = SessionManager.currentUser!!.projectIds
        projectsDataSource.getAllProjects()
            .map { it.toProject() }
            .filter { project -> userProjects.contains(project.id) }
    }

    override fun getProjectById(id: UUID) = executeInIO {
        projectsDataSource.getProjectById(id).toProject()
    }
}