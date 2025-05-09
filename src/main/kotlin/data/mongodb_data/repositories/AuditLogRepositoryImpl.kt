package data.mongodb_data.repositories

import data.data_source.AuditLogDataSource
import data.mongodb_data.mappers.toAuditLog
import data.mongodb_data.mappers.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.*

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    override fun addLog(log: AuditLog) {
        return runBlocking(Dispatchers.IO) {
            async {
                auditLogDataSource.addLog(log.toDto())
            }.await()
        }
    }

    override fun getAllLogsByTaskId(taskId: UUID): List<AuditLog> {
        return runBlocking(Dispatchers.IO) {
            async {
                auditLogDataSource.getAllLogsByTaskId(taskId).map {
                    it.toAuditLog()
                }
            }.await()
        }
    }

    override fun getAllLogsByProjectId(projectId: UUID): List<AuditLog> {
        return runBlocking(Dispatchers.IO) {
            async {
                auditLogDataSource.getAllLogsByTaskId(projectId).map {
                    it.toAuditLog()
                }
            }.await()
        }
    }
}