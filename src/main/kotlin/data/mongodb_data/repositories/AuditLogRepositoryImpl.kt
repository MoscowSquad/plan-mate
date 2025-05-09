package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.mongodb_data.mappers.toAuditLog
import data.mongodb_data.mappers.toDto
import data.mongodb_data.util.executeInIO
import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.*

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    override fun addLog(log: AuditLog) =
        executeInIO { auditLogDataSource.addLog(log.toDto()) }

    override fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> =
        executeInIO {
            auditLogDataSource.getAllLogsByTaskId(taskId).map {
                it.toAuditLog()
            }
        }

    override fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> =
        executeInIO {
            auditLogDataSource.getAllLogsByTaskId(projectId).map {
                it.toAuditLog()
            }
        }
}