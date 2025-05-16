package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SubTaskDto(
    @BsonId
    val objectId: ObjectId = ObjectId(),
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val parentTaskId: String
)