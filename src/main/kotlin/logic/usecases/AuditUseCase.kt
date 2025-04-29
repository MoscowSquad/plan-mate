package logic.usecases

import logic.repositories.AuditRepository

class AuditUseCase(
    private val auditRepository: AuditRepository
) {
}