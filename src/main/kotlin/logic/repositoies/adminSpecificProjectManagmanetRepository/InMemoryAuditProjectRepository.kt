package logic.repositoies.adminSpecificProjectManagmanetRepository

import logic.models.AuditLog
import utilities.ValidatorForASPM.ValidateProjectExists
import java.util.*

// Real in-memory implementation for testing
class InMemoryAuditProjectRepository : AuditProjectRepository {
    private val projectAudits = mutableMapOf<UUID, MutableList<AuditLog>>()

    override fun getSpecificAuditByProject(projectId: UUID, auditId: UUID): AuditLog {
        return projectAudits[projectId]?.find { it.id == auditId }
            ?: throw NoSuchElementException("Audit not found")
    }

    override fun getAllAuditByProject(projectId: UUID): List<AuditLog> {
        return projectAudits[projectId] ?: emptyList()
    }

    override fun addAuditToProject(projectId: UUID, audit: AuditLog) {
        projectAudits.getOrPut(projectId) { mutableListOf() }.add(audit)
    }

    override fun auditProjectExists(projectId: UUID, taskId: UUID, audit: AuditLog): Boolean {
        return projectAudits[projectId]?.any {
            (it.id == audit.id) && (it.id == taskId)
        } ?: false
    }
}

//// Always passing validator for testing
//class AlwaysValidProjectValidator : ValidateProjectExists {
//    override fun validateProjectExists(projectId: UUID) {
//        // Always passes validation
//    } }
