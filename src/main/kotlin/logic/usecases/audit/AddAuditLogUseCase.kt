package logic.usecases.audit

import logic.models.AuditLog
import logic.repositories.AuditRepository

class AddAuditLogUseCase (private val auditLogRepository : AuditRepository){
    fun invoke(log: AuditLog?): Boolean {
        return auditLogRepository.add(log)
    }
}