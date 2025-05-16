package domain.usecases.audit

import domain.models.AuditLog
import domain.repositories.AuditRepository
import java.util.*

class ViewAuditLogsByProjectUseCase(private val auditRepository: AuditRepository) {
    suspend operator fun invoke(projectId: UUID): List<AuditLog> {
        return auditRepository.getAllLogsByProjectId(projectId)
    }
}