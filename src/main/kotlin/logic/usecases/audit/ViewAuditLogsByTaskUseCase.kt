package logic.usecases.audit

import logic.models.AuditLog
import logic.repositoies.AuditRepository
import java.util.UUID

class ViewAuditLogsByTaskUseCase(private val auditLogUseCase: AuditRepository) {
    operator fun invoke(taskId: UUID):  List<AuditLog>{
        return auditLogUseCase.getAllByTaskId(taskId)
    }
}