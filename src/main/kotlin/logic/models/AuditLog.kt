package logic.models

import java.time.LocalDateTime
import java.util.*

data class AuditLog(
    val id: UUID,
    val auditType: AuditType,
    val action: String,
    val timestamp: LocalDateTime,
    val entityId: UUID,
    val userId: UUID,
)


