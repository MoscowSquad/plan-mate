package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TaskStateDto(
    @BsonId
    val id: ObjectId,
    val name: String,
    val projectId: String,
)