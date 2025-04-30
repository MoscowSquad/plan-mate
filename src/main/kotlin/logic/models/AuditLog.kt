package logic.models

import java.time.Instant
import java.util.*

data class AuditLog(
    val id: UUID,
    val entityType: EntityType,
    val action: String,
    val timestamp: Instant,
    val entityId: UUID,
    val userId: UUID,
)


