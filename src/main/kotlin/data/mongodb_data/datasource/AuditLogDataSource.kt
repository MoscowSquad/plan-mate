package data.mongodb_data.datasource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.mongodb_data.dto.AuditLogDto
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson

class AuditLogDataSource(
    private val collection: MongoCollection<AuditLogDto>
) : DataSource<AuditLogDto> {
    override suspend fun add(data: AuditLogDto) {
        collection.insertOne(data)
    }

    override suspend fun update(data: AuditLogDto) {
        val queryParam = Filters.eq("id", data.id)
        val updateParams = listOf(
            Updates.set(AuditLogDto::id.name, data.id),
            Updates.set(AuditLogDto::timestamp.name, data.timestamp),
            Updates.set(AuditLogDto::userId.name, data.userId),
            Updates.set(AuditLogDto::action.name, data.action),
            Updates.set(AuditLogDto::auditType.name, data.auditType),
            Updates.set(AuditLogDto::entityId.name, data.entityId),
        )
        collection.updateOne(
            filter = queryParam,
            update = updateParams
        )
    }

    override suspend fun delete(id: Bson) {
        collection.deleteOne(id)
    }

    override suspend fun getAll(): List<AuditLogDto> {
        return collection.find().toList()
    }

}