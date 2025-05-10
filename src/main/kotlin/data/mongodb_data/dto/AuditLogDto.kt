package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class AuditLogDto(
    @BsonId
    val objectId: ObjectId= ObjectId(),
    val id :String,
    val action: String,
    val auditType: String,
    val timestamp: String,
    val entityId: String,
)


