package domain.usecases.audit

import domain.models.AuditLog
import domain.repositories.AuditRepository
import java.util.*

class ViewAuditLogsByTaskUseCase(private val auditRepository: AuditRepository) {
    suspend operator fun invoke(taskId: UUID): List<AuditLog> {
        return auditRepository.getAllLogsByTaskId(taskId)
    }
}