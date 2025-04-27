package logic.entities

import java.time.LocalDateTime
import java.util.UUID

data class AuditLog(
    val id: UUID,
    val entityType: EntityType,
    val entityId: UUID,
    val userId: UUID,
    val action: String,
    val timestamp: LocalDateTime
)


