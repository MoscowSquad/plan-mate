package data.mongodb_data.repositories


import data.data_source.AuditLogDataSource
import data.data_source.ProjectsDataSource
import data.mongodb_data.dto.AuditLogDto
import data.mongodb_data.mappers.toDto
import data.mongodb_data.mappers.toProject
import data.mongodb_data.util.executeInIO
import logic.models.AuditType
import logic.models.Project
import logic.repositories.ProjectsRepository
import java.util.*


class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val auditLogDataSource: AuditLogDataSource,
) : ProjectsRepository {

    override fun addProject(project: Project) =
        executeInIO {
            val result = projectsDataSource.addProject(project.toDto())
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "Project named ${project.name} with id ${project.id} Created",
                    entityId = project.id.toString(),
                    timestamp = System.currentTimeMillis().toString(),
                    auditType = AuditType.PROJECT.toString(),
                )
            )
            return@executeInIO result
        }

    override fun updateProject(project: Project) =
        executeInIO {
            val result = projectsDataSource.updateProject(project.toDto())
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "Project named ${project.name} with id ${project.id} Updated",
                    entityId = project.id.toString(),
                    timestamp = System.currentTimeMillis().toString(),
                    auditType = AuditType.PROJECT.toString(),
                )
            )
            return@executeInIO result
        }

    override fun deleteProject(id: UUID) =
        executeInIO {
            val result = projectsDataSource.deleteProject(id)
            auditLogDataSource.addLog(
                log = AuditLogDto(
                    id = UUID.randomUUID().toString(),
                    action = "Project with id $id Deleted",
                    entityId = id.toString(),
                    timestamp = System.currentTimeMillis().toString(),
                    auditType = AuditType.PROJECT.toString(),
                )
            )
            return@executeInIO result
        }

    override fun getAllProjects() =
        executeInIO {
            projectsDataSource.getAllProjects().map {
                it.toProject()
            }
        }

    override fun getProjectById(id: UUID) =
        executeInIO { projectsDataSource.getProjectById(id).toProject() }
}