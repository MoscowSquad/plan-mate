package logic.models

import kotlinx.datetime.LocalDateTime
import java.util.*

data class AuditLog(
    val id: UUID,
    val action: String,
    val auditType: AuditType,
    val timestamp: LocalDateTime,
    val entityId: UUID,
    val userId: UUID,
)


