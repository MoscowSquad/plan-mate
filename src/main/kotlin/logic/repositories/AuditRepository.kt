package logic.repositories

import logic.models.AuditLog
import java.util.*

interface AuditRepository {
    fun getAllLogsByTaskId(taskId: UUID): List<AuditLog>
    fun getAllLogsByProjectId(projectId: UUID): List<AuditLog>
}