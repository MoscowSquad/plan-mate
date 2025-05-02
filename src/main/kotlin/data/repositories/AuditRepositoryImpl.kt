package data.repositories

import data.datasource.AuditLogDataSource
import logic.models.AuditLog
import logic.models.AuditType
import logic.repositories.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    private val audits = mutableListOf<AuditLog>()

    init {
        audits.addAll(auditLogDataSource.fetch())
    }

    override fun add(log: AuditLog?): Boolean {
        return try {
            if (log != null) {
                audits.add(log)
                auditLogDataSource.save(audits)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllByTaskId(taskId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.TASK && it.entityId == taskId }
    }

    override fun getAllByProjectId(projectId: UUID): List<AuditLog> {
        return audits.filter { it.auditType == AuditType.PROJECT && it.entityId == projectId }
    }
}
