package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TaskDto(
    @BsonId
    val id: ObjectId,
    val name: String,
    val description: String,
    val projectId: String,
    val stateId: String,
)
