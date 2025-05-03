package logic.repositories

import logic.models.AuditLog
import java.util.*

interface AuditRepository {
    fun addLog(log: AuditLog): Boolean
    fun getAllLogsByTaskId(taskId: UUID): List<AuditLog>
    fun getAllLogsByProjectId(projectId: UUID): List<AuditLog>
}