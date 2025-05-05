package data.csv_data.repositories

import data.csv_data.datasource.AuditLogDataSource
import data.csv_data.mappers.toAudiLog
import data.csv_data.mappers.toDto
import logic.models.AuditLog
import logic.models.AuditType
import logic.repositories.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {
    private val audits = mutableListOf<AuditLog>()

    init {
        val auditLog = auditLogDataSource.fetch()
        audits.addAll(auditLog.map { it.toAudiLog() })
    }

    override fun addLog(log: AuditLog) {
        audits.add(log)
        auditLogDataSource.save(audits.map { it.toDto() })
    }

    override fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.TASK && it.entityId == taskId }
    }

    override fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }
    }
}
