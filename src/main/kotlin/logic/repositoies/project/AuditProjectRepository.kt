package logic.repositoies.project

import logic.models.AuditLog
import java.util.UUID

interface AuditProjectRepository {
    fun getSpecificAuditByProject( projectId: UUID,auditId: UUID): AuditLog
    fun getAllAuditByProject( projectId: UUID): List<AuditLog>
    fun addAuditToProject(projectId: UUID, audit: AuditLog)
    fun auditProjectExists(projectId: UUID, taskId: UUID, audit: AuditLog): Boolean
}