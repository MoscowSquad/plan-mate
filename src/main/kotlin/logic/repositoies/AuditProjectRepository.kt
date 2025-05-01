package logic.repositoies

import logic.models.AuditLog
import java.util.UUID

interface AuditProjectRepository {
    fun getByProjectId(projectId: UUID, auditId: UUID): AuditLog?
    fun getAll(projectId: UUID): List<AuditLog>?
    fun addAuditToProject(projectId: UUID, audit: AuditLog)
}