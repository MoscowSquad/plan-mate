package logic.usecases.audit

import logic.models.AuditLog
import logic.repositories.AuditRepository
import java.util.UUID

class ViewAuditLogsByProjectUseCase(private val auditLogUseCase: AuditRepository) {
    operator fun invoke(projectId: UUID): List<AuditLog>{
        return auditLogUseCase.getAllLogsByProjectId(projectId)
    }
}