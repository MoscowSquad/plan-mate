package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.data_source.AuditLogDataSource
import data.mongodb_data.dto.AuditLogDto
import kotlinx.coroutines.flow.toList
import java.util.*

class AuditLogDataSourceImpl(
    private val collection: MongoCollection<AuditLogDto>
) : AuditLogDataSource {
    override suspend fun addLog(log: AuditLogDto) {
        collection.insertOne(log)
    }

    override suspend fun getAllLogsByTaskId(taskId: UUID): List<AuditLogDto> {
        val filter = Filters.eq(AuditLogDto::entityId.name, taskId.toString())
        return collection.find(filter).toList()
    }

    override suspend fun getAllLogsByProjectId(projectId: UUID): List<AuditLogDto> {
        val filter = Filters.eq(AuditLogDto::entityId.name, projectId.toString())
        return collection.find(filter).toList()
    }
}