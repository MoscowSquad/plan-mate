package logic.usecases.audit

import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.UUID

class ViewAuditLogsByProjectUseCase(private val auditRepository: AuditRepository) {
    operator fun invoke(projectId: UUID): List<AuditLog> {
        return auditRepository.getAllLogsByProjectId(projectId)
    }
}