package logic.entities

import logic.models.EntityType
import java.time.LocalDateTime
import java.util.UUID

data class AuditLog(
    val id: UUID,
    val entityType: EntityType,
    val action: String,
    val timestamp: LocalDateTime,
    val entityId: UUID,
    val userId: UUID,
)


