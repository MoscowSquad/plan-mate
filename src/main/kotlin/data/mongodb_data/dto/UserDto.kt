package data.mongodb_data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserDto(
    @BsonId
    val id: ObjectId,
    val name: String,
    val hashedPassword: String,
    val role: String,
    val projectIds: List<String>
)