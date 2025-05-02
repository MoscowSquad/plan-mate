package logic.repositoies

import logic.models.AuditLog
import java.util.*

interface AuditRepository {
    fun add(log: AuditLog?): Boolean
    fun getAllByTaskId(taskId: UUID): List<AuditLog>
    fun getAllByProjectId(projectId: UUID): List<AuditLog>
}