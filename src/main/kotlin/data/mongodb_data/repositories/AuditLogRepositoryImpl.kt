package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.mongodb_data.mappers.toAuditLog
import data.mongodb_data.mappers.toDto
import kotlinx.coroutines.*
import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.*

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun addLog(log: AuditLog) {
        scope.launch {
            auditLogDataSource.addLog(log.toDto())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> {
        val deferred = scope.async {
            auditLogDataSource.getAllLogsByTaskId(taskId).map {
                it.toAuditLog()
            }
        }
        return deferred.getCompleted()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> {
        val deferred = scope.async {
            auditLogDataSource.getAllLogsByTaskId(projectId).map {
                it.toAuditLog()
            }
        }
        return deferred.getCompleted()
    }
}