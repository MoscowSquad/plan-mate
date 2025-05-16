package domain.repositories

import domain.models.AuditLog
import java.util.*

interface AuditRepository {
    suspend fun getAllLogsByTaskId(taskId: UUID): List<AuditLog>
    suspend fun getAllLogsByProjectId(projectId: UUID): List<AuditLog>
}