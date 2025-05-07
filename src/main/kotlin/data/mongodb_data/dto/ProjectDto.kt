package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ProjectDto(
    @BsonId
    val objectId: ObjectId= ObjectId(),
    val id :String,
    val name: String,
)
