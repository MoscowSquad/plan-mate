package data.csv_data.repositories

import data.csv_data.datasource.AuditLogDataSource
import data.csv_data.mappers.toAudiLog
import domain.models.AuditLog
import domain.models.AuditLog.AuditType
import domain.repositories.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    auditLogDataSource: AuditLogDataSource
) : AuditRepository {
    private val audits = mutableListOf<AuditLog>()

    init {
        val auditLog = auditLogDataSource.fetch()
        audits.addAll(auditLog.map { it.toAudiLog() })
    }

    override suspend fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.TASK && it.entityId == taskId }
    }

    override suspend fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }
    }
}
