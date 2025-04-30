package logic.usecases.project

import logic.models.AuditLog
import logic.repositoies.project.AuditProjectRepository
import utilities.exception.ValidateProjectExists

import java.util.*

class AuditProjectUseCase (
    private val auditRepository: AuditProjectRepository,
    private val validateProjectExists: ValidateProjectExists
    ) {

        fun getSpecificAuditByProject(projectID: UUID,auditId:UUID):AuditLog{
            validateProjectExists.validateProjectExists(projectID)
            return auditRepository.getSpecificAuditByProject(projectID,auditId)
        }

      fun getAllAuditByProject(projectID: UUID):List<AuditLog>{
          validateProjectExists.validateProjectExists(projectID)
        return auditRepository.getAllAuditByProject(projectID)
     }
    fun addAuditToProject(projectID: UUID, auditId: AuditLog){
        validateProjectExists.validateProjectExists(projectID)
        auditRepository.addAuditToProject(projectID,auditId)
    }
}