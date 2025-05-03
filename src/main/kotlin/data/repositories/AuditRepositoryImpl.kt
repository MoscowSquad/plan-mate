package data.repositories

import data.datasource.AuditLogDataSource
import data.mappers.toDto
import data.mappers.toLogic
import logic.models.AuditLog
import logic.models.AuditType
import logic.repositories.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    private val audits = mutableListOf<AuditLog>()

    init {
        val auditLogDtos = auditLogDataSource.fetch()
        audits.addAll(auditLogDtos.map { it.toLogic() })
    }

    override fun addLog(log: AuditLog): Boolean {
        return try {
            audits.add(log)
            auditLogDataSource.save(audits.map { it.toDto() })
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.TASK && it.entityId == taskId }
    }

    override fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }
    }
}
