package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.mongodb_data.mappers.toAuditLog
import data.mongodb_data.util.executeInIO
import domain.models.AuditLog
import domain.repositories.AuditRepository
import java.util.*

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    override suspend fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> = executeInIO {
        auditLogDataSource.getAllLogsByTaskId(taskId).map {
            it.toAuditLog()
        }
    }

    override suspend fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> = executeInIO {
        auditLogDataSource.getAllLogsByTaskId(projectId).map {
            it.toAuditLog()
        }
    }
}