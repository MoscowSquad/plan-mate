package data.mongodb_data.datasource

import data.mongodb_data.dto.AuditLogDto
import java.util.*

interface AuditLogDataSource {
    suspend fun addLog(log: AuditLogDto)
    suspend fun getAllLogsByTaskId(taskId: UUID): List<AuditLogDto>
    suspend fun getAllLogsByProjectId(projectId: UUID): List<AuditLogDto>
}