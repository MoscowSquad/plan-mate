package logic.usecases.audit

import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.UUID

class ViewAuditLogsByTaskUseCase(private val auditRepository: AuditRepository) {
    operator fun invoke(taskId: UUID): List<AuditLog> {
        return auditRepository.getAllLogsByTaskId(taskId)
    }
}