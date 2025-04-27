package logic.entities

import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val hashedPassword: String,
    val role: Role
)
